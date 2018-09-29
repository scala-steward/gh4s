package gh4s.model

import io.circe.generic.JsonCodec

@JsonCodec
final case class RateLimit(
    resources: Resources
)

@JsonCodec
final case class Resources(
    core: Limits,
    search: Limits
)

@JsonCodec
final case class Limits(
    limit: Int,
    remaining: Int,
    reset: Long
)
