package gh4s.http

import scala.annotation.implicitNotFound

sealed trait Authentication
object Authentication {
  @implicitNotFound(
    "This operation requires an authenticated client: use GithubClientBuilder's apiToken(...) or credentials(...).")
  type IsAuthenticated[A] = A =:= On

  sealed trait On  extends Authentication
  sealed trait Off extends Authentication
}
