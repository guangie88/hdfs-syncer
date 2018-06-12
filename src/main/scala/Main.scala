import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation 

object Main extends App {
  println("Hello, World!")

  val conf = new Configuration()

  conf.set("fs.defaultFS", "hdfs://localhost:8020")
  conf.set("hadoop.security.authentication", "kerberos")
  conf.set("hadoop.rpc.protection", "integrity");
  conf.set("dfs.namenode.kerberos.principal.pattern", "hdfs/localhost@esciencecenter.nl");

  UserGroupInformation.setConfiguration(conf);
  UserGroupInformation.loginUserFromKeytab("hdfs/localhost@esciencecenter.nl", "./config/krb5.keytab");

  val fs = FileSystem.get(conf)
  val output = fs.create(new Path("/test.txt"))
}
