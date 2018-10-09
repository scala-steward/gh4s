package gh4s.interpreter.misc

import cats.effect.Sync
import gh4s.GithubClientConfig
import gh4s.algebra.misc.RateLimitAlgebra
import gh4s.http.RequestRunner
import gh4s.model.RateLimit
import org.http4s.Method.GET
import org.http4s.client.dsl.Http4sClientDsl._

object RateLimitInterpreter {
  def apply[F[_]: Sync](config: GithubClientConfig[F, _]): RateLimitAlgebra[F] =
    new RateLimitAlgebra[F] {
      override def fetch: F[RateLimit] =
        RequestRunner.asJson[F, RateLimit](GET(config.apiUrl / "rate_limit"), config)
    }
}
