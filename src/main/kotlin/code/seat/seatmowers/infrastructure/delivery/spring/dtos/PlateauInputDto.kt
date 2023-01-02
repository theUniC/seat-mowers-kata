package code.seat.seatmowers.infrastructure.delivery.spring.dtos

import javax.validation.constraints.Positive

data class PlateauInputDto(@get:Positive val x: Int, @get:Positive val y: Int)
