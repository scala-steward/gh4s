package gh4s.http

import com.softwaremill.sttp.HeaderNames
import gh4s.HttpRequest

object Authenticator {

  def noop: Authenticator[Authentication.Off] =
    fromFunction(identity)

  def apiTokenAuthenticator(apiToken: String): Authenticator[Authentication.On] =
    fromFunction(_.header(HeaderNames.Authorization, s"token $apiToken"))

  def credentialsAuthenticator(
      username: String,
      password: String,
      twoFactorCode: Option[String]
  ): Authenticator[Authentication.On] =
    fromFunction(request => {
      val withBasicAuth = request.auth.basic(username, password)
      twoFactorCode.map(withBasicAuth.header("X-GitHub-OTP", _)).getOrElse(withBasicAuth)
    })

  private def fromFunction[A <: Authentication](f: HttpRequest => HttpRequest): Authenticator[A] =
    new Authenticator[A] {
      override def apply(req: HttpRequest): HttpRequest = f(req)
    }
}
trait Authenticator[A <: Authentication] extends (HttpRequest => HttpRequest)
