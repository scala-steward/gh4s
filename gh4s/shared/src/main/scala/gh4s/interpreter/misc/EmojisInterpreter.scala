package gh4s.interpreter.misc

import cats.effect.Sync
import com.softwaremill.sttp._
import gh4s.GithubClientConfig
import gh4s.algebra.misc.EmojisAlgebra
import gh4s.http.RequestRunner
import gh4s.model.Emoji

object EmojisInterpreter {
  def apply[F[_]](config: GithubClientConfig[_])(implicit F: Sync[F],
                                                 backend: SttpBackend[F, Nothing]): EmojisAlgebra[F] =
    new EmojisAlgebra[F] {
      override def fetchAll: F[Seq[Emoji]] =
        RequestRunner.asJson[F, Seq[Emoji]](sttp.get(uri"${config.apiUrl}/emojis"), config)
    }
}
