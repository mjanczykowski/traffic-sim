package miss.trafficsimulation.roads

import miss.trafficsimulation.roads.RoadDirection.{EW, NS}
import miss.trafficsimulation.traffic.MoveDirection.{GoStraight, Turn, SwitchLaneLeft, SwitchLaneRight}
import miss.trafficsimulation.traffic.{Car, Move, VehicleId}
import miss.trafficsimulation.util._
import org.specs2.mutable.Specification

//TODO: Make moves from middle field
class RoadSegmentSpecification extends Specification {
  "RoadSegment" should {
    "iterate through cars in proper way" in {
      val roadSegment = new RoadSegment(RoadId(1), 3, 7, None, null, NS)
      roadSegment.lanes(0).cells(6).vehicle = Some(Car(VehicleId("1"), 2, 1, Yellow))
      roadSegment.lanes(1).cells(5).vehicle = Some(Car(VehicleId("2"), 2, 1, Magenta))
      roadSegment.lanes(2).cells(5).vehicle = Some(Car(VehicleId("3"), 2, 1, Cyan))
      roadSegment.lanes(0).cells(3).vehicle = Some(Car(VehicleId("4"), 2, 1, Red))
      roadSegment.lanes(2).cells(3).vehicle = Some(Car(VehicleId("5"), 2, 1, Green))
      roadSegment.lanes(1).cells(2).vehicle = Some(Car(VehicleId("6"), 2, 1, Blue))
      roadSegment.lanes(2).cells(0).vehicle = Some(Car(VehicleId("7"), 2, 1, Black))

      val actual = roadSegment.vehicleIterator().toList.map(
        (vac: VehicleAndCoordinates) => vac.vehicle.color)
      val expected = List(Yellow, Cyan, Magenta, Green, Red, Blue, Black)

      actual mustEqual expected
    }
    "correctly calculate possible moves (first half of road)" in {
      val roadSegment = new RoadSegment(RoadId(1), 3, 10, None, null, NS)
      val car1 = Car(VehicleId("1"), 10, 1, Black, 5, 0)
      val car2 = Car(VehicleId("2"), 10, 1, White, 5, 0)
      val car3 = Car(VehicleId("3"), 10, 1, Yellow, 5, 0)
      roadSegment.lanes(0).cells(2).vehicle = Some(car1)
      roadSegment.lanes(1).cells(2).vehicle = Some(car2)
      roadSegment.lanes(2).cells(2).vehicle = Some(car3)
      roadSegment.lanes(0).cells(5).vehicle = Some(Car(VehicleId("4"), 10, 1, Red, 5, 0))
      roadSegment.lanes(1).cells(9).vehicle = Some(Car(VehicleId("5"), 10, 1, Green, 5, 0))
      roadSegment.lanes(2).cells(5).vehicle = Some(Car(VehicleId("6"), 10, 1, Blue, 5, 0))
      val actual1 = roadSegment.calculatePossibleMoves(VehicleAndCoordinates(car1, 0, 2))
      val actual2 = roadSegment.calculatePossibleMoves(VehicleAndCoordinates(car2, 1, 2))
      val actual3 = roadSegment.calculatePossibleMoves(VehicleAndCoordinates(car3, 2, 2))
      actual1 must contain(
        Move(GoStraight, 0, 2),
        Move(SwitchLaneRight, 1, 5)
      )
      actual2 must contain(
        Move(SwitchLaneLeft, 0, 2),
        Move(GoStraight, 1, 5),
        Move(SwitchLaneRight, 2, 2)
      )
      actual3 must contain(
        Move(SwitchLaneLeft, 1, 5),
        Move(GoStraight, 2, 2)
      )
    }
    "correctly calculate possible moves (second half of road)" in {
      val intersection = new Intersection()
      val horizontalRoadSegmentIn = new RoadSegment(RoadId(1), 3, 10, None, intersection, EW)
      val horizontalRoadSegmentOut = new RoadSegment(RoadId(1), 3, 10, Some(intersection), null, EW)
      val verticalRoadSegmentIn = new RoadSegment(RoadId(2), 3, 10, None, intersection, NS)
      val verticalRoadSegmentOut = new RoadSegment(RoadId(2), 3, 10, Some(intersection), null, NS)
      intersection.horizontalRoadIn = horizontalRoadSegmentIn
      intersection.horizontalRoadOut = horizontalRoadSegmentOut
      intersection.verticalRoadIn = verticalRoadSegmentIn
      intersection.verticalRoadOut = verticalRoadSegmentOut
      val car = Car(VehicleId("1"), 10, 1, Black, 5, 0)
      horizontalRoadSegmentIn.lanes(0).cells(7).vehicle = Some(car)
      horizontalRoadSegmentOut.lanes(0).cells(2).vehicle = Some(Car(VehicleId("2"), 10, 1, Red, 5, 0))
      verticalRoadSegmentOut.lanes(1).cells(2).vehicle = Some(Car(VehicleId("3"), 10, 1, Blue, 5, 0))
      verticalRoadSegmentOut.lanes(2).cells(0).vehicle = Some(Car(VehicleId("4"), 10, 1, Green, 5, 0))
      val listOfPossibleMoves = horizontalRoadSegmentIn.calculatePossibleMoves(VehicleAndCoordinates(
        car, 0, 7))
      listOfPossibleMoves must contain(
        Move(GoStraight, 0, 4),
        Move(Turn, 1, 4),
        Move(Turn, 0, 5)
      )
    }
  }
}
