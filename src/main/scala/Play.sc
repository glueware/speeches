import cats.implicits._
import scala.concurrent.ExecutionContext
import cats.effect.ContextShift

import cats._, cats.data._, cats.syntax.all._, cats.effect.IO

val anIO = IO(1)

val aLotOfIOs = List(anIO, anIO)

val ioOfList = aLotOfIOs.parSequence