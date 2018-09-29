package gh4s.interpreter.misc

import cats.effect.Sync
import com.softwaremill.sttp._
import gh4s.GithubClientConfig
import gh4s.algebra.misc.RateLimitAlgebra
import gh4s.http.RequestRunner
import gh4s.model.RateLimit

object RateLimitInterpreter {
  def apply[F[_]](config: GithubClientConfig[_])(implicit F: Sync[F],
                                                 backend: SttpBackend[F, Nothing]): RateLimitAlgebra[F] =
    new RateLimitAlgebra[F] {
      override def fetch: F[RateLimit] =
        RequestRunner.asJson[F, RateLimit](sttp.get(uri"${config.apiUrl}/rate_limit"), config)
    }
}
