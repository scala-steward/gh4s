package gh4s.http

import cats.{Functor, MonoidK}
import cats.effect.Sync
import gh4s.GithubClientConfig
import io.circe.Decoder
import org.http4s.Request
import org.http4s.Status.{NotFound, Successful}
import org.http4s.circe.jsonOf

private[gh4s] object RequestRunner {
  private val ownerMissingException =
    new GithubClientException(
      "The owner must be specified either at the request or global level when required by the API.")

  def asJsonK[F[_], C[_], T](
      request: F[Request[F]],
      config: GithubClientConfig[F, _],
      mediaType: MediaType = MediaType.Default
  )(implicit F: Sync[F], C: MonoidK[C], D: Decoder[C[T]]): F[C[T]] =
    config.client.fetch(prepare(request, config, mediaType)) {
      case Successful(response) => response.as(F, jsonOf[F, C[T]])
      case NotFound(_)          => F.pure(C.empty[T])
      case response             => F.raiseError(new GithubApiException(response.status, response.body.toString))
    }

  def asJson[F[_], T: Decoder](
      request: F[Request[F]],
      config: GithubClientConfig[F, _],
      mediaType: MediaType = MediaType.Default
  )(implicit F: Sync[F]): F[T] =
    config.client.fetch(prepare(request, config, mediaType)) {
      case Successful(response) => response.as(F, jsonOf[F, T])
      case response             => F.raiseError(new GithubApiException(response.status, response.body.toString))
    }

  def resolveOwner[F[_]](owner: Option[String], config: GithubClientConfig[F, _])(implicit F: Sync[F]): F[String] =
    owner.orElse(config.owner).map(F.pure).getOrElse(F.raiseError(ownerMissingException))

  private def prepare[F[_]](
      request: F[Request[F]],
      config: GithubClientConfig[F, _],
      mediaType: MediaType
  )(implicit F: Functor[F]): F[Request[F]] =
    F.map(request)(_.putHeaders(mediaType.header :: config.authenticator.authHeaders: _*))
}
