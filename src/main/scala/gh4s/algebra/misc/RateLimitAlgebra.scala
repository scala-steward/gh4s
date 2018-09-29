package gh4s.algebra.misc

import gh4s.model.RateLimit

trait RateLimitAlgebra[F[_]] {
  def fetch: F[RateLimit]
}
