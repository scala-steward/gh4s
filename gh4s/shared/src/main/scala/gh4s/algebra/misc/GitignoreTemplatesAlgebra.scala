package gh4s.algebra.misc

import gh4s.model.{GitignoreTemplate, GitignoreTemplateName}

trait GitignoreTemplatesAlgebra[F[_]] {
  def fetchAll: F[Seq[GitignoreTemplateName]]
  def fetchOne(template: String): F[Option[GitignoreTemplate]]
}
