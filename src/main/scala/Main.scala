/*
Copyright (c) 2018 Chen Weiguang

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

import java.io.File
import java.nio.file.Paths
import java.util.{Map => JMap}
import scala.collection.JavaConversions._

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.{Path => HdfsPath}
import org.apache.hadoop.security.UserGroupInformation

import com.moandjiezana.toml.Toml
import org.rogach.scallop.ScallopConf

class ArgConf(arguments: Seq[String]) extends ScallopConf(arguments) {
  val fileConf = opt[String](required = true)
  // this takes in -vvv based on the first letter of variable name
  val verbose = tally()
  verify()
}

case class FileConf(
  globs: Array[String],
  dst: String,
  conf: JMap[String, String],
  keytab: Keytab
)

case class Keytab(login: String, path: String)

object Main extends App {
  private def fileConfAssert(field: AnyRef, name: String) = {
    if (field == null) {
      Log.e0(s"'${name}' field cannot be empty in TOML file")
      System.exit(1)
    }
  }

  // take in CLI arg configuration and set verbose level
  val argConf = new ArgConf(args)

  implicit val verboser: Verboser = new Verboser {
    def level(): Int = argConf.verbose()
  }

  Log.v2("Starting hdfs-syncer")

  val fileConf = new Toml()
    .read(new File(argConf.fileConf()))
    .to(classOf[FileConf])

  fileConfAssert(fileConf.globs, "globs")
  fileConfAssert(fileConf.dst, "dst")
  Log.v2(pprint.apply(fileConf))

  // set up the HDFS configuration
  val hdfsConf = new Configuration()

  val dstDir = Paths.get(fileConf.dst)

  if (fileConf.conf != null) {
    fileConf.conf.foreach { case (k, v) =>
      hdfsConf.set(k.stripPrefix("\"").stripSuffix("\"").trim, v)
    }
  }

  // allow both keytab and non-keytab login
  UserGroupInformation.setConfiguration(hdfsConf)

  if (fileConf.keytab != null) {
    UserGroupInformation.loginUserFromKeytab(fileConf.keytab.login, fileConf.keytab.path)
  }

  val fs = FileSystem.get(hdfsConf)

  if (fileConf.globs != null) {
    val files = fileConf.globs.flatMap(f => fs.globStatus(new HdfsPath(f)))

    for (f <- files) {
      val src = f.getPath

      val rel = HdfsPath.getPathWithoutSchemeAndAuthority(src)
        .toString
        .stripPrefix("/")

      val dst = new HdfsPath(dstDir.resolve(rel).toString)
      Log.v1(s"Syncing ${src} -> ${dst}")
      fs.copyToLocalFile(src, dst)
    }
  }

  fs.close
}
