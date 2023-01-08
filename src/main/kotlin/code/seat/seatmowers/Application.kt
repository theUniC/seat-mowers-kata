package code.seat.seatmowers

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableTransactionManagement
@OpenAPIDefinition(info = Info(title = "Seat Mowers Open API", version = "1.0"))
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
