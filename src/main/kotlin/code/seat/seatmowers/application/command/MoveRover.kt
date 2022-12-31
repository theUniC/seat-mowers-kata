package code.seat.seatmowers.application.command

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.UUID

data class MoveRover(
    @TargetAggregateIdentifier val plateauId: UUID,
    val roverId: UUID,
    val to: String
)
