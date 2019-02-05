package gh4s

import cats.data._
import cats.effect.IO
import org.http4s._
import org.http4s.implicits._
import org.http4s.client.Client
import org.http4s.dsl.io._
import org.scalatest._

import scala.io.Source

class Gh4sSpec extends FunSpec with Matchers with OptionValues with EitherValues {

  protected def newClient(routes: HttpRoutes[IO]): Client[IO] = Client.fromHttpApp(routes.orNotFound)

  protected val noopClient: Client[IO] = newClient(HttpRoutes.empty[IO])

  protected def jsonResponse(resourcePath: String): IO[Response[IO]] =
    IO(getClass.getClassLoader.getResourceAsStream(resourcePath))
      .map(Source.fromInputStream)
      .bracket(in => IO(in.mkString))(in => IO(in.close()))
      .flatMap(Ok(_))

  protected def requireHeader(header: Header)(service: HttpRoutes[IO]): HttpRoutes[IO] =
    Kleisli { req =>
      req.headers
        .get(header.name)
        .map(_ => service(req))
        .getOrElse(OptionT.liftF(IO.pure(Response(status = Status.BadRequest))))
    }
}
