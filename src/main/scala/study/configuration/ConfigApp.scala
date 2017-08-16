package study.configuration

import com.typesafe.config.Config
import monix.kafka.KafkaConsumerConfig
import study.configuration.kafka.{KafkaConsumerGroups, KafkaConsumerTopics, KafkaProducerDefaultConfig, KafkaProducerTopics}

trait ConfigApp {
  def config: Config
  def kafkaConsumerTopics: KafkaConsumerTopics
  def kafkaProducerTopics: KafkaProducerTopics
  def kafkaConsumerConfig: KafkaConsumerConfig
  def kafkaConsumerConfig2: KafkaConsumerConfig
  def kafkaConsumerGroups: KafkaConsumerGroups
  def kafkaProducerConfig: KafkaProducerDefaultConfig
}
