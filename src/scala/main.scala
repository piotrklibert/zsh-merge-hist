package zsh.history

import scala.collection.parallel.CollectionConverters._


object Transform {
  import better.files.File
  import Parsing.ResultData
  import Configuration.config

  def removeDuplicates(lines: ResultData): ResultData =
    lines.sortBy(_._1).distinct // TODO: change to distinctBy(_._3) and fix the tests

  def distinct(lines: ResultData): ResultData = {
    lines.reverse.distinctBy(_._3).reverse
  }

  def processHistoryFiles(): ResultData =
    processHistoryPaths(config.hosts.map(config.getPathForHost))

  def processHistoryPaths(files: List[String]) =
    processHistoryFiles(files.map(File(_)))

  def processHistoryFiles(files: List[File]): ResultData = {
    val parsedFiles =
      files.par.map({ dest =>
        println(s"Parsing ${dest}...")
        val inputString = Unmetafy.unmetafy(dest)
        val Right(parsed) = Parsing.parseHistory(inputString): @unchecked
        parsed
      })

    distinct(removeDuplicates(List.concat(parsedFiles.toList: _*)))
  }
}


object Main extends App {
  try {
    println("Downloading...");
    val files = Transfer.downloadHistoryFiles()
    println("Processing...");
    Dumping.dumpResultsToFile(Transform.processHistoryFiles(files))
    println("Uploading...");
    Transfer.uploadMergedHistory()
    println("done");
  } catch {
    case ex: Transfer.TransferError =>
      println()
      println(ex)
      println()
  }
}
