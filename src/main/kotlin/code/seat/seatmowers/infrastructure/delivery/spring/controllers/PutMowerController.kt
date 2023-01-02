package code.seat.seatmowers.infrastructure.delivery.spring.controllers

import code.seat.seatmowers.application.command.MoveRoverCommand
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.MowerMovementInputDto
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import java.util.concurrent.Future
import javax.validation.Valid

@RestController
class PutMowerController(val commandGateway: CommandGateway) {
    @PutMapping("/plateaus/{plateauId}/mowers/{mowerId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    fun handleRequest(@PathVariable plateauId: UUID, @PathVariable mowerId: UUID, @Valid @RequestBody mowerMovementInputDto: MowerMovementInputDto): Future<Unit> =
        commandGateway
            .send(MoveRoverCommand(plateauId, mowerId, mowerMovementInputDto.movement))
}
