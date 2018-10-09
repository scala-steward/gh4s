package gh4s

import cats.effect.Sync
import gh4s.http.{Authentication, Authenticator}
import org.http4s.Uri
import org.http4s.client.Client

object GithubClientBuilder {

  def anonymous: GithubClientBuilder[Authentication.Off] =
    new GithubClientBuilder(GithubClientBuilderConfig(Authenticator.noop))

  def apiToken(token: String): GithubClientBuilder[Authentication.On] =
    new GithubClientBuilder(GithubClientBuilderConfig(Authenticator.apiTokenAuthenticator(token)))

  def credentials(
      username: String,
      password: String,
      twoFactorCode: Option[String] = None
  ): GithubClientBuilder[Authentication.On] =
    new GithubClientBuilder(
      GithubClientBuilderConfig(Authenticator.credentialsAuthenticator(username, password, twoFactorCode))
    )
}

final private[gh4s] case class GithubClientBuilderConfig[A <: Authentication](
    authenticator: Authenticator[A],
    owner: Option[String] = None,
    apiUrl: Uri = Uri.unsafeFromString("https://api.github.com")
)

class GithubClientBuilder[A <: Authentication] private[gh4s] (configBuilder: GithubClientBuilderConfig[A]) {

  def owner(owner: String): GithubClientBuilder[A] =
    new GithubClientBuilder(configBuilder.copy(owner = Some(owner)))

  def apiUrl(apiUrl: Uri): GithubClientBuilder[A] =
    new GithubClientBuilder(configBuilder.copy(apiUrl = apiUrl))

  def build[F[_]: Sync](client: Client[F]): GithubClient[F, A] =
    new GithubClient(
      GithubClientConfig(
        client,
        configBuilder.owner,
        configBuilder.apiUrl,
        configBuilder.authenticator
      )
    )
}
