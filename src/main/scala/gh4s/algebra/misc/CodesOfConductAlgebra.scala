package gh4s.algebra.misc

import gh4s.model.{CodeOfConduct, CodeOfConductDescription}

trait CodesOfConductAlgebra[F[_]] {
  def fetchAll: F[Seq[CodeOfConductDescription]]
  def fetchOne(codeOfConduct: String): F[Option[CodeOfConduct]]
  def fetchByRepository(repository: String, owner: Option[String] = None): F[Option[CodeOfConduct]]
}
