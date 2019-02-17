package com.glue

import cats.{CoflatMap, Comonad, Id}
import cats.data.{RepresentableStore, State, Store}
import cats.implicits._

trait GlueStore[S, A] {

  type F[L] = Store[S, L]

  def pos: Store[S, A] => S = st => st.index

  def peek: Store[S, A] => A = st => st.extract

  def seek: (Store[S, A], S) => Store[S, A] = (st, s) => Store(st.fa, s)

  def map[B]: ((A => B) => (Store[S, A] => Store[S, B])) = f => (st => st.map(f))

  import RepresentableStore._

  def r = implicitly[Comonad[RepresentableStore[S => ?, S, ?]]]

  def extend[B]: (Store[S, A] => B) => (Store[S, A] => Store[S, B]) = (fb: Store[S, A] => B) => (st: Store[S, A]) => r.coflatMap(st)(fb)

  def duplicate[B]: Store[S, A] => Store[S, Store[S, A]] = st => st.coflatten

  implicit class StoreToCoflatMap[S,A](store: Store[S, A]) {
    def coflatMap[B](fb: Store[S, A] => B) = CoflatMap[Store[S, ?]].coflatMap(store)(fb)
  }

  case class `[`()
}

object Zap {
  def zap[S,A,B](state: State[S,A], store: Store[S,B]): (A,B) = {
    val (s, a) = state.run(store.index).value
    (a, Store(store.fa, s).peek(s))
  }
}

object ComonadStore {
  implicit def ComonadStore[S] =
    new Comonad[Store[S, ?]] {
      override def extract[A](x: Store[S, A]): A = x.extract

      override def coflatMap[A, B](fa: Store[S, A])(f: Store[S, A] => B): Store[S, B] = fa.coflatten.map(f)

      override def map[A, B](fa: Store[S, A])(f: A => B): Store[S, B] = fa.map(f)
    }
}

