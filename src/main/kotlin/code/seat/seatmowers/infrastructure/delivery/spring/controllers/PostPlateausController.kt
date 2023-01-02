package code.seat.seatmowers.infrastructure.delivery.spring.controllers

import code.seat.seatmowers.application.command.CreatePlateauCommand
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.PlateauInputDto
import code.seat.seatmowers.infrastructure.delivery.spring.dtos.PlateauOutputDto
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import javax.validation.Valid

@RestController
@Tag(name = "Plateau")
class PostPlateausController(val commandGateway: CommandGateway) {
    @PostMapping("/plateaus")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(
        ApiResponse(description = "The new plateau", responseCode = "200", content = [Content(mediaType = "application/json", schema = Schema(implementation = PlateauOutputDto::class))]),
        ApiResponse(description = "When the given plateau does not exist", responseCode = "404"),
        ApiResponse(description = "When provided data is not correct", responseCode = "400")
    )
    @ResponseBody
    fun handleRequest(@Valid @RequestBody plateauInputDto: PlateauInputDto): PlateauOutputDto {
        val plateauId = UUID.randomUUID()
        commandGateway.sendAndWait<Any>(CreatePlateauCommand(plateauId, plateauInputDto.x, plateauInputDto.y))
        return PlateauOutputDto(plateauId, plateauInputDto.x, plateauInputDto.y)
    }
}
