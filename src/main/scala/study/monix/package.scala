package study

import com.trueaccord.scalapb.{GeneratedMessage, GeneratedMessageCompanion, Message}
import sun.plugin2.message.EventMessage

package object monix {
  type KafkaMessage[T <: EventMessage]    = EventMessage with GeneratedMessage with Message[T]
  type KafkaEncoder[E <: KafkaMessage[E]] = GeneratedMessageCompanion[E]
}
