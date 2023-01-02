package code.seat.seatmowers.domainmodel.mower

import org.axonframework.modelling.command.EntityId
import java.util.UUID

class Mower {
    @EntityId
    private var id: String
    private var position: Position

    constructor(id: UUID, position: Position) {
        this.id = id.toString()
        this.position = position
    }

    fun id() = id
    fun position() = position

    fun moveTo(newPosition: Position) {
        position = newPosition
    }

    fun newPositionFor(movement: Movement) = when (movement) {
        Movement.M -> position.moveForward()
        Movement.L -> position.turnLeft()
        else -> position.turnRight()
    }
}
