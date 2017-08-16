package study.configuration

import com.trueaccord.scalapb.{GeneratedMessage, GeneratedMessageCompanion, Message}
import monix.kafka.KafkaProducerConfig
import study.EventMessage

import scala.util.Try

package object kafka {

  type KafkaMessage[T <: EventMessage]    = EventMessage with GeneratedMessage with Message[T]
  type KafkaEncoder[E <: KafkaMessage[E]] = GeneratedMessageCompanion[E]

  case class KafkaConsumerTopics(exampleTopic: String)
  case class KafkaProducerTopics(exampleTopic: String)
  case class KafkaProducerDefaultConfig(producerConf: KafkaProducerConfig)


  case class TestConsumer(group:String)
  case class MembershipConsumer(group:String)
  case class KafkaConsumerGroups(testConsumer: TestConsumer,membershipConsumer:MembershipConsumer)


  object KafkaEncoder {

    def serialize[T <: EventMessage](data: T): Array[Byte] = data.toByteArray

    def deserialize[T <: KafkaMessage[T]](bytes: Array[Byte])(
      implicit companion: KafkaEncoder[T]
    ): Try[T] = companion.validate(bytes)
  }


}
