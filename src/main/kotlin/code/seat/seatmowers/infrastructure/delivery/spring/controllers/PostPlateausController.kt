package code.seat.seatmowers.infrastructure.delivery.spring.controllers

import code.seat.seatmowers.application.command.CreatePlateauCommand
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.PlateauInputDto
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.PlateauOutputDto
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class PostPlateausController(val commandGateway: CommandGateway) {
    @PostMapping("/plateaus")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    fun handleRequest(@RequestBody plateauInputDto: PlateauInputDto): ResponseEntity<PlateauOutputDto> {
        val plateauId = UUID.randomUUID()
        commandGateway.sendAndWait<Any>(CreatePlateauCommand(plateauId, plateauInputDto.x, plateauInputDto.y))
        return ResponseEntity.ok(PlateauOutputDto(plateauId, plateauInputDto.x, plateauInputDto.y))
    }
}
