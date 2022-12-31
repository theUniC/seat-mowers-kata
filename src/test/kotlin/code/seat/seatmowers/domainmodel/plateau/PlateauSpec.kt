package code.seat.seatmowers.domainmodel.plateau

import code.seat.seatmowers.application.command.CreatePlateau
import code.seat.seatmowers.application.command.DeployRover
import code.seat.seatmowers.application.command.MoveRover
import org.axonframework.messaging.annotation.MessageHandlerInvocationException
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.jupiter.api.Assertions
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
                    .expectException(IllegalStateException::class.java)
            }

            it("throws an exception when Y coordinate is not valid") {
                fixture
                    .givenNoPriorActivity()
                    .`when`(CreatePlateau(UUID.randomUUID(), VALID_COORDINATE_COMPONENT, INVALID_COORDINATE_COMPONENT))
                    .expectException(IllegalStateException::class.java)
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

        describe("Mower deployment") {
            val plateauId = UUID.randomUUID()

            it("Throws an exception when mower X component is outside plateau bounds") {
                fixture.setReportIllegalStateChange(false)
                fixture
                    .givenCommands(CreatePlateau(plateauId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT))
                    .`when`(DeployRover(plateauId, UUID.randomUUID(), VALID_COORDINATE_COMPONENT + 1, VALID_COORDINATE_COMPONENT + 1, "N"))
                    .expectException(IllegalStateException::class.java)
            }

            it("Throws an exception when mower Y component is outside plateau bounds") {
                fixture.setReportIllegalStateChange(false)
                fixture
                    .givenCommands(CreatePlateau(plateauId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT))
                    .`when`(DeployRover(plateauId, UUID.randomUUID(), VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT + 1, "N"))
                    .expectException(IllegalStateException::class.java)
            }

            it("Throws an exception when mower is given an invalid direction") {
                fixture.setReportIllegalStateChange(false)
                fixture
                    .givenCommands(CreatePlateau(plateauId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT))
                    .`when`(DeployRover(plateauId, UUID.randomUUID(), VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT + 1, "A"))
                    .expectException(IllegalStateException::class.java)
            }

            it("Throws an exception when mower is deployed to an already occupied position") {
                fixture.setReportIllegalStateChange(false)
                val roverId = UUID.randomUUID()
                fixture
                    .givenCommands(CreatePlateau(plateauId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT))
                    .andGivenCommands(DeployRover(plateauId, roverId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT, "N"))
                    .`when`(DeployRover(plateauId, roverId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT, "N"))
                    .expectException(MessageHandlerInvocationException::class.java)
            }

            it("Deploys mower successfully") {
                val roverId = UUID.randomUUID()
                fixture.setReportIllegalStateChange(false)
                fixture
                    .givenCommands(CreatePlateau(plateauId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT))
                    .`when`(DeployRover(plateauId, roverId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT, "N"))
                    .expectSuccessfulHandlerExecution()
                    .expectEvents(RoverWasDeployed(roverId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT, "N"))
            }
        }

        describe("Mower movement") {
            val plateauId = UUID.randomUUID()
            val roverId = UUID.randomUUID()

            it("Throws an exception when rover movement is not recognized") {
                fixture.setReportIllegalStateChange(false)
                fixture
                    .givenCommands(CreatePlateau(plateauId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT))
                    .andGivenCommands(DeployRover(plateauId, roverId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT, "N"))
                    .`when`(MoveRover(plateauId, roverId, "A"))
                    .expectException(IllegalStateException::class.java)
            }

            it("Throws an exception when rover is not recognized") {
                fixture.setReportIllegalStateChange(false)
                fixture
                    .givenCommands(CreatePlateau(plateauId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT))
                    .andGivenCommands(DeployRover(plateauId, roverId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT, "N"))
                    .`when`(MoveRover(plateauId, UUID.randomUUID(), "M"))
                    .expectException(IllegalArgumentException::class.java)
            }

            it("Throws an exception when rover is moved out of plateau bounds") {
                fixture.setReportIllegalStateChange(false)
                fixture
                    .givenCommands(CreatePlateau(plateauId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT))
                    .andGivenCommands(DeployRover(plateauId, roverId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT, "N"))
                    .`when`(MoveRover(plateauId, roverId, "M"))
                    .expectException(MessageHandlerInvocationException::class.java)
            }

            it("Throws an exception when rover new position is already occupied") {
                val secondRoverId = UUID.randomUUID()
                fixture.setReportIllegalStateChange(false)
                fixture
                    .givenCommands(CreatePlateau(plateauId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT))
                    .andGivenCommands(DeployRover(plateauId, roverId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT, "N"))
                    .andGivenCommands(DeployRover(plateauId, secondRoverId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT - 1, "N"))
                    .`when`(MoveRover(plateauId, secondRoverId, "M"))
                    .expectException(MessageHandlerInvocationException::class.java)
            }

            it("is moved forward successfully") {
                fixture.setReportIllegalStateChange(false)
                fixture
                    .givenCommands(CreatePlateau(plateauId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT))
                    .andGivenCommands(DeployRover(plateauId, roverId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT - 1, "N"))
                    .`when`(MoveRover(plateauId, roverId, "M"))
                    .expectSuccessfulHandlerExecution()
                    .expectEvents(RoverWasMoved(plateauId, roverId, "M"))
                    .expectState { p -> Assertions.assertEquals(Coordinates(VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT), p.rovers().first().position().coordinates) }
            }

            it("is turned left successfully") {
                fixture.setReportIllegalStateChange(false)
                fixture
                    .givenCommands(CreatePlateau(plateauId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT))
                    .andGivenCommands(DeployRover(plateauId, roverId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT - 1, "N"))
                    .`when`(MoveRover(plateauId, roverId, "L"))
                    .expectSuccessfulHandlerExecution()
                    .expectEvents(RoverWasMoved(plateauId, roverId, "L"))
                    .expectState { p -> Assertions.assertEquals(Direction.NORTH.left(), p.rovers().first().position().direction) }
            }

            it("is turned right successfully") {
                fixture.setReportIllegalStateChange(false)
                fixture
                    .givenCommands(CreatePlateau(plateauId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT))
                    .andGivenCommands(DeployRover(plateauId, roverId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT - 1, "N"))
                    .`when`(MoveRover(plateauId, roverId, "R"))
                    .expectSuccessfulHandlerExecution()
                    .expectEvents(RoverWasMoved(plateauId, roverId, "R"))
                    .expectState { p -> Assertions.assertEquals(Direction.NORTH.right(), p.rovers().first().position().direction) }
            }
        }
    }
})
