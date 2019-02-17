package model

import org.joda.time.DateTime

case class Speech(speaker: Speaker, topic: String, date: DateTime, wordCount: Int)
