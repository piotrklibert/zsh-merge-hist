package zsh.history

import org.scalatest.FunSuite

class TestMerging extends FunSuite {
  import better.files._
  import Parsing.parseHistory
  import Transform.removeDuplicates
  import Dumping._

  val timestamp = 1538336195L + 1
  val input = (File.currentWorkingDirectory / "data/original_history").contentAsString()

  test("test parsing input") {
    val Right(parsed) = parseHistory(input): @unchecked
    assert(parsed.length == 20150)
  }

  test("test dumping to file") {
    val Right(parsed) = parseHistory(input): @unchecked
    assert(dumpResultsAsString(parsed) == input)
  }

  test("test merging working both pre-parse and when parsed separately") {
    val Right(parsed1) = parseHistory(input): @unchecked
    val Right(parsed2) = parseHistory(input): @unchecked
    val lst = parsed1 ++ parsed2
    assert(lst.length == 40300)
    val noDups = removeDuplicates(lst)
    assert(noDups.length == 20150)
    assert(input == dumpResultsAsString(noDups))

  }

  test("test removing duplicates in history") {
    val inp = input+input
    val Right(parsed) = parseHistory(inp): @unchecked
    assert(parsed.length == 40300)
    val noDups = removeDuplicates(parsed)
    assert(noDups.length == 20150)
    assert(input == dumpResultsAsString(noDups))
  }

  test("test merging two files") {
    val row = ": 1:0; some command"
    val Right(parsed) = parseHistory(input + "\n" + row): @unchecked
    assert(parsed.length == 20151)
    val sanitized = removeDuplicates(parsed)
    assert(sanitized.length == 20151)
    assert(sanitized(0) != parsed(0))
  }


  test("test merging two files 2") {
    val row = Dumping.renderLine(timestamp, 12, "some command")
    val Right(parsed) = parseHistory(input + "\n" + row): @unchecked
    assert(parsed.length == 20151)
    val sanitized = removeDuplicates(parsed)
    assert(sanitized.length == 20151)
    assert(sanitized(0) == parsed(0))
    assert(sanitized(1) != parsed(1))
    assert(sanitized(2) != parsed(2))
    val (l1, l2) = (sanitized.drop(2), parsed.drop(1).dropRight(1))
    assert(l1 == l2)
    // println(diff(l1.map(_._3), l2.map(_._3)))
  }
}




class TestSSH extends FunSuite {
  import better.files.File

  import Configuration.config

  test("getting all history files") {
    Storage.clearDestDir()
    Transfer.downloadHistFiles()
    assert(File(config.tempDir).list.length == config.hosts.length)
  }

  test("processing history files") {
    Storage.clearDestDir()
    Transfer.downloadHistFiles()
    Dumping.dumpResultsToFile(Transform.processHistFiles())
    assert(File(config.tempDir).list.length == config.hosts.length + 1)
    assert(File(config.fmtDest("merged")).exists)
  }


  // test("putting merged history back to hosts") {
  //   for (host <- hosts) {
  //     s"scp $dest/merged_hist $host:mgmnt/zsh_history.1".!
  //   }
  // }
}


class TestUnmetafy extends FunSuite {
  import Unmetafy.unmetafy
  import better.files._

  test("unmetafying simple array") {
    val bb = Array(100, 105, 99, 116, 32, 39, 111, 98, 114, 97, 122, 105, 196, 131, 167, 32, 115, 105, 196, 131, 185, 39).map(_.toByte)
    val s = unmetafy(bb)
    assert(s == "dict 'obrazić się'")
  }

  test("unmetafying simple list") {
    val bb = List(100, 105, 99, 116, 32, 39, 111, 98, 114, 97, 122, 105, 196, 131, 167, 32, 115, 105, 196, 131, 185, 39).map(_.toByte)
    val s = unmetafy(bb)
    assert(s == "dict 'obrazić się'")
  }

  test("unmetafying the whole history") {
    val ba = (File.home/"mgmnt/zsh_history").byteArray
    val s = unmetafy(ba)
    assert(s contains "obrazić się")
  }
}
