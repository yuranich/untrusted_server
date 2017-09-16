import akka.actor.{Actor, ActorLogging}

/**
  * Created by yury on 16.09.17.
  */
class BucketActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case "root" => sender ! BucketStorage.getRoot.getOrElse(Bucket("empty".getBytes()))

    case id: String => sender ! BucketStorage.storage.getOrElse(id.toLong, null)

    case ids: List[Long] => sender ! (for (id <- ids) yield BucketStorage.storage.getOrElse(id, Bucket("empty".getBytes())))

    case buckets: List[Bucket] => sender ! BucketStorage.createFromList(buckets)

    case _ => throw new IllegalArgumentException("Query request didn't match any patterns")
  }
}
