package code.seat.seatmowers.application.projection

import code.seat.seatmowers.domainmodel.plateau.NewPlateauWasCreated

interface PlateauProjection {
    fun on(event: NewPlateauWasCreated)
}
