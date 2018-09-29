package gh4s.algebra.misc

import gh4s.model.Emoji

trait EmojisAlgebra[F[_]] {
  def fetchAll: F[Seq[Emoji]]
}
