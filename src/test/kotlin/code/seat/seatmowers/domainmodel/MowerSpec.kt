package code.seat.seatmowers.domainmodel

import code.seat.seatmowers.application.command.CreatePlateauCommand
import code.seat.seatmowers.application.command.DeployMowerCommand
import code.seat.seatmowers.application.command.MoveMowerCommand
import code.seat.seatmowers.domainmodel.mower.Direction
import code.seat.seatmowers.domainmodel.mower.MowerWasDeployed
import code.seat.seatmowers.domainmodel.mower.MowerWasMoved
import code.seat.seatmowers.domainmodel.mower.PositionIsAlreadyOccupied
import code.seat.seatmowers.domainmodel.plateau.Coordinates
import code.seat.seatmowers.domainmodel.plateau.Plateau
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.jupiter.api.Assertions
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.util.UUID

private const val VALID_COORDINATE_COMPONENT = 15

object MowerSpec : Spek({
    describe("Mower entity") {
        val fixture: FixtureConfiguration<Plateau> by memoized { AggregateTestFixture(Plateau::class.java) }

        describe("Mower deployment") {
            val plateauId = UUID.randomUUID()

            it("Throws an exception when mower X component is outside plateau bounds") {
                fixture.setReportIllegalStateChange(false)
                fixture
                    .givenCommands(CreatePlateauCommand(plateauId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT))
                    .`when`(DeployMowerCommand(plateauId, UUID.randomUUID(), VALID_COORDINATE_COMPONENT + 1, VALID_COORDINATE_COMPONENT + 1, "N"))
                    .expectException(IllegalStateException::class.java)
            }

            it("Throws an exception when mower Y component is outside plateau bounds") {
                fixture.setReportIllegalStateChange(false)
                fixture
                    .givenCommands(CreatePlateauCommand(plateauId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT))
                    .`when`(DeployMowerCommand(plateauId, UUID.randomUUID(), VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT + 1, "N"))
                    .expectException(IllegalStateException::class.java)
            }

            it("Throws an exception when mower is given an invalid direction") {
                fixture.setReportIllegalStateChange(false)
                fixture
                    .givenCommands(CreatePlateauCommand(plateauId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT))
                    .`when`(DeployMowerCommand(plateauId, UUID.randomUUID(), VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT + 1, "A"))
                    .expectException(IllegalStateException::class.java)
            }

            it("Throws an exception when mower is deployed to an already occupied position") {
                fixture.setReportIllegalStateChange(false)
                val roverId = UUID.randomUUID()
                fixture
                    .givenCommands(CreatePlateauCommand(plateauId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT))
                    .andGivenCommands(DeployMowerCommand(plateauId, roverId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT, "N"))
                    .`when`(DeployMowerCommand(plateauId, roverId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT, "N"))
                    .expectException(PositionIsAlreadyOccupied::class.java)
            }

            it("Deploys mower successfully") {
                val roverId = UUID.randomUUID()
                fixture.setReportIllegalStateChange(false)
                fixture
                    .givenCommands(CreatePlateauCommand(plateauId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT))
                    .`when`(DeployMowerCommand(plateauId, roverId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT, "N"))
                    .expectSuccessfulHandlerExecution()
                    .expectEvents(MowerWasDeployed(roverId, plateauId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT, "N"))
            }
        }

        describe("Mower movement") {
            val plateauId = UUID.randomUUID()
            val roverId = UUID.randomUUID()

            it("Throws an exception when rover movement is not recognized") {
                fixture.setReportIllegalStateChange(false)
                fixture
                    .givenCommands(CreatePlateauCommand(plateauId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT))
                    .andGivenCommands(DeployMowerCommand(plateauId, roverId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT, "N"))
                    .`when`(MoveMowerCommand(plateauId, roverId, "A"))
                    .expectException(IllegalStateException::class.java)
            }

            it("Throws an exception when rover is not recognized") {
                fixture.setReportIllegalStateChange(false)
                fixture
                    .givenCommands(CreatePlateauCommand(plateauId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT))
                    .andGivenCommands(DeployMowerCommand(plateauId, roverId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT, "N"))
                    .`when`(MoveMowerCommand(plateauId, UUID.randomUUID(), "M"))
                    .expectException(IllegalArgumentException::class.java)
            }

            it("Throws an exception when rover is moved out of plateau bounds") {
                fixture.setReportIllegalStateChange(false)
                fixture
                    .givenCommands(CreatePlateauCommand(plateauId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT))
                    .andGivenCommands(DeployMowerCommand(plateauId, roverId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT, "N"))
                    .`when`(MoveMowerCommand(plateauId, roverId, "M"))
                    .expectException(IllegalStateException::class.java)
            }

            it("Throws an exception when rover new position is already occupied") {
                val secondRoverId = UUID.randomUUID()
                fixture.setReportIllegalStateChange(false)
                fixture
                    .givenCommands(CreatePlateauCommand(plateauId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT))
                    .andGivenCommands(DeployMowerCommand(plateauId, roverId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT, "N"))
                    .andGivenCommands(DeployMowerCommand(plateauId, secondRoverId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT - 1, "N"))
                    .`when`(MoveMowerCommand(plateauId, secondRoverId, "M"))
                    .expectException(PositionIsAlreadyOccupied::class.java)
            }

            it("is moved forward successfully") {
                fixture.setReportIllegalStateChange(false)
                fixture
                    .givenCommands(CreatePlateauCommand(plateauId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT))
                    .andGivenCommands(DeployMowerCommand(plateauId, roverId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT - 1, "N"))
                    .`when`(MoveMowerCommand(plateauId, roverId, "M"))
                    .expectSuccessfulHandlerExecution()
                    .expectEvents(MowerWasMoved(plateauId, roverId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT, "NORTH"))
                    .expectState { p -> Assertions.assertEquals(Coordinates(VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT), p.mowers().first().position().coordinates) }
            }

            it("is turned left successfully") {
                fixture.setReportIllegalStateChange(false)
                fixture
                    .givenCommands(CreatePlateauCommand(plateauId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT))
                    .andGivenCommands(DeployMowerCommand(plateauId, roverId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT - 1, "N"))
                    .`when`(MoveMowerCommand(plateauId, roverId, "L"))
                    .expectSuccessfulHandlerExecution()
                    .expectEvents(MowerWasMoved(plateauId, roverId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT - 1, "WEST"))
                    .expectState { p -> Assertions.assertEquals(Direction.NORTH.left(), p.mowers().first().position().direction) }
            }

            it("is turned right successfully") {
                fixture.setReportIllegalStateChange(false)
                fixture
                    .givenCommands(CreatePlateauCommand(plateauId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT))
                    .andGivenCommands(DeployMowerCommand(plateauId, roverId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT - 1, "N"))
                    .`when`(MoveMowerCommand(plateauId, roverId, "R"))
                    .expectSuccessfulHandlerExecution()
                    .expectEvents(MowerWasMoved(plateauId, roverId, VALID_COORDINATE_COMPONENT, VALID_COORDINATE_COMPONENT - 1, "EAST"))
                    .expectState { p -> Assertions.assertEquals(Direction.NORTH.right(), p.mowers().first().position().direction) }
            }
        }
    }
})
