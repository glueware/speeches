package server

import java.net.URL

import repository.ExecutionSpeechRepository
import service.SpeakerRanking

object SpeechServer {

  def get(urls: Set[URL]) =
    new SpeakerRanking(ExecutionSpeechRepository).rankSpeakers(urls)
}
