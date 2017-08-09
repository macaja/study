package study.configuration.logging

import org.slf4j.LoggerFactory

trait Logger {
  @volatile protected lazy val logger: org.slf4j.Logger =
    LoggerFactory.getLogger(getClass.getName.stripSuffix("$"))
}
