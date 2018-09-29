package gh4s

import cats.effect.Sync
import com.softwaremill.sttp.SttpBackend
import gh4s.algebra.misc.MiscAlgebra
import gh4s.http.{Authentication, Authenticator}
import gh4s.interpreter.misc.MiscInterpreter

final private[gh4s] case class GithubClientConfig[A <: Authentication](
    owner: Option[String],
    apiUrl: String,
    authenticator: Authenticator[A]
)

class GithubClient[F[_], A <: Authentication](private val config: GithubClientConfig[A])(
    implicit F: Sync[F],
    backend: SttpBackend[F, Nothing]) {

  val misc: MiscAlgebra[F] = MiscInterpreter(config)
}
