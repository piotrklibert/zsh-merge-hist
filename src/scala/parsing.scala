package zsh.history


object Parsing {
  import scala.util.parsing.combinator.RegexParsers

  type ResultData = List[(Long, Int, String)]
  type ParseResult = SimpleParser.ParseResult[ResultData]

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
  }

  import java.io.SequenceInputStream

  def streamConcat(streams: java.io.InputStream*): java.io.InputStream =
    streams.reduce({new SequenceInputStream(_, _)})

  def toInputStream(s: String) =
    new java.io.ByteArrayInputStream(s.getBytes("UTF-8"))

  def normalize(s: String) = "\n" + s + "\n:"

  def normalize(s: java.io.InputStream) =
    streamConcat(toInputStream("\n"), s, toInputStream("\n:"))

  def parseHistory(s: String): Either[String, ResultData] = {
    parseHistory(toInputStream(normalize(s)))
  }

  def parseHistory(input: java.io.InputStream): Either[String, ResultData] = {
    import SimpleParser.{parse, lines}
    val reader = new java.io.InputStreamReader(input, "UTF-8")
    parse(lines, reader) match {
      case SimpleParser.Success(lines, _) => Right(lines)
      case SimpleParser.Failure(msg, _) => Left(msg)
      case SimpleParser.Error(msg, _) => Left(msg)
    }
  }
}
