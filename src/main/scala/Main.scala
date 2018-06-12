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
  keytab: Keytab,
)

case class Keytab(login: String, path: String)

object Main extends App {
  // take in CLI arg configuration and set verbose level
  val argConf = new ArgConf(args)

  implicit val verboser: Verboser = new Verboser {
    def level(): Int = argConf.verbose()
  }

  val fileConf = new Toml()
    .read(new File(argConf.fileConf()))
    .to(classOf[FileConf])

  Log.v1(Log.prettyPrint(fileConf))

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
    Log.v1(s"Syncing ${src} -> ${dst}")
    fs.copyToLocalFile(src, dst)
  }

  fs.close
}
