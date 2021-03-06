package miss.trafficsimulation.traffic

import miss.trafficsimulation.util.Color

import scala.util.Random

case class Situation()

case class VehicleId(id: Long)

object VehicleIdGenerator {
  private var _idSequence = 0: Long

  def nextId: VehicleId = {
    val nextId = _idSequence
    _idSequence += 1
    VehicleId(nextId)
  }
}

trait Vehicle {
  def id: VehicleId

  def move(possibleMoves: List[Move]): Move

  def color: Color

  def timeFrame: Long

  def maxVelocity: Int

  def maxAcceleration: Int

  def currentVelocity: Int

  def currentAcceleration: Int
}

case class Car(id: VehicleId,
               maxVelocity: Int,
               maxAcceleration: Int,
               color: Color,
               var timeFrame: Long,
               var currentVelocity: Int = 0,
               var currentAcceleration: Int = 0)
  extends Vehicle {

  override def move(possibleMoves: List[Move]): Move = {
    currentAcceleration = Math.min(currentAcceleration + 1, maxAcceleration)

    val maximumPossibleVelocityInFrame =
      Math.min(maxVelocity, currentVelocity + currentAcceleration)

    val selectedMove = Random.shuffle(possibleMoves).head

    if (maximumPossibleVelocityInFrame > selectedMove.cellsCount) {
      currentAcceleration = 0
      currentVelocity = selectedMove.cellsCount
    } else {
      currentVelocity = maximumPossibleVelocityInFrame
    }

    timeFrame += 1

    selectedMove.copy(cellsCount = currentVelocity)
  }
}
