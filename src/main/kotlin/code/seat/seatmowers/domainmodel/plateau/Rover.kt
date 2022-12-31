package code.seat.seatmowers.domainmodel.plateau

import org.axonframework.modelling.command.EntityId
import org.springframework.util.Assert
import java.util.UUID

class Rover {
    @EntityId
    private var id: String
    private var position: Position

    constructor(id: UUID, position: Position) {
        this.id = id.toString()
        this.position = position
    }

    fun id() = id
    fun position() = position

    fun move(movement: Movement, plateau: Plateau) {
        val newPosition = when (movement) {
            Movement.M -> position.moveForward()
            Movement.L -> position.turnLeft()
            else -> position.turnRight()
        }

        assertPositionIsNotOutOfBounds(newPosition, plateau)
        assertPositionIsNotAlreadyOccupied(newPosition, plateau)
        position = newPosition
    }

    private fun assertPositionIsNotAlreadyOccupied(newPosition: Position, plateau: Plateau) {
        if (!plateau.isPositionFreeExceptFor(newPosition, this)) {
            throw PositionIsAlreadyOccupied(newPosition)
        }
    }

    private fun assertPositionIsNotOutOfBounds(newPosition: Position, plateau: Plateau) {
        Assert.state(newPosition.coordinates.x <= plateau.coordinates().x, "New mower X position is out of plateau bounds")
        Assert.state(newPosition.coordinates.y <= plateau.coordinates().y, "New mower Y position is out of plateau bounds")
    }
}
