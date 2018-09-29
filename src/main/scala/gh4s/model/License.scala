package gh4s.model

import io.circe.generic.JsonCodec
import io.circe.generic.extras.{Configuration, ConfiguredJsonCodec, JsonKey}

object LicenseDescription {
  implicit private val configuration = Configuration.default.withSnakeCaseMemberNames
}

@ConfiguredJsonCodec
final case class LicenseDescription(
    key: String,
    name: String,
    spdxId: String,
    url: String
)

object License {
  implicit private val configuration = Configuration.default.withSnakeCaseMemberNames
}
@ConfiguredJsonCodec
final case class License(
    key: String,
    name: String,
    spdxId: String,
    url: String,
    htmlUrl: String,
    description: String,
    implementation: String,
    permissions: List[String],
    conditions: List[String],
    limitations: List[String],
    body: String,
    featured: Boolean
)

object RepositoryLicense {
  implicit private val configuration = Configuration.default.withSnakeCaseMemberNames
}
@ConfiguredJsonCodec
final case class RepositoryLicense(
    name: String,
    path: String,
    sha: String,
    size: Int,
    url: String,
    htmlUrl: String,
    gitUrl: String,
    downloadUrl: String,
    @JsonKey("type") fileType: String,
    content: String,
    encoding: String,
    @JsonKey("_links") links: RepositoryLicenceLinks,
    license: LicenseDescription
)

@JsonCodec
final case class RepositoryLicenceLinks(
    self: String,
    git: String,
    html: String
)
