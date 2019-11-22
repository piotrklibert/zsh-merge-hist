package zsh.history
import better.files._


object Unmetafy {
  import scala.annotation.tailrec
  import scala.collection.immutable.ArraySeq

  val Meta = 0x83.toByte  // On the JVM bytes can only be signed, so numbers
                          // above 127 need to be be converted

  def unmetafy(f: File): String =
    unmetafy(f.byteArray)

  def unmetafy(ba: Seq[Byte]): String = {
    @tailrec
    def traverse(in: List[Byte], out: List[Byte]): List[Byte] =
      in match {
        case a :: b :: rest if a == Meta =>
          traverse(rest, (b ^ 32).toByte :: out)
        case a :: rest => traverse(rest, a :: out)
        case Nil => out
      }
    new String(traverse(ba.toList, List()).reverse.toArray, "UTF-8")
  }

  def unmetafy(ba: Array[Byte]): String =
    unmetafy(ArraySeq.unsafeWrapArray(ba))
}
