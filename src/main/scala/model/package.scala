import org.json4s.JObject

package object model {
  type Topic = String
  type Speaker = String
  type Scores = Int
  type CriteriumName = String
  type Query = Map[CriteriumName, Criterium]
  type SpeakerScoring = Map[Speaker, Scores]
  type ScoringMap = Map[CriteriumName, SpeakerScoring]
  type SpeakerRankingResult = JObject

  object ScoringMap {
    def empty(query: Query): ScoringMap = ???

  }

}
