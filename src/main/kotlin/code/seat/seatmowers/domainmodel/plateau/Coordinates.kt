package code.seat.seatmowers.domainmodel.plateau

import lombok.Value
import org.springframework.util.Assert

@Value
data class Coordinates(val x: Int, val y: Int) {
    init {
        assertCoordinatesAreValid(x, y)
    }

    private fun assertCoordinatesAreValid(x: Int, y: Int) {
        Assert.state(x > 0, "Plateau X upper coordinate cannot be 0 or lower")
        Assert.state(y > 0, "Plateau Y upper coordinate cannot be 0 or lower")
    }
}
