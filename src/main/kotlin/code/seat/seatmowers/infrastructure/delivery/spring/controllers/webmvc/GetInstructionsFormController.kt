package code.seat.seatmowers.infrastructure.delivery.spring.controllers.webmvc

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class GetInstructionsFormController {
    @GetMapping("/instructions")
    fun handleRequest(): String = "instructions-form"
}
