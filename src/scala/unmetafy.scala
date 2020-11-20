package zsh.history

import scala.collection.mutable.ArrayBuffer
import better.files.File

/**
  *
  */
object Unmetafy {
  val Meta = 0x83.toByte  // On the JVM bytes can only be signed, so numbers
                          // above 127 need to be be converted
  def unmetafy(file: File): String = unmetafy(file.byteArray)

  def unmetafy(bytes: Array[Byte]): String = {
    val it = bytes.iterator
    val out = new ArrayBuffer[Byte](bytes.length)
    while (it.hasNext) {
      val byte = it.next()
      if (byte == Meta)
        out.addOne((it.next() ^ 32).toByte)
      else
        out.addOne(byte)
    }
    new String(out.toArray, "UTF-8")
  }
}
