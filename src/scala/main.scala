package zsh.history

import scala.util.Using
import scala.util.parsing.combinator._

object Parsing {

  object SimpleParser extends RegexParsers {
    override def skipWhitespace = false

    type S[+T] = Success[T]
    val S = Success

    def nl = "\n"
    def semi = ";"
    def colon = ":"
    def digits = "[0-9]+".r
    def noNewLine = "[^\n]+".r

    def start = nl ~ colon
    def elapsed = digits <~ semi
    def timestamp = start ~> " " ~> digits <~ colon

    def head = timestamp ~ elapsed
    def tail = ( noNewLine | (not(start) ~> nl) ).+ <~ guard(start)

    def line = head ~ tail ^^ { case a ~ b ~ c => (a.toLong, b.toInt, c.mkString.trim) }

    def lines = line.+
  }

  type Success[+T] = SimpleParser.S[T]
  val Success = SimpleParser.S

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

  val Success(parseResult, _) = parseHistory(input): @unchecked

  val res = parseResult.sortBy(_._1).distinct

  dumpResults(res)
}
