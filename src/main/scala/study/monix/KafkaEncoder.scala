/*
 *  rewards-members-core
 *  Copyright © Starbucks 2017
 */

package study.monix

import study.EventMessage

import scala.util.Try

object KafkaEncoder {

  def serialize[T <: EventMessage](data: T): Array[Byte] = data.toByteArray

  def deserialize[T <: KafkaMessage[T]](bytes: Array[Byte])(
      implicit companion: KafkaEncoder[T]
  ): Try[T] = companion.validate(bytes)
}
