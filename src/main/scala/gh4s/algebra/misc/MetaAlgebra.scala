package gh4s.algebra.misc

import gh4s.model.Meta

trait MetaAlgebra[F[_]] {
  def fetch: F[Meta]
}
