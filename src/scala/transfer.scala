package zsh.history
import scala.collection.parallel.CollectionConverters._


object Transfer {
  import scala.sys.process._
  import better.files.File
  import Configuration.config

  class TransferError(val failures: List[(String, String)]) extends RuntimeException {
    override def toString() = (
      List("Error: couldn't download history from the following hosts: ") ++
      (for (fail <- failures) yield "   * " + fail._1) ++
      List("either remove the entries from .mergerc or set allowMissing to true",
           "to fix this error.")
    ).mkString("\n")
  }


  def downloadHistoryFiles(allowMissing: Switch = Switch.T): List[File] = {
    require(config.hosts.length > 0)
    config.createDir()

    val histFiles = config.hosts.par.map({ host =>
      val (from, to) = (s"$host:${config.sourcePath}", config.getPathForHost(host))
      val hfile = File(to)

      // NOTE: side-effects!
      val returnCode = s"scp $from $to".!
      println(s"Transfer $from to $to")
      if (returnCode != 0 || !hfile.exists) {
        hfile.delete(swallowIOExceptions=true) // clear the empty or partial file if it exists
        Left((from, to))
      }
      else {
        Right(hfile)
      }
    })

    val (failures, successes) = histFiles.partition(_.isLeft)
    if (failures.nonEmpty && !allowMissing)
      throw new TransferError(failures.map({case Left(x) => x}).toList)

    successes.map(_.toOption.get).toList

  } ensuring { files =>
    if (allowMissing) {
      files.map(_.exists).reduce(_ && _)
    } else {
      (files.length == config.hosts.length) &&
      files.map(_.exists).reduce(_ && _)
    }
  }

  def uploadMergedHistory() = {
    config.hosts.par.map({ host =>
      val dest = config.getPathForHost("merged")
      println(s"Transfer ${dest} to $host:${config.sourcePath}")
      s"scp ${dest} $host:${config.sourcePath}".!
    })
   }
}
