
import com.trueaccord.scalapb.GeneratedMessage

trait EventMessage extends Product with Serializable { self: GeneratedMessage =>
  def toByteArray: Array[Byte]
}

case class Event(topic: String, event: EventMessage, key: Option[String] = None)


