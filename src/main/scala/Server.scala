import akka.http.scaladsl.Http
import akka.http.scaladsl.server.{HttpApp, Route, RouteConcatenation}
import akka.actor.{ActorSystem, Props}
import akka.stream.ActorMaterializer
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors

object Server extends App with RouteConcatenation {

  implicit val system = ActorSystem("bucket-system")
  sys.addShutdownHook(system.terminate())

  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val buckAc = system.actorOf(Props[BucketActor])

  val routes =
    cors()(new BucketController(buckAc).route)

  Http().bindAndHandle(routes, "0.0.0.0", 12345)
}