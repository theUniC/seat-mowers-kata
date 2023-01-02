package code.seat.seatmowers.infrastructure.delivery.spring.dtos

import org.hibernate.annotations.Type
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "plateaus")
@Relation(collectionRelation = "plateaus", itemRelation = "plateau")
class PlateauOutputDto : RepresentationModel<PlateauOutputDto> {
    @Id
    @Type(type = "uuid-char")
    @Column(columnDefinition = "CHAR(36)")
    private var id: UUID
    private var x: Int = 0
    private var y: Int = 0

    constructor(id: UUID, x: Int, y: Int) {
        this.id = id
        this.x = x
        this.y = y
    }

    fun getId() = id
    fun getX() = x
    fun getY() = y
}
