package code.seat.seatmowers.domainmodel.plateau

import code.seat.seatmowers.application.command.CreatePlateau
import code.seat.seatmowers.application.command.DeployRover
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle.apply
import org.axonframework.modelling.command.AggregateMember
import org.axonframework.spring.stereotype.Aggregate
import org.springframework.util.Assert

@Aggregate
class Plateau {
    @AggregateIdentifier
    private lateinit var id: String
    private lateinit var coordinates: Coordinates

    @AggregateMember
    private lateinit var rovers: MutableList<Rover>

    @CommandHandler
    constructor(command: CreatePlateau) {
        apply(NewPlateauWasCreated(command.id, command.x, command.y))
    }

    @CommandHandler
    fun deployRover(command: DeployRover) {
        apply(RoverWasDeployed(command.id, command.x, command.y, command.direction))
    }

    @EventSourcingHandler
    fun on(event: NewPlateauWasCreated) {
        id = event.newPlateauId.toString()
        coordinates = Coordinates(event.x, event.y)
        rovers = mutableListOf()
    }

    @EventSourcingHandler
    fun on(event: RoverWasDeployed) {
        assertCoordinatesAreWithinPlateauBounds(event.x, event.y)
        rovers.add(
            Rover(event.id, Coordinates(event.x, event.y), Direction.valueOf(event.direction))
        )
    }

    private fun assertCoordinatesAreWithinPlateauBounds(x: Int, y: Int) {
        Assert.state(x <= coordinates.x, "X component is not within plateau bounds")
        Assert.state(y <= coordinates.y, "Y component is not within plateau bounds")
    }
}
