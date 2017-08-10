package study.monix

import study.configuration.ConfigApp

trait Environment {
  val config:ConfigApp
}
