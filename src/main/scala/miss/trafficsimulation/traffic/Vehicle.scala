package miss.trafficsimulation.traffic

import miss.trafficsimulation.util.Color

case class Situation()

case class VehicleId(id: String)

trait Vehicle {
  def move(possibleMoves: List[Move]): Move
  def color: Color
}

case class Car(id: VehicleId,
               maxVelocity: Int,
               maxAcceleration: Int,
               color: Color,
               var currentVelocity: Int = 0,
               var currentAcceleration: Int = 0)
  extends Vehicle {
  override def move(possibleMoves: List[Move]): Move = ???
}
