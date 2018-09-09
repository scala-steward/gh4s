package gh4s.model

import io.circe.Decoder
import io.circe.generic.JsonCodec

object GitignoreTemplateName {
  implicit val decoder: Decoder[GitignoreTemplateName] = Decoder.decodeString.map(GitignoreTemplateName.apply)
}
final case class GitignoreTemplateName(value: String) extends AnyVal

@JsonCodec
final case class GitignoreTemplate(
    name: String,
    source: String
)
