package gh4s.interpreter.misc

import cats.effect.Sync
import com.softwaremill.sttp._
import gh4s.GithubClientConfig
import gh4s.algebra.misc.MetaAlgebra
import gh4s.http.RequestRunner
import gh4s.model.Meta

object MetaInterpreter {
  def apply[F[_]](config: GithubClientConfig[_])(implicit F: Sync[F],
                                                 backend: SttpBackend[F, Nothing]): MetaAlgebra[F] =
    new MetaAlgebra[F] {
      override def fetch: F[Meta] =
        RequestRunner.asJson[F, Meta](sttp.get(uri"${config.apiUrl}/meta"), config)
    }
}
