import java.io.File
import java.util.{Map => JMap}

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.hadoop.security.UserGroupInformation

import com.moandjiezana.toml.Toml

import scala.collection.JavaConversions._

case class Keytab(login: String, path: String)

case class FileConf(
  conf: JMap[String, String],
  keytab: Keytab,
  globs: Array[String],
)

object Main extends App {
  val fileConf = new Toml()
    .read(new File("config/hdfs-syncer.toml"))
    .to(classOf[FileConf])

  // set up the HDFS configuration
  val conf = new Configuration()

  fileConf.conf.foreach { case (k, v) =>
    conf.set(k.stripPrefix("\"").stripSuffix("\"").trim, v)
  }

  UserGroupInformation.setConfiguration(conf)
  UserGroupInformation.loginUserFromKeytab(fileConf.keytab.login, fileConf.keytab.path)

  val fs = FileSystem.get(conf)
  val files = fileConf.globs.flatMap(f => fs.globStatus(new Path(f)))

  for (f <- files) {
    println(f)
  }

  fs.close()
}
