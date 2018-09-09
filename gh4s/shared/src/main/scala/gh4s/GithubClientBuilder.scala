package gh4s

import cats.effect.Sync
import com.softwaremill.sttp.SttpBackend
import gh4s.http.Authenticator

object GithubClientBuilder {

  def anonymous: GithubClientBuilder =
    new GithubClientBuilder(GithubClientBuilderConfig(Authenticator.noop))

  def apiToken(token: String): GithubClientBuilder =
    new GithubClientBuilder(GithubClientBuilderConfig(Authenticator.apiTokenAuthenticator(token)))

  def credentials(username: String, password: String, twoFactorCode: Option[String]): GithubClientBuilder =
    new GithubClientBuilder(
      GithubClientBuilderConfig(Authenticator.credentialsAuthenticator(username, password, twoFactorCode))
    )
}

final private[gh4s] case class GithubClientBuilderConfig(
    authenticator: Authenticator,
    owner: Option[String] = None,
    apiUrl: String = "https://api.github.com",
)

class GithubClientBuilder private[gh4s] (configBuilder: GithubClientBuilderConfig) {

  def owner(owner: String): GithubClientBuilder =
    new GithubClientBuilder(configBuilder.copy(owner = Some(owner)))

  def apiUrl(apiUrl: String): GithubClientBuilder =
    new GithubClientBuilder(configBuilder.copy(apiUrl = apiUrl))

  def build[F[_]: Sync](implicit backend: SttpBackend[F, Nothing]): GithubClient[F] = {
    val config = GithubClientConfig(configBuilder.owner, configBuilder.apiUrl, configBuilder.authenticator)
    new GithubClient(config)
  }
}
