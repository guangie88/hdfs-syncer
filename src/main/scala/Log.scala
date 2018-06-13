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
}
