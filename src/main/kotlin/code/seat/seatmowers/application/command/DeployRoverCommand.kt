package code.seat.seatmowers.application.command

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.UUID

data class DeployRoverCommand(
    @TargetAggregateIdentifier val plateauId: UUID,
    val id: UUID,
    val x: Int,
    val y: Int,
    val direction: String
)
