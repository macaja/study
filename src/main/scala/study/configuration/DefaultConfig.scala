package study.configuration

import cats.data.NonEmptyList
import cats.implicits.{catsSyntaxEither, catsSyntaxUCartesian}
import com.typesafe.config.ConfigFactory
import monix.kafka.KafkaProducerConfig
import pureconfig._
import pureconfig.error._
import study.configuration.kafka.{KafkaConsumerTopics, KafkaProducerTopics}
import study.configuration.logging.Logger

import scala.collection.immutable.Seq


object DefaultConfig extends ConfigApp with Logger{

  val config = ConfigFactory.load()
  val validateConfiguration =
    (
      loadConfig[KafkaConsumerTopics](config,"study.kafka.topic.consumer").toValidatedNel |@|
        loadConfig[KafkaProducerTopics](config, "study.kafka.topic.producer").toValidatedNel |@|
      loadConfig[KafkaProducerConfig](config, "study.kafka.producer.clients").toValidatedNel
    ).tupled

  val (kafkaConsumerTopics, kafkaProducerTopics, kafkaProducerConfig) = validateConfiguration.fold(throwException, identity)

  private[configuration] def throwException(failures: NonEmptyList[ConfigReaderFailures]): Nothing = {
    val msg: String = buildMessage(failures)
    logger.error(msg)
    throw new Exception(msg)
  }

  private[configuration] def buildMessage(nestedFailures: NonEmptyList[ConfigReaderFailures]): String = {
    val flatFailures: Seq[ConfigReaderFailure] = nestedFailures.map(_.toList).toList.flatten

    val errors: String = flatFailures.map {
      case CannotConvert(value, toTyp, because, _, _) => s"Att $value can't be set to $toTyp because $because"
      case CollidingKeys(key, existingValue, _)    => s"Att error: CollidingKeys $key - $existingValue"
      case KeyNotFound(key, _, _)                     => s"Att $key not found"
      case UnknownKey(key, _)                      => s"Att $key unknown"
      case WrongType(foundTyp, expectedTyp, _, _)     => s"Att has wrong type: found $foundTyp, expected $expectedTyp"
      case ThrowableFailure(throwable, _,_)          => s"Config throwable was thrown ${throwable.getMessage}"
      case EmptyStringFound(typ, _,_)                => s"Att empty string found $typ"
      case NoValidCoproductChoiceFound(value, _,_)   => s"Att No valid coproduct $value"
      case _                                       => ""
    } mkString ", "

    s"Errors while parsing config file: [$errors]"
  }
}