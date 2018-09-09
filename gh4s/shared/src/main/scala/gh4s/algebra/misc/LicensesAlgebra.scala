package gh4s.algebra.misc

import gh4s.model.{License, LicenseDescription, RepositoryLicense}

trait LicensesAlgebra[F[_]] {
  def fetchAll: F[Seq[LicenseDescription]]
  def fetchOne(license: String): F[Option[License]]
  def fetchByRepository(repository: String, owner: Option[String] = None): F[Option[RepositoryLicense]]
}
