package code.seat.seatmowers.infrastructure.query

import code.seat.seatmowers.application.query.readlayer.PlateauReadLayer
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.PlateauOutputDto
import org.springframework.stereotype.Component
import java.util.Optional
import java.util.UUID
import javax.persistence.EntityManager

@Component
class HibernatePlateauReadLayer(private val entityManager: EntityManager) : PlateauReadLayer {
    override fun all(offset: Int, limit: Int): List<PlateauOutputDto> {
        return entityManager
            .createQuery("SELECT p FROM PlateauOutputDto p", PlateauOutputDto::class.java)
            .setFirstResult(offset)
            .setMaxResults(limit)
            .resultList
    }

    override fun byId(id: UUID): Optional<PlateauOutputDto> =
        Optional.ofNullable(
            entityManager.find(PlateauOutputDto::class.java, id)
        )
}
