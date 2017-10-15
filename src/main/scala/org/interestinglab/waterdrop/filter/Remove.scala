package org.interestinglab.waterdrop.filter

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.streaming.StreamingContext
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

class Remove(var conf: Config) extends BaseFilter(conf) {

  def this() = {
    this(ConfigFactory.empty())
  }

  override def checkConfig(): (Boolean, String) = (true, "")

  override def prepare(spark: SparkSession, ssc: StreamingContext): Unit = {
    super.prepare(spark, ssc)
    val defaultConfig = ConfigFactory.parseMap(
      Map(
        "source_field" -> List("raw_message")
      )
    )
    conf = conf.withFallback(defaultConfig)
  }

  override def process(spark: SparkSession, df: DataFrame): DataFrame = {
    df.drop(conf.getStringList("source_field").asScala : _*)
  }
}