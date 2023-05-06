package zsh.history

object Storage {
    import Configuration.config
    import better.files._

    def clearDestDir(): Unit = {
        // println("Clearing temp dir...")
        File(config.tempDir).list.foreach(_.delete(false))
    }
}


object Switch {
    val T = new Switch(Some(true))
    val F = new Switch(Some(false))
    val N = new Switch(None)

    implicit def booleanToSwitch(b: Boolean): Switch = new Switch(Some(b))
    implicit def nullToSwitch(b: Null): Switch = new Switch(None)
    implicit def switchToBoolean(s: Switch): Boolean = if (s == T) true else false
}


case class Switch(value: Option[Boolean])


/** This object provides a service of diffing two collections (Lists) of
 * Strings. The results are printed in a table, formatted as markdown. It is
 * useful for debugging, especially around Unmetafy.
 */
object diff {
    import com.github.difflib._
    import text._

    import scala.jdk.CollectionConverters._

    def apply(lines1: List[String], lines2: List[String]) = {
        val generator = DiffRowGenerator
          .create()
          .showInlineDiffs(true)
          .inlineDiffByWord(true)
          .ignoreWhiteSpaces(true)
          .reportLinesUnchanged(false)
          .oldTag(f => "~")
          .newTag(f => "**")
          .build()

        val rows = generator.generateDiffRows(lines1.asJava, lines2.asJava)

        println("|original|new|")
        println("|--------|---|")
        for (row <- rows.asScala) {
            (row.getOldLine, row.getNewLine) match {
                case (l1, l2) if l1 == l2 => ;
                case (l1, l2) => println("|" + l1 + "|" + l2 + "|")
            }
        }
    }
}
