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
@Table(name = "mowers")
@Relation(collectionRelation = "mowers", itemRelation = "mower")
class MowerOutputDto : RepresentationModel<MowerOutputDto> {
    @Id
    @Type(type = "uuid-char")
    @Column(columnDefinition = "CHAR(36)")
    private var id: UUID

    @Type(type = "uuid-char")
    @Column(columnDefinition = "CHAR(36)")
    private var plateauId: UUID
    var x: Int
    var y: Int
    var direction: String

    constructor(id: UUID, plateauId: UUID, x: Int, y: Int, direction: String) {
        this.id = id
        this.plateauId = plateauId
        this.x = x
        this.y = y
        this.direction = direction[0].uppercase()
    }

    fun getId() = id
    fun getPlateauId() = plateauId
}
