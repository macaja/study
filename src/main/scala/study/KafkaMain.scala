package study

import monix.execution.Scheduler
import study.configuration.logging.Logger
import study.configuration.{ConfigApp, DefaultConfig}
import study.monixKafka.Environment
import study.monixKafka.consumer.MonixKafkaConsumer
import study.monixKafka.producer.Publisher
import study.protobuf.phone.PhoneData

import scala.concurrent.duration._

object KafkaMain extends App with Logger{

  implicit val scheduler = Scheduler.global

  implicit val environment = new Environment {
    override val config: ConfigApp = DefaultConfig
  }

  val event: Event = Event(
    environment.config.kafkaProducerTopics.exampleTopic,
    PhoneData(
      Some("99999999"),
      Some("Pepe9")
    )
  )

  Publisher[Array[Byte]](environment.config.kafkaProducerConfig.producerConf).publish(event).runAsync

  def consume = {
    MonixKafkaConsumer.consume[PhoneData](environment.config.kafkaConsumerTopics.exampleTopic)
      .map{
        msg =>
          val (_,eve) = msg
          println(s"El evento consumido es: $eve")
      }
      .bufferTimedWithPressure(1.seconds, 20)
      .onErrorRestart(3L)
      .subscribe()
  }
  consume
}
