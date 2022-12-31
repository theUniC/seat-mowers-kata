package code.seat.seatmowers.domainmodel.mower

enum class Direction {
    NORTH {
        override fun left(): Direction = WEST
        override fun right(): Direction = EAST
    },
    SOUTH {
        override fun left(): Direction = EAST
        override fun right(): Direction = WEST
    },
    WEST {
        override fun left(): Direction = SOUTH
        override fun right(): Direction = NORTH
    },
    EAST {
        override fun left(): Direction = NORTH
        override fun right(): Direction = SOUTH
    };

    abstract fun left(): Direction
    abstract fun right(): Direction

    companion object {
        fun fromLiteral(rawDirection: String) = when (rawDirection.lowercase()) {
            "n" -> NORTH
            "w" -> WEST
            "e" -> EAST
            else -> SOUTH
        }
    }
}
