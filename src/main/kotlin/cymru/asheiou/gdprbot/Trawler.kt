package cymru.asheiou.gdprbot

import java.io.File
import java.nio.file.Files
import java.nio.file.Path

class Trawler(val baseDir: Path, val match: String) {
  fun run(): MutableList<File> {
    val out = mutableListOf<File>()
    Files.walk(baseDir).filter {
      Files.isRegularFile(it)
    }.forEach {
      val file = it.toFile()
      if(file.readText().contains(match)
        || file.name.contains(match)) {
        out.add(file)
      }
    }
    return out
  }
}