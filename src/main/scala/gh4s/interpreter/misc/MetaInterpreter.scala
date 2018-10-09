package gh4s.interpreter.misc

import cats.effect.Sync
import gh4s.GithubClientConfig
import gh4s.algebra.misc.MetaAlgebra
import gh4s.http.RequestRunner
import gh4s.model.Meta
import org.http4s.Method.GET
import org.http4s.client.dsl.Http4sClientDsl._

object MetaInterpreter {
  def apply[F[_]: Sync](config: GithubClientConfig[F, _]): MetaAlgebra[F] =
    new MetaAlgebra[F] {
      override def fetch: F[Meta] =
        RequestRunner.asJson[F, Meta](GET(config.apiUrl / "meta"), config)
    }
}
