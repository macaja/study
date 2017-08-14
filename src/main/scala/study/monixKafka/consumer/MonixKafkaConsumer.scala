package study.monixKafka.consumer

import monix.kafka.KafkaConsumerObservable
import monix.reactive.Observable
import study.configuration.ConfigApp
import study.configuration.kafka.{KafkaEncoder, KafkaMessage}
import study.monixKafka.Environment

object MonixKafkaConsumer {

  def consume[T <: KafkaMessage[T]](topic: String)(
                                     implicit companion: KafkaEncoder[T],
                                     environment: Environment
                                   ): Observable[(Long, T)] = {
    val value = KafkaConsumerObservable[Array[Byte], Array[Byte]](
      environment.config.kafkaConsumerConfig,
      List(topic)
    )
    value
      .map { record =>
        (record.offset, KafkaEncoder.deserialize[T](record.value))
      }
      .filter { case (_, entity) => entity.isSuccess }
      .map { case (offset, entity) => (offset, entity.get) }
  }

}
