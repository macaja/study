package study.configuration

import com.typesafe.config.Config
import monix.kafka.KafkaProducerConfig
import study.configuration.kafka.{KafkaConsumerTopics, KafkaProducerDefaultConfig, KafkaProducerTopics}

trait ConfigApp {
  def config: Config
  def kafkaConsumerTopics: KafkaConsumerTopics
  def kafkaProducerTopics: KafkaProducerTopics
  def kafkaProducerConfig: KafkaProducerDefaultConfig
}
