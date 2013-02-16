// Run this generator like a script:
// scala Generator.scala > ../src/main/scala/com/twitter/bijection/clojure/GeneratedIFnBijections.scala

/*
 * Example of the code generated:

 implicit def function0ToIFn[A]: Bijection[Function0[A], IFn] =
  new AbstractBijection[Function0[A], IFn] {
    def apply(fn: Function0[A]) = new AFn {
      override def invoke: AnyRef = fn.apply.asInstanceOf[AnyRef]
    }
    override def invert(fn: IFn) = { () => fn.invoke.asInstanceOf[A] }
  }
*/

val lowerLetters = ('a' to 'z').map { _.toString }.toIndexedSeq
val upperLetters = ('A' to 'Z').map { _.toString }.toIndexedSeq

def castParam(i: Int): String =
  "%s.asInstanceOf[%s]".format(lowerLetters(i), upperLetters(i))

def typeParam(i: Int): String =
  "%s: %s".format(lowerLetters(i), upperLetters(i))

def genParams(count: Int)(fn: Int => String): String =
  (0 until count).map { fn(_) }.mkString(",")

// Here we put it all together:
def implicitIFn(i: Int): String = {
  val func = "function" + i
  val types = genParams(i + 1) { upperLetters(_) }
  val funcType = "%s[%s]".format(func.capitalize, types)
  val bijectionType = "Bijection[%s, IFn]".format(funcType)
  """  implicit def %sToIFn[%s]: %s =
    new Abstract%s {
      def apply(fn: %s) = new AFn {
        override def invoke(%s): AnyRef = fn.apply(%s).asInstanceOf[AnyRef]
      }
      override def invert(fn: IFn) = { (%s) => fn.invoke(%s).asInstanceOf[%s] }
    }""".format(func, types, bijectionType, bijectionType, funcType,
                genParams(i) { "%s: AnyRef" format lowerLetters(_) },
                genParams(i)(castParam),
                genParams(i){ lowerLetters(_) },
                genParams(i){ lowerLetters(_) },
                upperLetters(i))
}

println("// Autogenerated code DO NOT EDIT BY HAND")
println("package com.twitter.bijection.clojure")
println("import clojure.lang.{ AFn, IFn }")
println("import com.twitter.bijection.{ AbstractBijection, Bijection, CastInjection }")
println("\ntrait GeneratedIFnBijections {")
(0 to 20).map { implicitIFn(_) }.foreach { println(_) }
println("}")
