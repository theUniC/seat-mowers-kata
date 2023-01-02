package code.seat.seatmowers.domainmodel.plateau

import code.seat.seatmowers.application.command.CreatePlateauCommand
import code.seat.seatmowers.application.command.DeployRoverCommand
import code.seat.seatmowers.application.command.MoveRoverCommand
import code.seat.seatmowers.domainmodel.mower.Direction
import code.seat.seatmowers.domainmodel.mower.Movement
import code.seat.seatmowers.domainmodel.mower.Mower
import code.seat.seatmowers.domainmodel.mower.MowerWasDeployed
import code.seat.seatmowers.domainmodel.mower.MowerWasMoved
import code.seat.seatmowers.domainmodel.mower.Position
import code.seat.seatmowers.domainmodel.mower.PositionIsAlreadyOccupied
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle.apply
import org.axonframework.modelling.command.AggregateMember
import org.axonframework.modelling.command.ForwardMatchingInstances
import org.axonframework.spring.stereotype.Aggregate
import org.springframework.util.Assert
import java.util.UUID

@Aggregate
class Plateau {
    @AggregateIdentifier
    private lateinit var id: String
    private lateinit var coordinates: Coordinates

    @AggregateMember(eventForwardingMode = ForwardMatchingInstances::class)
    private lateinit var mowers: MutableMap<UUID, Mower>

    @CommandHandler
    constructor(command: CreatePlateauCommand) {
        val coordinates = Coordinates(command.x, command.y)
        apply(NewPlateauWasCreated(command.id, coordinates.x, coordinates.y))
    }

    fun coordinates() = coordinates
    fun rovers() = mowers.values.toList()

    @CommandHandler
    fun deployRover(command: DeployRoverCommand) {
        assertCoordinatesAreWithinPlateauBounds(command.x, command.y)
        assertDirectionIsValid(command.direction)
        assertPositionIsNotOccupied(Position(Coordinates(command.x, command.y), Direction.fromLiteral(command.direction)))

        apply(MowerWasDeployed(command.id, UUID.fromString(this.id), command.x, command.y, command.direction))
    }

    @CommandHandler
    fun moveRover(command: MoveRoverCommand) {
        assertMovementIsValid(command.to)
        assertRoverExists(command.roverId)

        val mower = mowers[command.roverId]
        val movement = Movement.valueOf(command.to)
        val newPosition = mower!!.newPositionFor(movement)

        assertPositionIsNotOutOfBounds(newPosition)
        assertPositionIsNotAlreadyOccupied(newPosition, mower)

        apply(MowerWasMoved(command.plateauId, command.roverId, newPosition.coordinates.x, newPosition.coordinates.y, newPosition.direction.toString()))
    }

    @EventSourcingHandler
    fun on(event: NewPlateauWasCreated) {
        id = event.newPlateauId.toString()
        coordinates = Coordinates(event.x, event.y)
        mowers = mutableMapOf()
    }

    @EventSourcingHandler
    fun on(event: MowerWasDeployed) {
        val position = Position(
            Coordinates(event.x, event.y),
            Direction.fromLiteral(event.direction)
        )

        mowers[event.id] = Mower(event.id, position)
    }

    @EventSourcingHandler
    fun on(event: MowerWasMoved) {
        mowers[event.id]!!.moveTo(
            Position(
                Coordinates(event.newX, event.newY),
                Direction.valueOf(event.newDirection)
            )
        )
    }

    fun isPositionFreeExceptFor(p: Position, except: Mower? = null): Boolean {
        var rovers = mowers.values

        if (except != null) {
            rovers = rovers.filterNot { r -> r.id() == except.id() }.toMutableList()
        }

        return !rovers.any { r -> r.position().coordinates == p.coordinates }
    }

    private fun assertPositionIsNotOccupied(position: Position) {
        if (!isPositionFreeExceptFor(position)) {
            throw PositionIsAlreadyOccupied(position)
        }
    }

    private fun assertCoordinatesAreWithinPlateauBounds(x: Int, y: Int) {
        Assert.state(x <= coordinates.x, "X component is not within plateau bounds")
        Assert.state(y <= coordinates.y, "Y component is not within plateau bounds")
    }

    private fun assertDirectionIsValid(direction: String) {
        Assert.state(direction.lowercase().contains("[nswe]".toRegex()), "Direction $direction is not valid")
    }

    private fun assertRoverExists(roverId: UUID) {
        Assert.notNull(mowers[roverId], "Rover with ID of $roverId does not exists in this plateau")
    }

    private fun assertMovementIsValid(to: String) {
        Assert.state(to.lowercase().contains("[mlr]".toRegex()), "Movement $to is not valid")
    }

    private fun assertPositionIsNotAlreadyOccupied(newPosition: Position, exceptMower: Mower) {
        if (!isPositionFreeExceptFor(newPosition, exceptMower)) {
            throw PositionIsAlreadyOccupied(newPosition)
        }
    }

    private fun assertPositionIsNotOutOfBounds(newPosition: Position) {
        Assert.state(newPosition.coordinates.x <= coordinates.x, "New mower X position is out of plateau bounds")
        Assert.state(newPosition.coordinates.y <= coordinates.y, "New mower Y position is out of plateau bounds")
    }
}
