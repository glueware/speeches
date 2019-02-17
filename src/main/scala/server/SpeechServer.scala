package server

import java.net.URL

import repository.ExecutionSpeechRepository
import service.SpeakerRanking


import cats.implicits._
import scala.concurrent.ExecutionContext.Implicits.global

object SpeechServer {
  implicit val traverse: cats.Traverse[scala.concurrent.Future] = ???

  def get(urls: Set[URL]) =
    new SpeakerRanking(ExecutionSpeechRepository).rankSpeakers(urls)
}
