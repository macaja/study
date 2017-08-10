package study

import sbux.ucp.rew.infrastructure.adapters.kafka.producer.Publisher
import study.configuration.logging.Logger
import study.configuration.{ConfigApp, DefaultConfig}
import study.monix.Environment
import study.protobuf.phone.PhoneData

object KafkaMain extends App with Logger{

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
  Publisher(environment.config.kafkaProducerConfig).publish(event).runAsync
}
