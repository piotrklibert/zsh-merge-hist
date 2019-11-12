package zsh.history

import scala.util.Using
import scala.util.parsing.combinator._

object Parsing {
  object SimpleParser extends RegexParsers {
    override def skipWhitespace = false

    def nl = "\n"
    def semi = ";"
    def colon = ":"
    def digits = "[0-9]+".r
    def noNewLine = "[^\n]+".r

    def start = nl ~ colon
    def elapsed = digits <~ semi
    def timestamp = start ~> " " ~> digits <~ colon
    def command = ( noNewLine | (not(start) ~> nl) ).+ <~ guard(start)

    def line = timestamp ~ elapsed ~ command ^^ {
      case ts ~ el ~ cmd => (ts.toLong, el.toInt, cmd.mkString.trim)
    }

    def lines = line.+

    type Success_[+T] = Success[T]
    val Success_ = Success
  }

  type Success[+T] = SimpleParser.Success_[T]
  val Success = SimpleParser.Success_

  import SimpleParser.{parse, lines}
  def normalize(s: String) = "\n" + s + "\n:"
  def parseHistory(s: String) = parse(lines, normalize(s))
}


object Dumping {
  def dumpResults(res: List[(Long, Int, String)]) = {
    Using.resource(System.out) { out =>
      for( (ts, elapsed, command) <- res ) {
        // val date = new java.util.Date(ts*1000)
        // System.err.write((date.toString+"\n").getBytes)
        out.write(s": $ts:$elapsed;$command\n".getBytes("UTF-8"))
      }
    }
  }
}


object Main extends App {
  import scala.io.Source
  import Parsing.{Success, parseHistory}
  import Dumping.dumpResults

  val input = Source.stdin.mkString
  val Success(lines, _) = parseHistory(input): @unchecked
  dumpResults(lines.sortBy(_._1).distinct)
}
