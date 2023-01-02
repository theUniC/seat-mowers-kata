package code.seat.seatmowers.infrastructure.delivery.spring.dtos

import org.hibernate.annotations.Type
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "mowers")
class MowerOutputDto {
    @Id
    @Type(type = "uuid-char")
    @Column(columnDefinition = "CHAR(36)")
    private var id: UUID

    @Type(type = "uuid-char")
    @Column(columnDefinition = "CHAR(36)")
    private var plateauId: UUID
    private var x: Int
    private var y: Int
    private var direction: String

    constructor(id: UUID, plateauId: UUID, x: Int, y: Int, direction: String) {
        this.id = id
        this.plateauId = plateauId
        this.x = x
        this.y = y
        this.direction = direction
    }

    fun getId() = id
    fun getPlateauId() = plateauId
    fun getX() = x
    fun getY() = y
    fun getDirection() = direction
}
