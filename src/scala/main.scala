package zsh.history

import scalaz.Scalaz._ // for the |> operator


object Transfer {
  import scala.sys.process._
  import better.files._
  import Configuration.config

  def downloadHistFiles(): List[File] = {
    val dir = File(config.tempDir)
    if (!dir.exists) dir.createDirectory
    val histFiles = for (host <- config.hosts) yield {
      println(s"Transfer $host:${config.sourcePath} to ${config.tempDir}/${host}_hist")
      val (from, to) = (s"$host:${config.sourcePath}", config.fmtDest(host))
      (s"scp $from $to".!)
      File(to)
    }
    histFiles.toList
  } ensuring { files =>
    (files.length == config.hosts.length) && files.map(_.exists).fold(true)(_ && _)
  }

  def uploadMergedHist() = {
    for (host <- config.hosts) {
      val dest = config.fmtDest("merged")
      println(s"Transfer ${dest} to $host:${config.sourcePath}")
      s"scp ${dest} $host:${config.sourcePath}".!
    }
  }
}


object Transform {
  import better.files._
  import Parsing.ResultData
  import Configuration.config

  def removeDuplicates(lines: ResultData): ResultData =
    lines.sortBy(_._1).distinct // TODO: change to distinctBy(_._3) and fix the tests

  def processHistFiles(): ResultData = {
    val parsedFiles: List[ResultData] =
      for(host <- config.hosts) yield {
        val dest = config.fmtDest(host)
        println(s"Parsing ${dest}...")
        val inputString = Unmetafy.unmetafy(File(dest))
        val Right(parsed) = Parsing.parseHistory(inputString): @unchecked
        parsed
      }

    List.concat(parsedFiles: _*) |> removeDuplicates
  }
}


object Main extends App {
  Transfer.downloadHistFiles()
  Dumping.dumpResultsToFile(Transform.processHistFiles())
  Transfer.uploadMergedHist()
}
