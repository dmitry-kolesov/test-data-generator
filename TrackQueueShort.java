package ru.tomsknipi.track_gateway_service.models.dbo;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import ru.tomsknipi.track_gateway_service.models.BaseId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name = "track_queue_message")
@Getter
@Setter
@DynamicUpdate
@TypeDefs({
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class TrackQueueShort extends BaseId {
    /**
     * Дата создания
     */
    @NotNull
    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

    /**
     * Название файла
     */
    @NotNull
    @Column(name = "message_id", nullable = false)
    private String messageId;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", updatable = false)
    private String body;
}
