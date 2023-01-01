package code.seat.seatmowers.application.projections

import code.seat.seatmowers.domainmodel.plateau.NewPlateauWasCreated
import code.seat.seatmowers.infrastructure.delivery.spring.jpa.Plateau
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component
import javax.persistence.EntityManager

@Component
class PlateauProjection(private val entityManager: EntityManager) {
    @EventHandler
    fun on(event: NewPlateauWasCreated) {
        val plateau = Plateau(event.newPlateauId, event.x, event.y)
        entityManager.persist(plateau)
    }
}
