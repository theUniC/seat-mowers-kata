package code.seat.seatmowers.application.projection

import code.seat.seatmowers.domainmodel.mower.MowerWasDeployed
import code.seat.seatmowers.domainmodel.mower.MowerWasMoved

interface MowerProjection {
    fun on(event: MowerWasDeployed)
    fun on(event: MowerWasMoved)
}
