package code.seat.seatmowers.infrastructure.delivery.spring.entities

import org.hibernate.annotations.Type
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "executions")
@EntityListeners(AuditingEntityListener::class)
class Execution {
    @Id
    @Type(type = "uuid-char")
    @Column(columnDefinition = "CHAR(36)")
    private var id: UUID
    private var instructions: String
    private var createdAt: LocalDateTime

    constructor(id: UUID, instructions: String) {
        this.id = id
        this.instructions = instructions
        this.createdAt = LocalDateTime.now()
    }

    fun getId() = id
    fun getInstructions() = instructions
    fun createdAt() = createdAt
}
