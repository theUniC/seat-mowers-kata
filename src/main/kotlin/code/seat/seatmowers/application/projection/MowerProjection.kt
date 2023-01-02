package code.seat.seatmowers.application.projection

import code.seat.seatmowers.domainmodel.mower.MowerWasDeployed
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.MowerOutputDto
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component
import javax.persistence.EntityManager

@Component
class MowerProjection(val entityManager: EntityManager) {
    @EventHandler
    fun on(event: MowerWasDeployed) {
        val newMower = MowerOutputDto(event.id, event.plateauId, event.x, event.y, event.direction)
        entityManager.persist(newMower)
    }
}
