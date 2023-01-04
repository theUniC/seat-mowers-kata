package code.seat.seatmowers.infrastructure.delivery.spring.exceptions

class ParseException(type: Type, parsedString: String) : RuntimeException("Parse exception for [${type.name}]: $parsedString") {
    enum class Type {
        PLATEAU,
        MOWER,
        MOVEMENTS,
    }
}
