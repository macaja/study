package study

import monix.execution.Scheduler
import study.configuration.adapters.{MonixKafkaConsumer, Publisher}
import study.configuration.logging.Logger
import study.configuration.{ConfigApp, DefaultConfig}
import study.monixKafka.Environment
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
      Some("555555"),
      Some("Bety5")
    )
  )
  Publisher[Array[Byte]](environment.config.kafkaProducerConfig.producerConf).publish(event).runAsync

  /*def consume = {
    MonixKafkaConsumer.consume[PhoneData](environment.config.kafkaConsumerGroups.testConsumer.group,environment.config.kafkaConsumerTopics.exampleTopic)
      .dump("THE CONSUMED EVENT")
      .bufferTimedWithPressure(1.seconds, 20)
      .onErrorRestart(3L)
      .subscribe()
  }
  consume*/
}
