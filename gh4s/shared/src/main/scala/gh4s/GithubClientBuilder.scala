package gh4s

import cats.effect.Sync
import com.softwaremill.sttp.SttpBackend
import gh4s.http.{Authentication, Authenticator}

object GithubClientBuilder {

  def anonymous: GithubClientBuilder[Authentication.Off] =
    new GithubClientBuilder(GithubClientBuilderConfig(Authenticator.noop))

  def apiToken(token: String): GithubClientBuilder[Authentication.On] =
    new GithubClientBuilder(GithubClientBuilderConfig(Authenticator.apiTokenAuthenticator(token)))

  def credentials[A <: Authentication](
      username: String,
      password: String,
      twoFactorCode: Option[String]
  ): GithubClientBuilder[Authentication.On] =
    new GithubClientBuilder(
      GithubClientBuilderConfig(Authenticator.credentialsAuthenticator(username, password, twoFactorCode))
    )
}

final private[gh4s] case class GithubClientBuilderConfig[A <: Authentication](
    authenticator: Authenticator[A],
    owner: Option[String] = None,
    apiUrl: String = "https://api.github.com"
)

class GithubClientBuilder[A <: Authentication] private[gh4s] (configBuilder: GithubClientBuilderConfig[A]) {

  def owner(owner: String): GithubClientBuilder[A] =
    new GithubClientBuilder(configBuilder.copy(owner = Some(owner)))

  def apiUrl(apiUrl: String): GithubClientBuilder[A] =
    new GithubClientBuilder(configBuilder.copy(apiUrl = apiUrl))

  def build[F[_]: Sync](implicit backend: SttpBackend[F, Nothing]): GithubClient[F, A] = {
    val config = GithubClientConfig(configBuilder.owner, configBuilder.apiUrl, configBuilder.authenticator)
    new GithubClient(config)
  }
}
