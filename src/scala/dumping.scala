package zsh.history

import scala.util.Using


object Dumping {
  import better.files._
  import Parsing.ResultData
  import Configuration.config

  def renderLine(ts: Long, rt: Int, cmd: String) =
    s": $ts:$rt;$cmd\n"

  def dumpResultsAsString(res: ResultData): String = {
    val out = new java.io.ByteArrayOutputStream()
    dumpResults(res, out)
    out.toString("UTF-8")
  }

  def dumpResultsToStdOut(res: ResultData): Unit =
    dumpResults(res)

  def dumpResultsToFile(res: ResultData): Unit =
    dumpResultsToFile(res, config.fmtDest("merged"))

  def dumpResultsToFile(res: ResultData, path: String): Unit = {
    println(s"Dumping parsed data to ${path}")
    dumpResults(res, File(path).newOutputStream)
  }

  def dumpResults(res: ResultData, out: java.io.OutputStream = System.out): Unit = {
    Using.resource(out) { out =>
      for( (ts, elapsed, command) <- res ) {
        // val date = new java.util.Date(ts*1000)
        // System.err.write((date.toString+"\n").getBytes)
        out.write(renderLine(ts, elapsed, command).getBytes("UTF-8"))
      }
    }
  }
}
