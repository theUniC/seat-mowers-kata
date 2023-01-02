package code.seat.seatmowers.infrastructure.projection

import code.seat.seatmowers.application.projection.MowerProjection
import code.seat.seatmowers.domainmodel.mower.MowerWasDeployed
import code.seat.seatmowers.domainmodel.mower.MowerWasMoved
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.MowerOutputDto
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component
import javax.persistence.EntityManager
@Component
class JpaMowerProjection(val entityManager: EntityManager) : MowerProjection {
    @EventHandler
    override fun on(event: MowerWasDeployed) {
        val newMower = MowerOutputDto(event.id, event.plateauId, event.x, event.y, event.direction)
        entityManager.persist(newMower)
    }

    @EventHandler
    override fun on(event: MowerWasMoved) {
        val mowerToMove = entityManager.find(MowerOutputDto::class.java, event.id)
        mowerToMove.x = event.newX
        mowerToMove.y = event.newY
        mowerToMove.direction = event.newDirection
    }
}
