package code.seat.seatmowers.infrastructure.query

import code.seat.seatmowers.application.query.readlayer.MowerReadLayer
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.MowerOutputDto
import org.springframework.stereotype.Component
import java.util.UUID
import javax.persistence.EntityManager

@Component
class HibernateMowerReadLayer(private val entityManager: EntityManager) : MowerReadLayer {
    override fun byPlateau(plateauId: UUID): List<MowerOutputDto> =
        entityManager
            .createQuery("SELECT m FROM MowerOutputDto m WHERE m.plateauId = :plateauId", MowerOutputDto::class.java)
            .setParameter("plateauId", plateauId)
            .resultList
}
