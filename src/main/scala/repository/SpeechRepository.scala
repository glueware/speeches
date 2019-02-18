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

  def list: G[Speech]

  def score(query: Query): F[ScoringMap]

  protected def fold(scoringMap: ScoringMap, speech: Speech): ScoringMap = ???
}

object ExecutionSpeechRepository extends SpeechRepositoryAlg[IO] {
  class _Source() extends Source[IO] {
    type G[A] = Stream[IO, A]

    def list: G[Speech] = ???

    def score(query: Query): IO[ScoringMap] = ???
  }

  def apply(urL: URL): Source[IO] = ???
}

object TestSpeechRepository extends SpeechRepositoryAlg[Id] {
  class _Source(testList: List[Speech]) extends Source[Id] {
    type G[A] = List[A]

    def list: G[Speech] = testList

    def score(query: Query): Id[ScoringMap] =
      testList.foldLeft(ScoringMap.empty(query))(fold)
  }

  def apply(urL: URL): Source[Id] = {
    new _Source(List())
  }
}
