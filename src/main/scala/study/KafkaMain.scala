package study

import java.time.{Instant, ZoneOffset}

import com.google.protobuf.timestamp.Timestamp
import monix.execution.Scheduler
import study.configuration.adapters.Publisher
import study.configuration.logging.Logger
import study.configuration.{ConfigApp, DefaultConfig}
import study.monixKafka.Environment
import study.protobuf.member.{CardData, MemberCreated, MemberData, ProgramMembershipData}
import study.protobuf.metadata.ZonedTimestamp
import study.protobuf.northamerica.TierHistorySyndication
import study.protobuf.program.PointTypeConfig.DateAdjuster.FIRST_DAY_OF_NEXT_MONTH
import study.protobuf.program.PointTypeConfig.{DateAdjuster, Expiration}
import study.protobuf.program.{PointTypeConfig, ProgramConfig, RedemptionLevelConfig, TierConfig}
import study.protobuf.program.ProgramConfig.ProgramType.MSR
import study.protobuf.program.Status.ACTIVE

object KafkaMain extends App with Logger{

  implicit val scheduler = Scheduler.global

  implicit val environment = new Environment {
    override val config: ConfigApp = DefaultConfig
  }

  val memberCreated = MemberCreated(
    Some(ProgramMembershipData(
      Some("membership_id"),
      Some("member_id"),
      Some("program_id"),
      Some("program_name"),
      Some("member_number"),
      Some("membership_status")
    )),
    Some(MemberData(
      Some("member_id2"),
      Some("user_id"),
      Some("partner_number"),
      Some("first_name"),
      Some("last_name"),
      Some("birthMonth"),
      Some("birthDay"),
      Some("countryCode"),
      Some("2015-09-23T12:00:00.000Z"),
      Some("2015-09-23T12:00:00.000Z")
    )),
    Some(CardData(
      Some("card_id"),
      Some(ZonedTimestamp(
        Some(Timestamp(Instant.now.getEpochSecond)),
        Some(ZoneOffset.UTC.getId)
      )),
      Some(ZonedTimestamp(
        Some(Timestamp(Instant.now.getEpochSecond)),
        Some(ZoneOffset.UTC.getId)
      )),
      Some("1234567891234567"),
      Some(ZonedTimestamp(
        Some(Timestamp(Instant.now.getEpochSecond)),
        Some(ZoneOffset.UTC.getId)
      )),
      Some("card_class"),
      Some("promo_code"),
      Some("status")
    ))
  )

  val programConfig = ProgramConfig(
    id = Some("2114"),
    status = ACTIVE,
    `type` = MSR,
    name = Some("name"),
    geography = Some("US"),
    currency = Some("USD"),
    reevaluationPeriod = Some("P1Y"),
    startTime =Some(Timestamp(System.currentTimeMillis)),
    createTime = Some(Timestamp(System.currentTimeMillis)),
    updateTime = Some(Timestamp(System.currentTimeMillis)),
    pointTypes = List(PointTypeConfig(
      id = Some("12344"),
      status = ACTIVE,
      code = Some("454"),
      name = Some("name point type config"),
      expiration = Some(Expiration(period = Some("1"))),
      createTime = Some(Timestamp(System.currentTimeMillis)),
      updateTime = Some(Timestamp(System.currentTimeMillis))
    )),
    tiers = List(TierConfig(
      id= Some("123"),
      number = Some(1),
      status = ACTIVE,
      code = Some("1234"),
      name = Some("name of tier config"),
      externalName = Some("external name"),
      top = Some(true),
      default = None,
      pointTypes = Seq("1","3"),
      entryThreshold = None,
      exitThreshold = None,
      redemptionLevels = Seq(RedemptionLevelConfig(
        name = Some("name redemption level 1"),
        level = None,
        code = Some(1)
      )),
      createTime = Some(Timestamp(System.currentTimeMillis)),
      updateTime = Some(Timestamp(System.currentTimeMillis))
    ))
  )

  val tierHistorySyndication = TierHistorySyndication(
    membershipId = Some("5018787035907800"),
    previousTierProcessDate = Some(ZonedTimestamp(
      Some(Timestamp(Instant.now.getEpochSecond)),
      Some(ZoneOffset.UTC.getId)
    )),
    previousTierId = Some("1"),
    currentTierProcessDate = Some(ZonedTimestamp(
      Some(Timestamp(Instant.now.getEpochSecond)),
      Some(ZoneOffset.UTC.getId)
    )),
    currentTierId = Some("3"),
    activeFlag = Some("Af"),
    previousTierSequenceNumber = Some(1),
    createdDate = Some(ZonedTimestamp(
      Some(Timestamp(Instant.now.getEpochSecond)),
      Some(ZoneOffset.UTC.getId)
    )),
    updatedDate = Some(ZonedTimestamp(
      Some(Timestamp(Instant.now.getEpochSecond)),
      Some(ZoneOffset.UTC.getId)
    )),
    uniqueId = Some("222"),
    tierStartDate = Some(ZonedTimestamp(
      Some(Timestamp(Instant.now.getEpochSecond)),
      Some(ZoneOffset.UTC.getId)
    )),
    tierReevaluationDate = Some(ZonedTimestamp(
      Some(Timestamp(Instant.now.getEpochSecond)),
      Some(ZoneOffset.UTC.getId)
    )),
    currentTierPoints = Some(2),
    currentTierSequenceNumber = Some(3)
  )

  val eventMemberCreated: Event = Event(
    environment.config.kafkaProducerTopics.memberSyndication,
    memberCreated
  )

  val eventProgramConfig: Event = Event(
    environment.config.kafkaProducerTopics.programSyndication,
    programConfig
  )

  val eventTierHistory: Event = Event(
    environment.config.kafkaProducerTopics.tierHistorySyndication,
    tierHistorySyndication
  )
  Publisher[Array[Byte]](environment.config.kafkaProducerConfig.producerConf).publish(eventMemberCreated).runAsync
  //Publisher[Array[Byte]](environment.config.kafkaProducerConfig.producerConf).publish(eventProgramConfig).runAsync
  //Publisher[Array[Byte]](environment.config.kafkaProducerConfig.producerConf).publish(eventTierHistory).runAsync
  /*def consume = {
    MonixKafkaConsumer.consume[PhoneData](environment.config.kafkaConsumerGroups.testConsumer.group,environment.config.kafkaConsumerTopics.exampleTopic)
      .dump("THE CONSUMED EVENT")
      .bufferTimedWithPressure(1.seconds, 20)
      .onErrorRestart(3L)
      .subscribe()
  }
  consume*/
}
