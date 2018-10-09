package gh4s.interpreter.misc

import cats.effect.Sync
import cats.implicits._
import gh4s.GithubClientConfig
import gh4s.algebra.misc.CodesOfConductAlgebra
import gh4s.http.{MediaType, RequestRunner}
import gh4s.model.{CodeOfConduct, CodeOfConductDescription}
import org.http4s.Method.GET
import org.http4s.client.dsl.Http4sClientDsl._

object CodesOfConductInterpreter {
  def apply[F[_]](config: GithubClientConfig[F, _])(implicit F: Sync[F]): CodesOfConductAlgebra[F] =
    new CodesOfConductAlgebra[F] {

      override def fetchAll: F[Seq[CodeOfConductDescription]] =
        RequestRunner.asJson[F, Seq[CodeOfConductDescription]](
          GET(config.apiUrl / "codes_of_conduct"),
          config,
          MediaType.Previews.ScarletWitch
        )

      override def fetchOne(codeOfConduct: String): F[Option[CodeOfConduct]] =
        RequestRunner.asJsonK[F, Option, CodeOfConduct](
          GET(config.apiUrl / "codes_of_conduct" / codeOfConduct),
          config,
          MediaType.Previews.ScarletWitch
        )

      override def fetchByRepository(repository: String, owner: Option[String] = None): F[Option[CodeOfConduct]] =
        F.flatMap(RequestRunner.resolveOwner(owner, config)) { owner =>
          RequestRunner.asJsonK[F, Option, CodeOfConduct](
            GET(config.apiUrl / "repos" / owner / repository / "community" / "code_of_conduct"),
            config,
            MediaType.Previews.ScarletWitch
          )
        }
    }
}
