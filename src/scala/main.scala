package zsh.history


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
    val parsedFiles: List[ResultData] =
      for (dest <- files) yield {
        println(s"Parsing ${dest}...")
        val inputString = Unmetafy.unmetafy(dest)
        val Right(parsed) = Parsing.parseHistory(inputString): @unchecked
        parsed
      }

    distinct(removeDuplicates(List.concat(parsedFiles: _*)))
  }
}


object Main extends App {
  try {
    println("Downloading..."); Transfer.downloadHistoryFiles()
    println("Processing...");  Dumping.dumpResultsToFile(Transform.processHistoryFiles())
    println("Uploading...");   Transfer.uploadMergedHistory()
  } catch {
    case ex: Transfer.TransferError =>
      println()
      println(ex)
      println()
  }
}
