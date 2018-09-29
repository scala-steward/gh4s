package gh4s.model

import io.circe.generic.JsonCodec

@JsonCodec
final case class CodeOfConductDescription(
    key: String,
    name: String,
    url: String
)

@JsonCodec
final case class CodeOfConduct(
    key: String,
    name: String,
    url: String,
    body: String
)
