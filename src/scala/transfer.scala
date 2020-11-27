package zsh.history

import scala.collection.parallel.CollectionConverters._


object Transfer {
  import scala.sys.process._
  import better.files.File
  import Configuration.config

  type DownloadInfo = (String, File)
  type DownloadResult = Either[DownloadInfo, DownloadInfo]

  class TransferError(val failures: List[String])
      extends RuntimeException
  {
    override def toString() = (
      List("Error: couldn't download history from the following hosts: ") ++
      (for (fail <- failures) yield "   * " + fail) ++
      List("either remove the entries from .mergerc or set allowMissing to true",
           "to fix this error.")
    ).mkString("\n")
  }

  def downloadSingleFileFrom(host: String): DownloadResult = {
    val (from, path) = config.connectionData(host)
    val localHistFile = File(path)

    println(s"Transfer $from to $path")
    val returnCode = s"scp $from $path".!
    if (returnCode != 0 || !localHistFile.exists) {
      // clear the empty or partial file, don't throw if it doesn't exist at all
      localHistFile.delete(swallowIOExceptions=true)
      Left((from, localHistFile))
    }
    else {
      Right((from, localHistFile))
    }
  }

  def downloadHistoryFiles(allowMissing: Switch = Switch.T): List[File] = {
    require(config.hosts.length > 0)
    config.createDownloadDirectory()

    val downloads = config.hosts.par.map(downloadSingleFileFrom _).toList
    val (failures, successes) = downloads.partition(_.isLeft)
    if (!allowMissing && failures.nonEmpty) {
      throw new TransferError(failures map unwrapFailure)
    }
    successes.map(_.toOption.map(_._2).get)
  }
  .ensuring {
    files =>
      val onlySuccessfulDownloads = files.map(_.exists).reduce(_ && _)
      if (allowMissing)
        onlySuccessfulDownloads
      else
        onlySuccessfulDownloads && (files.length == config.hosts.length)
  }

  private def unwrapFailure(fail: DownloadResult): String = fail match {
    case Left((host, _)) => host
    case _ => throw new Exception("Not a failed download data")
  }

  def uploadMergedHistory() = {
    config.hosts.par.map({ host =>
      val dest = config.getPathForHost("merged")
      println(s"Transfer ${dest} to $host:${config.sourcePath}")
      s"scp ${dest} $host:${config.sourcePath}".!
    })
   }
}
