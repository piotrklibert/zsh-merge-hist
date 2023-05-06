package zsh.history


object Parsing {

    import scala.util.parsing.combinator.RegexParsers

    type ResultData = List[(Long, Int, String)]
    type ParseResult = SimpleParser.ParseResult[ResultData]

    object SimpleParser extends RegexParsers {
        override def skipWhitespace = false

        private def nl = "\n"
        private def semi = ";"
        private def colon = ":"
        private def digits = "[0-9]+".r
        private def noNewLine = "[^\n]+".r
        private def start = nl ~ colon
        private def elapsed = digits <~ semi
        private def timestamp = start ~> " " ~> digits <~ colon

        private def command = (noNewLine | (not(start) ~> nl)).* <~ guard(start)

        private def line = timestamp ~ elapsed ~ command ^^ {
            case ts ~ el ~ cmd => (ts.toLong, el.toInt, cmd.mkString.trim)
        }
        def lines: Parser[List[(Long, Int, String)]] = line.+
    }

    import java.io.SequenceInputStream

    def streamConcat(streams: java.io.InputStream*): java.io.InputStream =
        streams.reduce({
            new SequenceInputStream(_, _)
        })

    def toInputStream(s: String) =
        new java.io.ByteArrayInputStream(s.getBytes("UTF-8"))

    def normalize(s: String) = "\n" + s + "\n:"

    def normalize(s: java.io.InputStream) =
        streamConcat(toInputStream("\n"), s, toInputStream("\n:"))

    def parseHistory(s: String): Either[String, ResultData] = {
        parseHistory(toInputStream(normalize(s)))
    }

    def parseHistory(input: java.io.InputStream): Either[String, ResultData] = {
        import SimpleParser.{lines, parse}
        val reader = new java.io.InputStreamReader(input, "UTF-8")
        parse(lines, reader) match {
            case SimpleParser.Success(lines, _) => Right(lines)
            case SimpleParser.Failure(msg, _) => Left(msg)
            case SimpleParser.Error(msg, _) => Left(msg)
        }
    }
}
