package gh4s.http

import cats.MonoidK
import cats.effect.Sync
import com.softwaremill.sttp.{Request, SttpBackend}
import com.softwaremill.sttp.circe.{asJson => sttpAsJson}
import gh4s.GithubClientConfig
import io.circe.Decoder

object RequestRunner {
  private val ownerMissingException =
    new GithubClientException(
      "The owner must be specified either at the request or global level when required by the API.")

  def asJsonK[F[_], C[_], T](
      request: Request[String, Nothing],
      config: GithubClientConfig,
      mediaType: MediaType = MediaType.Default
  )(implicit F: Sync[F], C: MonoidK[C], D: Decoder[C[T]], backend: SttpBackend[F, Nothing]): F[C[T]] =
    F.flatMap(prepare(request, config, mediaType).response(sttpAsJson[C[T]]).send()) { response =>
      response.body.fold(
        _ => F.pure(C.empty[T]),
        _.fold(
          e => F.raiseError(e.error),
          F.pure
        )
      )
    }

  def asJson[F[_], T: Decoder](
      request: Request[String, Nothing],
      config: GithubClientConfig,
      mediaType: MediaType = MediaType.Default)(implicit F: Sync[F], backend: SttpBackend[F, Nothing]): F[T] =
    F.flatMap(prepare(request, config, mediaType).response(sttpAsJson[T]).send()) { response =>
      response.body.fold(
        msg => F.raiseError(new GithubApiException(response.code, msg)),
        _.fold(
          e => F.raiseError(e.error),
          F.pure
        )
      )
    }

  def resolveOwner[F[_]](owner: Option[String], config: GithubClientConfig)(implicit F: Sync[F]): F[String] =
    owner.orElse(config.owner).map(F.pure).getOrElse(F.raiseError(ownerMissingException))

  private def prepare(request: Request[String, Nothing],
                      config: GithubClientConfig,
                      mediaType: MediaType): Request[String, Nothing] = {
    val pipeline = config.authenticator.andThen(mediaType.applyHeader)
    pipeline(request)
  }
}
