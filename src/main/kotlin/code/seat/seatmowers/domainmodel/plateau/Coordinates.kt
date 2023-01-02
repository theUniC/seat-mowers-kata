package code.seat.seatmowers.domainmodel.plateau

import lombok.Value
import org.springframework.util.Assert

@Value
data class Coordinates(val x: Int, val y: Int) {
    init {
        assertCoordinatesAreValid(x, y)
    }

    private fun assertCoordinatesAreValid(x: Int, y: Int) {
        Assert.state(x >= 0, "X upper coordinate component cannot be lower than 0")
        Assert.state(y >= 0, "Y upper coordinate component cannot be lower than 0")
    }
}
