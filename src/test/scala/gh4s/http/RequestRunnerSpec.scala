package gh4s.http

import java.net.UnknownHostException

import cats.effect.IO
import cats.instances.option._
import gh4s.{Gh4sSpec, GithubClientConfig}
import org.http4s.{HttpRoutes, MalformedMessageBodyFailure, Uri}
import org.http4s.client.Client
import org.http4s.client.dsl.io._
import org.http4s.dsl.io._

@SuppressWarnings(Array("org.wartremover.warts.Throw"))
class RequestRunnerSpec extends Gh4sSpec {

  describe("asJsonK") {
    it("should raise an error if the HTTP call fails") {
      val client  = newClient(HttpRoutes.of[IO] { case _ => IO(throw new UnknownHostException) })
      val config  = dummyConfig(client)
      val request = GET(Uri.unsafeFromString("http://test"))

      val result = RequestRunner.asJsonK[IO, Option, String](request, config)

      result.attempt.unsafeRunSync.isLeft shouldBe true
    }

    it("should return the container's empty value if the response's status code is 404") {
      val config  = dummyConfig(noopClient)
      val request = GET(Uri.unsafeFromString("http://test"))

      val result = RequestRunner.asJsonK[IO, Option, String](request, config)

      result.unsafeRunSync shouldBe None
    }

    it("should raise and error if the response's status code is not a 'success' and not 404") {
      val client  = newClient(HttpRoutes.of { case _ => BadRequest() })
      val config  = dummyConfig(client)
      val request = GET(Uri.unsafeFromString("http://test"))

      val result = RequestRunner.asJsonK[IO, Option, String](request, config)

      result.attempt.unsafeRunSync.isLeft shouldBe true
    }

    it("should raise an error if deserialization fails") {
      val client  = newClient(HttpRoutes.of { case _ => Ok("") })
      val config  = dummyConfig(client)
      val request = GET(Uri.unsafeFromString("http://test"))

      val result = RequestRunner.asJsonK[IO, Option, String](request, config)

      result.attempt.unsafeRunSync.left.value shouldBe a[MalformedMessageBodyFailure]
    }
  }

  describe("asJson") {
    it("should raise an error if the HTTP call fails") {
      val client  = newClient(HttpRoutes.of[IO] { case _ => IO(throw new UnknownHostException) })
      val config  = dummyConfig(client)
      val request = GET(Uri.unsafeFromString("http://test"))

      val result = RequestRunner.asJson[IO, String](request, config)

      result.attempt.unsafeRunSync.isLeft shouldBe true
    }

    it("should raise an error if the response's status code is 404") {
      val config  = dummyConfig(noopClient)
      val request = GET(Uri.unsafeFromString("http://test"))

      val result = RequestRunner.asJson[IO, String](request, config)

      result.attempt.unsafeRunSync.isLeft shouldBe true
    }

    it("should raise an error if deserialization fails") {
      val client  = newClient(HttpRoutes.of { case _ => Ok("") })
      val config  = dummyConfig(client)
      val request = GET(Uri.unsafeFromString("http://test"))

      val result = RequestRunner.asJson[IO, String](request, config)

      result.attempt.unsafeRunSync.left.value shouldBe a[MalformedMessageBodyFailure]
    }
  }

  describe("resolveOwner") {

    it("should use the owner passed as parameter first") {
      val config = dummyConfig(noopClient)
      val owner  = RequestRunner.resolveOwner(Some("owner1"), config.copy(owner = Some("owner2")))

      owner.unsafeRunSync shouldBe "owner1"
    }

    it("should default to the owner in the client's configuration otherwise") {
      val config = dummyConfig(noopClient)
      val owner  = RequestRunner.resolveOwner(None, config.copy(owner = Some("owner2")))

      owner.unsafeRunSync shouldBe "owner2"
    }

    it("should fail if no owner is specified at all") {
      val config = dummyConfig(noopClient)
      val owner  = RequestRunner.resolveOwner(None, config)

      owner.attempt.unsafeRunSync.left.value shouldBe a[GithubClientException]
    }
  }

  private def dummyConfig(client: Client[IO]) =
    GithubClientConfig(client, None, Uri.unsafeFromString("http://dummy.host"), Authenticator.noop)
}
