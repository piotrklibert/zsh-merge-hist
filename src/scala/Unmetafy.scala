package zsh.history

import better.files.File

import scala.collection.mutable.ArrayBuffer

/** This is a translation of unmetafy.c from the ZSH source code. It's the same
 * algorithm that's used in BASH, too. It's used to unescape characters with
 * ASCII code above 131 (0x83) in the history file. Without unescaping these,
 * the history file is NOT a valid UTF8 string, which leads to errors when
 * trying to read the contents. */
object Unmetafy {
    // On the JVM bytes can only be signed, so numbers above 127 need to be converted
    private val Meta = 0x83.toByte
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
