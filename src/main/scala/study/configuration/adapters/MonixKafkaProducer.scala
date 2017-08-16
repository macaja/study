package study.configuration.adapters

import monix.eval.Task
import monix.execution.Scheduler
import monix.kafka.{KafkaProducer, KafkaProducerConfig, Serializer => MonixSerializer}
import org.apache.kafka.clients.producer.{ProducerRecord, RecordMetadata}
import study.Event
import study.configuration.kafka.KafkaEncoder
import study.configuration.logging.Logger
import study.configuration.logging.Syntax._

import scala.concurrent.duration.{FiniteDuration, _}
import scala.util.control.NoStackTrace

class Publisher[K: MonixSerializer](cfg: KafkaProducerConfig, timeout: FiniteDuration = 30.seconds) extends Logger {
  type V = Array[Byte]

  val kafkaProducer: KafkaProducer[String, Array[Byte]] =
    KafkaProducer[String, Array[Byte]](cfg, Scheduler.io())

  def publish(event: Event): Task[RecordMetadata] = {
    val message = event match {
      case Event(t, msg, Some(key)) => new ProducerRecord[String, Array[Byte]](t, key, KafkaEncoder.serialize(msg))
      case Event(t, msg, None)      => new ProducerRecord[String, Array[Byte]](t, KafkaEncoder.serialize(msg))
    }
    kafkaProducer
      .send(message)
      .timeout(timeout)
      .flatMap(
        _.fold[Task[RecordMetadata]](Task.raiseError(ProducerException("Unable to publish message")))(Task.now)
      )
      .onErrorRecoverWith {
        case ex =>
          logger.error("Failed to publish kafka message", ex)
          logger.errorWithMdc(
            s"Failed to publish kafka message ${ex.getMessage}",
            Map(
              "reqResp"           -> "KAFKA_PUBLISH_END",
              "kafkaTopic"        -> message.topic,
              "kafkaMessageBody"  -> message.toString,
              "kafkaMessageClass" -> message.getClass.toString
            )
          )
          Task.raiseError(ex)
      }
  }
  def close(): Task[Unit] = kafkaProducer.close()
}
object Publisher {
  def apply[K: MonixSerializer](cfg: KafkaProducerConfig): Publisher[K] = new Publisher[K](cfg)
}
case class ProducerException(message: String) extends RuntimeException(message) with NoStackTrace