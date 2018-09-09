package gh4s.interpreter.misc

import cats.effect.Sync
import cats.implicits._
import com.softwaremill.sttp._
import gh4s.GithubClientConfig
import gh4s.algebra.misc.LicensesAlgebra
import gh4s.http.RequestRunner
import gh4s.model.{License, LicenseDescription, RepositoryLicense}

object LicensesInterpreter {
  def apply[F[_]](config: GithubClientConfig)(implicit F: Sync[F],
                                              backend: SttpBackend[F, Nothing]): LicensesAlgebra[F] =
    new LicensesAlgebra[F] {
      override def fetchAll: F[Seq[LicenseDescription]] =
        RequestRunner.asJson[F, Seq[LicenseDescription]](sttp.get(uri"${config.apiUrl}/licenses"), config)

      override def fetchOne(license: String): F[Option[License]] =
        RequestRunner.asJsonK[F, Option, License](sttp.get(uri"${config.apiUrl}/licenses/$license"), config)

      override def fetchByRepository(repository: String, owner: Option[String] = None): F[Option[RepositoryLicense]] =
        F.flatMap(RequestRunner.resolveOwner(owner, config)) { owner =>
          RequestRunner.asJsonK[F, Option, RepositoryLicense](
            sttp.get(uri"${config.apiUrl}/repos/$owner/$repository/license"),
            config
          )
        }
    }
}
