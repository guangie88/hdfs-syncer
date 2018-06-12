import java.io.File
import java.nio.file.Paths
import java.util.{Map => JMap}

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.{Path => HdfsPath}
import org.apache.hadoop.security.UserGroupInformation

import org.rogach.scallop.ScallopConf

import com.moandjiezana.toml.Toml

import scala.collection.JavaConversions._

class ArgConf(arguments: Seq[String]) extends ScallopConf(arguments) {
  val fileConf = opt[String](required = true)
  verify()
}

case class FileConf(
  globs: Array[String],
  dst: String,
  conf: JMap[String, String],
  keytab: Keytab,
)

case class Keytab(login: String, path: String)

object Main extends App {
  val argConf = new ArgConf(args)

  val fileConf = new Toml()
    .read(new File(argConf.fileConf()))
    .to(classOf[FileConf])

  // set up the HDFS configuration
  val hdfsConf = new Configuration()
  val dstDir = Paths.get(fileConf.dst)

  fileConf.conf.foreach { case (k, v) =>
    hdfsConf.set(k.stripPrefix("\"").stripSuffix("\"").trim, v)
  }

  UserGroupInformation.setConfiguration(hdfsConf)
  UserGroupInformation.loginUserFromKeytab(fileConf.keytab.login, fileConf.keytab.path)

  val fs = FileSystem.get(hdfsConf)
  val files = fileConf.globs.flatMap(f => fs.globStatus(new HdfsPath(f)))

  for (f <- files) {
    val src = f.getPath
    
    val rel = HdfsPath.getPathWithoutSchemeAndAuthority(src)
      .toString
      .stripPrefix("/")

    val dst = new HdfsPath(dstDir.resolve(rel).toString)
    fs.copyToLocalFile(src, dst)
  }

  fs.close
}
