package server

import java.net.URL

import cats.effect.IO
import repository.ExecutionSpeechRepository
import service.SpeakerRanking
import cats.implicits._

object SpeechServer {
  implicit val traverse: cats.Traverse[IO] = ???

  def get(urls: Set[URL]) =
    new SpeakerRanking(ExecutionSpeechRepository).rankSpeakers(urls)
}
