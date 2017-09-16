import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server._
import Directives._

/**
  * Created by yury on 16.09.17.
  */
class ServerTest extends WordSpec with Matchers with ScalatestRouteTest with DefaultJsonFormats {

  val buckets = List(Bucket("abcd".getBytes()), Bucket("second buck".getBytes()))

  BucketStorage.createFromList(buckets)

  implicit val bucketFormat = jsonFormat1(Bucket)

  Server.main(Array[String]())

  "The service" should {

    "return a root bucket response for GET requests to /bucketsroot" in {
      // tests:
      Get("/bucketsroot") ~> Server.routes ~> check {
        responseAs[Bucket] shouldEqual buckets.head
      }
    }

    "return a bucket by ID" in {
      // tests:
      Get("/buckets/1") ~> Server.routes ~> check {
        responseAs[Bucket] shouldEqual buckets.tail.head
      }
    }
  }
}
