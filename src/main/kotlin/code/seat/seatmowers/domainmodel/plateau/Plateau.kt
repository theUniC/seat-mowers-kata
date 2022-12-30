package code.seat.seatmowers.domainmodel.plateau

import code.seat.seatmowers.application.command.CreatePlateau
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle.apply
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class Plateau {
    @AggregateIdentifier
    private lateinit var id: String
    private lateinit var coordinates: Coordinates

    @CommandHandler
    constructor(command: CreatePlateau) {
        apply(NewPlateauWasCreated(command.id, command.x, command.y))
    }

    @EventSourcingHandler
    fun on(event: NewPlateauWasCreated) {
        id = event.newPlateauId.toString()
        coordinates = Coordinates(event.x, event.y)
    }
}
