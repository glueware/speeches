package service

import cats.implicits._
import cats.{Applicative, Traverse}
import java.net.URL

import model._
import repository.{Source, SpeechRepositoryAlg}

import org.json4s._

trait SpeakerRankingAlg[F[_]] {
  val speechRepositoryFactory: SpeechRepositoryAlg[F]

  def rankSpeakers[G[_]](query: Query, sources: Set[Source[F]]): F[SpeakerRankingResult]

  def rankSpeakers(urls: Set[URL]): F[SpeakerRankingResult] =
    rankSpeakers(defaultQuery, urls.map(speechRepositoryFactory.apply))

  private lazy val defaultQuery: Query = ???
}

class SpeakerRanking[F[_]](val speechRepositoryFactory: SpeechRepositoryAlg[F])
                          (implicit traverse: Traverse[F], applicative: Applicative[F])
  extends SpeakerRankingAlg[F] {

  def rankSpeakers[G[_]](query: Query, sources: Set[Source[F]]): F[SpeakerRankingResult] = {

    val scoringOfSources = sources.toList.traverse(_.score(query))

    def fold(aggregated: ScoringMap, next: ScoringMap): ScoringMap = {
      def aggregateSpeakerScoring(aggregator: (Scores, Scores) => Scores,
                                  speakerScoring: SpeakerScoring,
                                  aggregatedSpeakerScoring: SpeakerScoring): SpeakerScoring = {

        val speakers = speakerScoring.keySet ++ aggregatedSpeakerScoring.keySet

        def aggregateScores(aggregatedScores: Option[Scores], speakerScores: Option[Scores]): Scores =
          (aggregatedScores, speakerScores) match {
            case (Some(a), Some(s)) => aggregator(a, s)
            case (Some(a), _) => a
            case (_, Some(s)) => s
            // case _ => should never occur
          }

        Map(speakers.map(
          speaker =>
            speaker ->
              aggregateScores(aggregatedSpeakerScoring.get(speaker), speakerScoring.get(speaker))
        ).toSeq: _*)
      }

      val criteria = next.keySet
      Map(criteria.map(
        criteriumName =>
          criteriumName ->
            aggregateSpeakerScoring(
              query(criteriumName).aggregator,
              aggregated(criteriumName),
              next(criteriumName))
      ).toSeq: _*)
    }

    val aggregationOfSources: F[ScoringMap] =
      applicative.map(scoringOfSources)(_.foldLeft(ScoringMap.empty(query))(fold))

    def toSpeakerRankingResult(scoringMap: ScoringMap): SpeakerRankingResult = {
      def result(speakerScoring: SpeakerScoring): Option[Speaker] = ???

      val keyValues = scoringMap.mapValues(result).toList.collect {
        case (criterium, Some(speaker)) => (criterium, JString(speaker))
      }
      JObject(keyValues.toSeq: _*)
    }

    applicative.map(aggregationOfSources)(toSpeakerRankingResult)
  }
}
