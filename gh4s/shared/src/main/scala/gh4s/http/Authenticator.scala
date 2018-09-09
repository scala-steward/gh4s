package gh4s.http

import com.softwaremill.sttp.{HeaderNames, Request}

private[gh4s] object Authenticator {
  def noop: Authenticator = request => request

  def apiTokenAuthenticator(apiToken: String): Authenticator =
    request => request.header(HeaderNames.Authorization, s"token $apiToken")

  def credentialsAuthenticator(username: String, password: String, twoFactorCode: Option[String]): Authenticator =
    request => {
      val withBasicAuth = request.auth.basic(username, password)
      twoFactorCode.map(withBasicAuth.header("X-GitHub-OTP", _)).getOrElse(withBasicAuth)
    }
}
private[gh4s] trait Authenticator extends (Request[String, Nothing] => Request[String, Nothing])
