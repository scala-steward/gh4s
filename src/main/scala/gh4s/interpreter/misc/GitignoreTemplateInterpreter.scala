package gh4s.interpreter.misc

import cats.effect.Sync
import cats.implicits._
import gh4s.GithubClientConfig
import gh4s.algebra.misc.GitignoreTemplatesAlgebra
import gh4s.http.RequestRunner
import gh4s.model.{GitignoreTemplate, GitignoreTemplateName}
import org.http4s.Method.GET
import org.http4s.client.dsl.Http4sClientDsl._

object GitignoreTemplateInterpreter {
  def apply[F[_]: Sync](config: GithubClientConfig[F, _]): GitignoreTemplatesAlgebra[F] =
    new GitignoreTemplatesAlgebra[F] {
      override def fetchAll: F[Seq[GitignoreTemplateName]] =
        RequestRunner.asJson[F, Seq[GitignoreTemplateName]](
          GET(config.apiUrl / "gitignore" / "templates"),
          config
        )

      override def fetchOne(template: String): F[Option[GitignoreTemplate]] =
        RequestRunner.asJsonK[F, Option, GitignoreTemplate](
          GET(config.apiUrl / "gitignore" / "templates" / template),
          config
        )
    }
}
