package zsh.history

import scala.collection.parallel.CollectionConverters._

object Transform {
    import Configuration.config
    import Parsing.ResultData
    import better.files.File

    def removeDuplicates(lines: ResultData): ResultData =
        lines.sortBy(_._1).distinct // TODO: change to distinctBy(_._3) and fix the tests

    def removeRepeatedCommands(lines: ResultData): ResultData =
        lines.reverse.distinctBy(_._3).reverse

    def processHistoryFiles(): ResultData =
        processHistoryPaths(config.hosts.map(config.getPathForHost))

    def processHistoryPaths(files: List[String]): ResultData =
        processHistoryFiles(files.map(File(_)))

    def processHistoryFiles(files: List[File]): ResultData = {
        val parsedFiles = files.par flatMap { historyFile =>
            println(s"Parsing ${historyFile}...")
            val inputString = Unmetafy.unmetafy(historyFile)
            val debugOutput = List(
                s"\n---------------${historyFile}----------------\n",
                inputString.takeRight(500).dropWhile(_ != '\n').drop(1)
            )
            println(debugOutput.mkString)
            val Right(parseResults) = Parsing.parseHistory(inputString): @unchecked
            parseResults.sortBy(_._1)
        }
        val sortedResults = parsedFiles.toList.sortBy(_._1)
        val noDuplicates = removeDuplicates(sortedResults)
        val noRepeats = removeRepeatedCommands(noDuplicates)
        noRepeats
    }
}


object Main extends App {
    try {
        println("Downloading...");
        val files = Transfer.downloadHistoryFiles()
        println("Processing...");
        val mergedHistory = Transform.processHistoryFiles(files)
        println("Dumping...");
        Dumping.dumpResultsToFile(mergedHistory)
        println("Uploading...");
        Transfer.uploadMergedHistory()
        println("All done.")
    } catch {
        case ex: Transfer.TransferError =>
            println()
            println(ex)
            println()
        case ex: Throwable =>
            println()
            println(ex)
            println()
            ex.printStackTrace()
            println()
    }
}
