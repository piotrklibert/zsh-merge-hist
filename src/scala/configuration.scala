package zsh.history

import com.fasterxml.jackson.databind._
import com.fasterxml.jackson.module.scala.DefaultScalaModule


case class Configuration(
  val hosts: List[String],
  val tempDir: String = "./tmp/",
  val sourcePath: String = "~/.zsh_history",
) {
  import better.files.File

  def getPathForHost(host: String) =
    s"${tempDir}/${host}_hist"

  def connectionData(host: String) = {
    val dir = File(tempDir)
    createDownloadDirectory()
    (s"$host:${sourcePath}", getPathForHost(host))
  }

  def createDownloadDirectory() = {
    val dir = File(tempDir)
    if (!dir.exists) dir.createDirectory()
    dir
  }
}


/** Some context for the Configuration object: there is a possibility that this
  * program will be running as a daemon started from systemd unit file. It would
  * be like this so that we don't pay the initialization cost every time we want
  * to perform the merge. The code below is written with that possibility kept
  * in mind: in the case of a long-running process it would be inconvenient to
  * have to restart the process when the config changes.
  *
  * Currently, though, it serves absolutely no purpose at all, other than maybe
  * showcasing Scala's support for mutable variables...
  * */
object Configuration {
  import better.files.File
  import org.joda.time.Instant

  val settingsFile = (File.home / ".mergerc")
  def lastModified =
    new Instant(settingsFile.lastModifiedTime.toEpochMilli)

  val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)

  def loadConfig =
    mapper.readValue(settingsFile.toJava, classOf[Configuration])

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
