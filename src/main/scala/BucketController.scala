import akka.actor.ActorRef

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import akka.http.scaladsl.server.Directives
import akka.pattern.ask
import akka.util.Timeout

/**
  * Created by yury on 16.09.17.
  */
class BucketController(buckAc: ActorRef) (implicit executionContext: ExecutionContext)
extends Directives with DefaultJsonFormats {
  implicit val timeout = Timeout(2.seconds)


  implicit val bucketFormat = jsonFormat1(Bucket)

  val buckets = List(Bucket("abcd".getBytes()), Bucket("second buck".getBytes()))
  BucketStorage.createFromList(buckets)

  val route =
    addBuckets ~
    getRoot ~
    getBucket ~
    getBuckets

  def addBuckets =
    path("buckets") {
      post {
        entity(as[List[Bucket]]) { request =>
          complete((buckAc ? request).mapTo[Bucket])
        }
      }
    }

  def getRoot =
    path("bucketsroot") {
      get {
        complete { (buckAc ? "root" ).mapTo[Bucket] }
      }
    }

  def getBucket =
    path("buckets" / Segment) { id =>
      get {
        complete( (buckAc ? id).mapTo[Bucket])
      }
    }

  def getBuckets =
    path("buckets") {
      parameters('ids) { ids =>
        get {
          complete( (buckAc ? ids).mapTo[List[Bucket]])
        }
      }
    }
}
