package gh4s.model

import io.circe.Decoder
import Function.tupled

object Emoji {
  implicit val emojiDecoder: Decoder[Seq[Emoji]] =
    Decoder.decodeMap[String, String].map(_.map(tupled(Emoji.apply _)).toSeq)
}
final case class Emoji(code: String, url: String)
