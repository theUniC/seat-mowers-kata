package code.seat.seatmowers.infrastructure.delivery.spring.dtos

import org.springframework.hateoas.CollectionModel

class PlateauOutputDtoCollection(ps: List<PlateauOutputDto>) : CollectionModel<PlateauOutputDto>(ps)
