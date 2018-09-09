package gh4s

import cats.effect.Sync
import com.softwaremill.sttp.SttpBackend
import gh4s.algebra.misc.MiscAlgebra
import gh4s.http.Authenticator
import gh4s.interpreter.misc.MiscInterpreter

final private[gh4s] case class GithubClientConfig(
    owner: Option[String],
    apiUrl: String,
    authenticator: Authenticator
)

class GithubClient[F[_]](private val config: GithubClientConfig)(implicit F: Sync[F],
                                                                 backend: SttpBackend[F, Nothing]) {

  val misc: MiscAlgebra[F] = MiscInterpreter(config)
}
