package code.seat.seatmowers.application.command

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.UUID

data class MoveRoverCommand(
    @TargetAggregateIdentifier val plateauId: UUID,
    val roverId: UUID,
    val to: String
)
