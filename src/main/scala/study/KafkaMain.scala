package study

import monix.execution.Scheduler
import study.configuration.logging.Logger
import study.configuration.{ConfigApp, DefaultConfig}
import study.monixKafka.Environment
import study.monixKafka.producer.Publisher
import study.protobuf.phone.PhoneData

object KafkaMain extends App with Logger{

  implicit val scheduler = Scheduler.global

  val environment = new Environment {
    override val config: ConfigApp = DefaultConfig
  }

  val event: Event = Event(
    environment.config.kafkaProducerTopics.exampleTopic,
    PhoneData(
      Some("3217840574"),
      Some("grey")
    )
  )
  Publisher[Array[Byte]](environment.config.kafkaProducerConfig.producerConf).publish(event).runAsync
}
