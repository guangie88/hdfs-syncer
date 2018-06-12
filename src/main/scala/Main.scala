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

  fileConf.conf.foreach { case (k, v) => {
    val key = k.stripPrefix("\"").stripSuffix("\"").trim
    conf.set(key, v)
  }}

  UserGroupInformation.setConfiguration(conf)
  UserGroupInformation.loginUserFromKeytab("hdfs/localhost@esciencecenter.nl", "./config/krb5.keytab")

  val fs = FileSystem.get(conf)

  val files =
    fs.globStatus(new Path("/data/*/*/valid_data_count/count.csv")) ++
    fs.globStatus(new Path("/data/*/*/volume_logs/{input,output}.log"))

  for (f <- files) {
    println(f)
  }

  fs.close()
}
