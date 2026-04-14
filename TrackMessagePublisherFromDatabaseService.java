package ru.tomsknipi.track_gateway_service.service;

import lombok.extern.log4j.Log4j2;
import lombok.var;
import org.hibernate.ScrollMode;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.tomsknipi.track_gateway_service.models.dbo.TrackQueueShort;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@Log4j2
public class TrackMessagePublisherFromDatabaseService {

    int maxCountIterates = 100;
    private final RabbitTemplate ektpSenderRabbitTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    public TrackMessagePublisherFromDatabaseService(@Qualifier("ektpSenderRabbitTemplate") RabbitTemplate ektpSenderRabbitTemplate) {
        this.ektpSenderRabbitTemplate = ektpSenderRabbitTemplate;
    }

    @PostConstruct
    public void processAllMessages() {
        // Получаем SessionFactory и открываем StatelessSession
        SessionFactory sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);

        try (StatelessSession session = sessionFactory.openStatelessSession()) {
            Transaction tx = session.beginTransaction();

            String hql = "FROM TrackQueueShort ORDER BY createDate ASC";

            var query = session.createQuery(hql, TrackQueueShort.class);

            // 2. Устанавливаем размер пакета (столько строк будет в Heap)
            query.setFetchSize(50);

            var scrollableResults = query.scroll(ScrollMode.FORWARD_ONLY);
            int currentIterate = 0;
            while (scrollableResults.next() && currentIterate < maxCountIterates) {
                TrackQueueShort item = (TrackQueueShort) scrollableResults.get(0);
                this.ektpSenderRabbitTemplate.convertAndSend("int:ektp_cgis_orenburg", item.getBody());
                currentIterate++;
            }

            tx.commit();
        } catch (Exception e) {
            log.error("Error processing queue", e);
        }
        log.info("Sending finished");
    }
}
