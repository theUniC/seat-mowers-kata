package code.seat.seatmowers.application.projection

import code.seat.seatmowers.domainmodel.plateau.NewPlateauWasCreated
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.PlateauOutputDto
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component
import javax.persistence.EntityManager

@Component
class PlateauProjection(private val entityManager: EntityManager) {
    @EventHandler
    fun on(event: NewPlateauWasCreated) {
        val plateau = PlateauOutputDto(event.newPlateauId, event.x, event.y)
        entityManager.persist(plateau)
    }
}
