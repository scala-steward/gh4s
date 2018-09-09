package gh4s.model

import io.circe.generic.extras.{Configuration, ConfiguredJsonCodec}

object Meta {
  implicit private val configuration = Configuration.default.withSnakeCaseMemberNames
}
@ConfiguredJsonCodec
final case class Meta(
    verifiablePasswordAuthentication: Boolean,
    githubServicesSha: String,
    hooks: Seq[String],
    git: Seq[String],
    pages: Seq[String],
    importer: Seq[String]
)
