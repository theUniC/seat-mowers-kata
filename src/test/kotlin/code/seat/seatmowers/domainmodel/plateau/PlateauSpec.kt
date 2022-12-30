package code.seat.seatmowers.domainmodel.plateau

import code.seat.seatmowers.application.command.CreatePlateau
import code.seat.seatmowers.application.command.DeployRover
import org.axonframework.messaging.annotation.MessageHandlerInvocationException
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.util.UUID

private const val INVALID_COORDINATE_COMPONENT = 0
private const val VALID_COORDINATE_COMPONENT = 15

object PlateauSpec : Spek({
    describe("Plateau aggregate") {
        val fixture: FixtureConfiguration<Plateau> by memoized { AggregateTestFixture(Plateau::class.java) }

        describe("Creation") {
            it("throws an exception when X coordinate is not valid") {
                fixture
                    .givenNoPriorActivity()
                    .`when`(CreatePlateau(UUID.randomUUID(), INVALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT))
                    .expectException(MessageHandlerInvocationException::class.java)
            }

            it("throws an exception when Y coordinate is not valid") {
                fixture
                    .givenNoPriorActivity()
                    .`when`(CreatePlateau(UUID.randomUUID(), VALID_COORDINATE_COMPONENT, INVALID_COORDINATE_COMPONENT))
                    .expectException(MessageHandlerInvocationException::class.java)
            }

            it("Creates a new plateau successfully") {
                val id = UUID.randomUUID()
                fixture
                    .givenNoPriorActivity()
                    .`when`(CreatePlateau(id, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT))
                    .expectSuccessfulHandlerExecution()
                    .expectEvents(NewPlateauWasCreated(id, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT))
            }
        }

        describe("Mower creation") {
            val plateauId = UUID.randomUUID()

            it("Throws an exception when mower X component is outside plateau bounds") {
                fixture.setReportIllegalStateChange(false)
                fixture
                    .givenCommands(CreatePlateau(plateauId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT))
                    .`when`(DeployRover(plateauId, UUID.randomUUID(), VALID_COORDINATE_COMPONENT + 1, VALID_COORDINATE_COMPONENT + 1, "NORTH"))
                    .expectException(MessageHandlerInvocationException::class.java)
            }

            it("Throws an exception when mower Y component is outside plateau bounds") {
                fixture.setReportIllegalStateChange(false)
                fixture
                    .givenCommands(CreatePlateau(plateauId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT))
                    .`when`(DeployRover(plateauId, UUID.randomUUID(), VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT + 1, "NORTH"))
                    .expectException(MessageHandlerInvocationException::class.java)
            }

            it("Deploys mower successfully") {
                val roverId = UUID.randomUUID()
                fixture.setReportIllegalStateChange(false)
                fixture
                    .givenCommands(CreatePlateau(plateauId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT))
                    .`when`(DeployRover(plateauId, roverId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT, "NORTH"))
                    .expectSuccessfulHandlerExecution()
                    .expectEvents(RoverWasDeployed(roverId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT, "NORTH"))
            }
        }
    }
})
