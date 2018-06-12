import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

object Main extends App {
  println("Hello, World!")

  val conf = new Configuration()
  val fs = FileSystem.get(new URI("hdfs://localhost:8020"), conf)
  val output = fs.create(new Path("/test.txt"))
}
