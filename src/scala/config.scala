package zsh.history

import com.fasterxml.jackson.databind._
import com.fasterxml.jackson.module.scala.DefaultScalaModule


case class Configuration(
  val hosts: List[String],
  val tempDir: String,
  val sourcePath: String,
) {
  def fmtDest(host: String) =
    s"${tempDir}/${host}_hist"
}

object Configuration {
  import better.files.File
  import org.joda.time.Instant

  val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)

  val settingsFile = (File.home / ".mergerc")
  def lastModified = new Instant(settingsFile.lastModifiedTime.toEpochMilli)
  def loadConfig = mapper.readValue(
    settingsFile.toJava, classOf[Configuration]
  )

  if (!settingsFile.exists)
    throw new RuntimeException("No configuration file found!")

  private var _config: Configuration = loadConfig
  private var _modtime: Instant = lastModified

  def config = {
    if (lastModified isAfter _modtime) {
      _config = loadConfig
      _modtime = lastModified
    }
    _config
  }
}
