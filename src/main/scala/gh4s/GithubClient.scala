package gh4s

import cats.effect.Sync
import gh4s.algebra.misc.MiscAlgebra
import gh4s.http.{Authentication, Authenticator}
import gh4s.interpreter.misc.MiscInterpreter
import org.http4s.Uri
import org.http4s.client.Client

final private[gh4s] case class GithubClientConfig[F[_]: Sync, A <: Authentication](
    client: Client[F],
    owner: Option[String],
    apiUrl: Uri,
    authenticator: Authenticator[A]
)

class GithubClient[F[_]: Sync, A <: Authentication](private val config: GithubClientConfig[F, A]) {

  val misc: MiscAlgebra[F] = MiscInterpreter(config)
}
