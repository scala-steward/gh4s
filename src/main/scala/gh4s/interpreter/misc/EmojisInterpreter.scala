package gh4s.interpreter.misc

import cats.effect.Sync
import gh4s.GithubClientConfig
import gh4s.algebra.misc.EmojisAlgebra
import gh4s.http.RequestRunner
import gh4s.model.Emoji
import org.http4s.Method.GET
import org.http4s.client.dsl.Http4sClientDsl._

object EmojisInterpreter {
  def apply[F[_]: Sync](config: GithubClientConfig[F, _]): EmojisAlgebra[F] =
    new EmojisAlgebra[F] {
      override def fetchAll: F[Seq[Emoji]] =
        RequestRunner.asJson[F, Seq[Emoji]](GET(config.apiUrl / "emojis"), config)
    }
}
