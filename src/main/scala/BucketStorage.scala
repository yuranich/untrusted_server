import scala.collection.mutable

/**
  * Created by yury on 16.09.17.
  */
object BucketStorage {
  val storage: mutable.Map[Long, Bucket] = mutable.HashMap[Long, Bucket]()

  val rootId = 0

  def getRoot: Option[Bucket] = {
    storage.get(rootId)
  }

  def createFromList(buckets: List[Bucket]): Unit = {
    var i: Long = -1
    for (buck <- buckets) {
      i = i + 1
      storage.put(i, buck)
    }
    //only for testing
    Bucket(s"added $i buckets".getBytes())
  }
}
