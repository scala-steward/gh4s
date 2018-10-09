package gh4s.interpreter.misc

import cats.effect.Sync
import cats.implicits._
import gh4s.GithubClientConfig
import gh4s.algebra.misc.LicensesAlgebra
import gh4s.http.RequestRunner
import gh4s.model.{License, LicenseDescription, RepositoryLicense}
import org.http4s.Method.GET
import org.http4s.client.dsl.Http4sClientDsl._

object LicensesInterpreter {
  def apply[F[_]](config: GithubClientConfig[F, _])(implicit F: Sync[F]): LicensesAlgebra[F] =
    new LicensesAlgebra[F] {
      override def fetchAll: F[Seq[LicenseDescription]] =
        RequestRunner.asJson[F, Seq[LicenseDescription]](
          GET(config.apiUrl / "licenses"),
          config
        )

      override def fetchOne(license: String): F[Option[License]] =
        RequestRunner.asJsonK[F, Option, License](
          GET(config.apiUrl / "licenses" / license),
          config
        )

      override def fetchByRepository(repository: String, owner: Option[String] = None): F[Option[RepositoryLicense]] =
        F.flatMap(RequestRunner.resolveOwner(owner, config)) { owner =>
          RequestRunner.asJsonK[F, Option, RepositoryLicense](
            GET(config.apiUrl / "repos" / owner / repository / "license"),
            config
          )
        }
    }
}
