package gh4s.http

import org.http4s.{BasicCredentials, Header}
import org.http4s.headers.Authorization

private[gh4s] object Authenticator {

  def noop: Authenticator[Authentication.Off] =
    new Authenticator[Authentication.Off] {
      override val authHeaders = Nil
    }

  def apiTokenAuthenticator(apiToken: String): Authenticator[Authentication.On] =
    new Authenticator[Authentication.On] {
      override val authHeaders: List[Header] =
        List(Header("Authorization", s"token $apiToken"))
    }

  def credentialsAuthenticator(
      username: String,
      password: String,
      twoFactorCode: Option[String]
  ): Authenticator[Authentication.On] =
    new Authenticator[Authentication.On] {
      override val authHeaders: List[Header] = {
        val headers: List[Header] = List(Authorization(BasicCredentials(username, password)))
        twoFactorCode
          .map(Header("X-GitHub-OTP", _) :: headers)
          .getOrElse(headers)
      }
    }
}

private[gh4s] trait Authenticator[A <: Authentication] {
  def authHeaders: List[Header]
}
