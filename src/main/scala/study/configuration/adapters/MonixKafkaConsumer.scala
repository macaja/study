package study.configuration.adapters

import monix.kafka.{KafkaConsumerConfig, KafkaConsumerObservable}
import monix.reactive.Observable
import study.configuration.kafka.{KafkaEncoder, KafkaMessage}
import study.configuration.logging.Logger
import study.monixKafka.Environment

object MonixKafkaConsumer extends Logger{

  def consume[T <: KafkaMessage[T]](group:String,topic: String)(
                                     implicit companion: KafkaEncoder[T],
                                     environment: Environment
                                   ): Observable[(Long, T)] = {
    logger.info(s"El group es  => ${group}")
    val value: KafkaConsumerObservable[Array[Byte], Array[Byte]] = KafkaConsumerObservable[Array[Byte], Array[Byte]](
      environment.config.kafkaConsumerConfig.copy(groupId = group),
      List(topic)
    )
    value
      .map { record =>
        (record.offset, KafkaEncoder.deserialize[T](record.value))
      }
      .filter { case (_, entity) => entity.isSuccess }
      .map { case (offset, entity) => (offset, entity.get) }
      .onErrorRecoverWith{
        case ex =>
          logger.error("Failed to consume kafka message", ex)
          Observable.raiseError(ex)
     }

  }

}
