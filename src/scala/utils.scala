package zsh.history


object Storage {
  import better.files._
  import Configuration.config

  def clearDestDir(): Unit = {
    println("Clearing temp dir...")
    File(config.tempDir).list.foreach(_.delete(false))
  }
}


/** This object provides a service of diffing two collections (Lists) of
  * Strings. The results are printed in a table, formatted as markdown. It is
  * useful for debugging, especially around Unmetafy.
  */
object diff {
  import com.github.difflib._  // from io.github.java-diff-utils:java-diff-utils:4.4
  import text._
  import scala.jdk.CollectionConverters._

  def apply(lines1: List[String], lines2: List[String]) = {
    val generator = DiffRowGenerator.create()
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
