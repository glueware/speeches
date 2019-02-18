package repository

import java.net.URL

import cats.Id
import cats.effect.IO
import fs2.Stream
import model._


trait SpeechRepositoryAlg[F[_]] {
  def apply(urL: URL): Source[F]
}

trait Source[F[_]] {

  type G[_]

  def findAll: G[Speech]

  def score(query: Query): F[ScoringMap]

  protected def op(scoringMap: ScoringMap, speech: Speech): ScoringMap = ???
}

object ExecutionSpeechRepository extends SpeechRepositoryAlg[IO] {

  class _Source(urL: URL) extends Source[IO] {
    type G[A] = Stream[IO, A]

    def findAll: G[Speech] = ???

    def score(query: Query): IO[ScoringMap] =
      findAll.compile.fold(ScoringMap.empty(query))(op)
  }

  def apply(urL: URL): Source[IO] = ???
}

object TestSpeechRepository extends SpeechRepositoryAlg[Id] {

  class _Source(testList: List[Speech]) extends Source[Id] {
    type G[A] = List[A]

    def findAll: G[Speech] = testList

    def score(query: Query): Id[ScoringMap] =
      testList.foldLeft(ScoringMap.empty(query))(op)
  }

  def apply(urL: URL): Source[Id] = {
    val someTestData: List[Speech] = ???
    new _Source(someTestData)
  }

}
