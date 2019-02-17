package model

case class Criterium(scoring: Speech => Scores,
                     aggregator: (Scores, Scores) => Scores,
                     ascending: Boolean)
