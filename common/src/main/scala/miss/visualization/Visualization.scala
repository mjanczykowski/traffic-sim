package miss.visualization

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

import scala.swing._

trait Visualization extends SimpleSwingApplication {

  import VisualizationActor._

  var x: Int = 0
  var y: Int = 0

  val config = ConfigFactory.load("visualization.conf")
  val trafficSimulationConfig = config.getConfig("trafficsimulation")
  val areaConfig = trafficSimulationConfig.getConfig("area")
  val visConfig = trafficSimulationConfig.getConfig("visualization")

  val canvas = new Canvas
  val system = ActorSystem("Visualization", config)
  val actor = system.actorOf(VisualizationActor.props(canvas), "vizActor")

  override def startup(args: Array[String]): Unit = {
    super.startup(args)
    if (args.length == 2) {
      try {
        x = args(0).toInt
        y = args(1).toInt
      } catch {
        case e: NumberFormatException =>
          x = 0
          y = 0
      }
    }
    actor ! Init(x, y)
  }

  override def top: Frame = new MainFrame {
    title = s"Traffic Simulation Visualization: Area ($x, $y)"
    contents = canvas

    override def closeOperation(): Unit = {
      actor ! Exit(x, y)
      Thread.sleep(1000)
      system.terminate()
      super.closeOperation()
    }
  }
}

object VisualizationMain extends Visualization
