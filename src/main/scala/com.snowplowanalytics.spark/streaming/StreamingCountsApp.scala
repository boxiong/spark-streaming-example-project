/*
 * Copyright (c) 2015 Snowplow Analytics Ltd. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package com.snowplowanalytics.spark.streaming

// Java
import java.io.File
import java.io.FileReader
import java.util.Properties

// AWS libs
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.InitialPositionInStream
import com.amazonaws.services.kinesis.connectors.KinesisConnectorConfiguration

// Config
import com.typesafe.config.{Config, ConfigFactory}

// Spark
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Minutes, Seconds, Milliseconds}

/**
 * The entry point class for the Spark Streaming Application.
 *
 * Usage:
 *
 * spark/bin/spark-submit --class com.snowplowanalytics.spark.streaming.StreamingCountsApp \
 *                        --master local[2] \
 *                        spark-streaming-example-project/target/scala-2.10/spark-streaming-example-project-0.1.0.jar \
 *                        --config spark-streaming-example-project/src/main/resources/config.hocon.sample
*/
object StreamingCountsApp {

  def main(args: Array[String]) {

    // create Spark Streaming Config from hocon file in resource directory
    val scc = StreamingCountsConfig(
      region = "us-east-1",
      streamName = "my-stream",

      checkpointInterval = Minutes(10),
      initialPosition = InitialPositionInStream.LATEST,
      storageLevel = StorageLevel.MEMORY_AND_DISK_2,
      appName = "StreamingCountsApp",
      master = "local[2]",
      batchInterval =  Milliseconds(2000),

      tableName = "my-table",
      awsProfile = "default"
    )

    // start StreamingCounts application with config object
    StreamingCounts.execute(scc)
  }
}
