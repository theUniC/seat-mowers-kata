package code.seat.seatmowers.application.command

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.UUID

data class MoveMowerCommand(
    @TargetAggregateIdentifier val plateauId: UUID,
    val mowerId: UUID,
    val to: String
)
