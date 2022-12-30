package code.seat.seatmowers.domainmodel.plateau

import org.axonframework.modelling.command.EntityId
import java.util.UUID

class Rover {
    @EntityId
    private var id: String
    private var coordinates: Coordinates
    private var direction: Direction

    constructor(id: UUID, coordinates: Coordinates, direction: Direction) {
        this.id = id.toString()
        this.coordinates = coordinates
        this.direction = direction
    }
}
