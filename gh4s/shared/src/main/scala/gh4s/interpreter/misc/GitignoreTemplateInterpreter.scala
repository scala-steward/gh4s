package gh4s.interpreter.misc

import cats.effect.Sync
import cats.implicits._
import com.softwaremill.sttp._
import gh4s.GithubClientConfig
import gh4s.algebra.misc.GitignoreTemplatesAlgebra
import gh4s.http.RequestRunner
import gh4s.model.{GitignoreTemplate, GitignoreTemplateName}

object GitignoreTemplateInterpreter {
  def apply[F[_]](config: GithubClientConfig)(implicit F: Sync[F],
                                              backend: SttpBackend[F, Nothing]): GitignoreTemplatesAlgebra[F] =
    new GitignoreTemplatesAlgebra[F] {
      override def fetchAll: F[Seq[GitignoreTemplateName]] =
        RequestRunner.asJson[F, Seq[GitignoreTemplateName]](sttp.get(uri"${config.apiUrl}/gitignore/templates"), config)

      override def fetchOne(template: String): F[Option[GitignoreTemplate]] =
        RequestRunner.asJsonK[F, Option, GitignoreTemplate](
          sttp.get(uri"${config.apiUrl}/gitignore/templates/$template"),
          config
        )
    }
}
