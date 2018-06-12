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

abstract class Verboser {
  def level: Int
}

object Log {
  def v0(msg: AnyRef)(implicit v: Verboser): Unit = {
    if (v.level >= 0) {
      println(msg)
    }
  }

  def v1(msg: AnyRef)(implicit v: Verboser): Unit = {
    if (v.level >= 1) {
      println(msg)
    }
  }

  def v2(msg: AnyRef)(implicit v: Verboser): Unit = {
    if (v.level >= 2) {
      println(msg)
    }
  }

  def v3(msg: AnyRef)(implicit v: Verboser): Unit = {
    if (v.level >= 3) {
      println(msg)
    }
  }

  def e0(msg: AnyRef)(implicit v: Verboser): Unit = {
    if (v.level >= 0) {
      System.err.println(msg)
    }
  }

  def e1(msg: AnyRef)(implicit v: Verboser): Unit = {
    if (v.level >= 1) {
      System.err.println(msg)
    }
  }

  def e2(msg: AnyRef)(implicit v: Verboser): Unit = {
    if (v.level >= 2) {
      System.err.println(msg)
    }
  }

  def e3(msg: AnyRef)(implicit v: Verboser): Unit = {
    if (v.level >= 3) {
      System.err.println(msg)
    }
  }

  // source: https://gist.github.com/carymrobbins/7b8ed52cd6ea186dbdf8
  /**
   * Pretty prints a Scala value similar to its source represention.
   * Particularly useful for case classes.
   * @param a - The value to pretty print.
   * @param indentSize - Number of spaces for each indent.
   * @param maxElementWidth - Largest element size before wrapping.
   * @param depth - Initial depth to pretty print indents.
   * @return
   */
  // def prettyPrint(a: Any, indentSize: Int = 2, maxElementWidth: Int = 30, depth: Int = 0): String = {
  //   val indent = " " * depth * indentSize
  //   val fieldIndent = indent + (" " * indentSize)
  //   val thisDepth = prettyPrint(_: Any, indentSize, maxElementWidth, depth)
  //   val nextDepth = prettyPrint(_: Any, indentSize, maxElementWidth, depth + 1)
  //   a match {
  //     // Make Strings look similar to their literal form.
  //     case s: String =>
  //       val replaceMap = Seq(
  //         "\n" -> "\\n",
  //         "\r" -> "\\r",
  //         "\t" -> "\\t",
  //         "\"" -> "\\\""
  //       )
  //       '"' + replaceMap.foldLeft(s) { case (acc, (c, r)) => acc.replace(c, r) } + '"'
  //     // For an empty Seq just use its normal String representation.
  //     case xs: Seq[_] if xs.isEmpty => xs.toString()
  //     case xs: Seq[_] =>
  //       // If the Seq is not too long, pretty print on one line.
  //       val resultOneLine = xs.map(nextDepth).toString()
  //       if (resultOneLine.length <= maxElementWidth) return resultOneLine
  //       // Otherwise, build it with newlines and proper field indents.
  //       val result = xs.map(x => s"\n$fieldIndent${nextDepth(x)}").toString()
  //       result.substring(0, result.length - 1) + "\n" + indent + ")"
  //     // Product should cover case classes.
  //     case p: Product =>
  //       val prefix = p.productPrefix
  //       // We'll use reflection to get the constructor arg names and values.
  //       val cls = p.getClass
  //       val fields = cls.getDeclaredFields.filterNot(_.isSynthetic).map(_.getName)
  //       val values = p.productIterator.toSeq
  //       // If we weren't able to match up fields/values, fall back to toString.
  //       if (fields.length != values.length) return p.toString
  //       fields.zip(values).toList match {
  //         // If there are no fields, just use the normal String representation.
  //         case Nil => p.toString
  //         // If there is just one field, let's just print it as a wrapper.
  //         case (_, value) :: Nil => s"$prefix(${thisDepth(value)})"
  //         // If there is more than one field, build up the field names and values.
  //         case kvps =>
  //           val prettyFields = kvps.map { case (k, v) => s"$fieldIndent$k = ${nextDepth(v)}" }
  //           // If the result is not too long, pretty print on one line.
  //           val resultOneLine = s"$prefix(${prettyFields.mkString(", ")})"
  //           if (resultOneLine.length <= maxElementWidth) return resultOneLine
  //           // Otherwise, build it with newlines and proper field indents.
  //           s"$prefix(\n${prettyFields.mkString(",\n")}\n$indent)"
  //       }
  //     // If we haven't specialized this type, just use its toString.
  //     case _ => a.toString
  //   }
  // }
}
