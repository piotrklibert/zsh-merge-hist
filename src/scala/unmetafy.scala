package zsh.history

import better.files._


/** I never learned why did the authors of ZSH apply such character-escaping
  * scheme. It's not like ZSH can't work with clean UTF-8... at least I didn't
  * find any problems. The problem with this is that it makes the contents of
  * history file a not-clean UTF-8, which, of course, splendidly breaks all the
  * text processing methods - there's no other way than treating it as a
  * sequence of bytes. The object below does exactly that and then reverses the
  * escaping to produce a full-fledged String.
  *
  * The original algorithm for this was implemented in a C function `unmetafy`,
  * which should be in `unmetafy/unmetafy.c`. It's short and efficient, but it's
  * a bit hard to decipher... Well, it's worth seeing at least once in a
  * lifetime.
  */
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
