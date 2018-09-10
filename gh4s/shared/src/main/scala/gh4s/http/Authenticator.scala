package gh4s.http

import com.softwaremill.sttp.{HeaderNames, Request}

private[gh4s] object Authenticator {
  private def fromFunction(f: Request[String, Nothing] => Request[String, Nothing]): Authenticator =
    new Authenticator {
      override def apply(req: Request[String, Nothing]) = f(req)
    }

  def noop: Authenticator = fromFunction(Predef.identity)

  def apiTokenAuthenticator(apiToken: String): Authenticator =
    fromFunction(_.header(HeaderNames.Authorization, s"token $apiToken"))

  def credentialsAuthenticator(username: String, password: String, twoFactorCode: Option[String]): Authenticator =
    fromFunction(request => {
      val withBasicAuth = request.auth.basic(username, password)
      twoFactorCode.map(withBasicAuth.header("X-GitHub-OTP", _)).getOrElse(withBasicAuth)
    })
}
private[gh4s] trait Authenticator extends (Request[String, Nothing] => Request[String, Nothing])
