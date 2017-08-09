/*
 *  rewards-members-core
 *  Copyright © Starbucks 2017
 */

package study.configuration.logging

import org.slf4j.MDC

object Syntax {

  implicit class LoggerExtension(val logger: org.slf4j.Logger) extends AnyVal {
    def errorWithMdc(message: String, mdc: Map[String, String]): Unit =
      withMdc(mdc)(logger.error(message))
  }

  def withMdc[A](mdc: Map[String, String])(func: => A): A = {
    val oldContextMap = MDC.getCopyOfContextMap
    mdc.foreach { case (key, value) => MDC.put(key, value) }
    val result = func
    MDC.setContextMap(oldContextMap)
    result
  }
}
