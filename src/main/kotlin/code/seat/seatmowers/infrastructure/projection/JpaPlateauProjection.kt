package code.seat.seatmowers.infrastructure.projection

import code.seat.seatmowers.application.projection.PlateauProjection
import code.seat.seatmowers.domainmodel.plateau.NewPlateauWasCreated
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.PlateauOutputDto
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component
import javax.persistence.EntityManager

@Component
class JpaPlateauProjection(private val entityManager: EntityManager) : PlateauProjection {
    @EventHandler
    override fun on(event: NewPlateauWasCreated) {
        val plateau = PlateauOutputDto(event.newPlateauId, event.x, event.y)
        entityManager.persist(plateau)
    }
}
