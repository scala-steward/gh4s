package gh4s.http

import java.net.UnknownHostException

import cats.effect.IO
import cats.implicits._
import io.circe.{Error => CirceError}
import com.softwaremill.sttp._
import gh4s.Gh4sSpec
import monix.eval.Task

class RequestRunnerSpec extends Gh4sSpec {

  describe("asJsonK") {
    it("should raise an error if the HTTP call fails") {
      implicit val backend = backendStub.whenAnyRequest.thenRespond(new UnknownHostException())
      val config           = newConfig(Authenticator.noop)
      val request          = sttp.get(uri"http://test")
      val task             = RequestRunner.asJsonK[Task, Option, String](request, config)

      task.materialize.unsafeRunSync.isFailure shouldBe true
    }

    it("should return the container's empty value if the response's status code is 404") {
      implicit val backend = backendStub.whenAnyRequest.thenRespondNotFound
      val config           = newConfig(Authenticator.noop)
      val request          = sttp.get(uri"http://test")
      val task             = RequestRunner.asJsonK[Task, Option, String](request, config)

      task.unsafeRunSync shouldBe None
    }

    it("should raise and error if the response's status code is not a 'success' and not 404") {
      implicit val backend = backendStub.whenAnyRequest.thenRespondWithCode(StatusCodes.Forbidden)
      val config           = newConfig(Authenticator.noop)
      val request          = sttp.get(uri"http://test")
      val task             = RequestRunner.asJsonK[Task, Option, String](request, config)

      task.materialize.unsafeRunSync.failure.isFailure shouldBe true
    }

    it("should raise an error if deserialization fails") {
      implicit val backend = backendStub.whenAnyRequest.thenRespond("")
      val config           = newConfig(Authenticator.noop)
      val request          = sttp.get(uri"http://test")
      val task             = RequestRunner.asJsonK[Task, Option, String](request, config)

      task.materialize.unsafeRunSync.failure.exception shouldBe a[CirceError]
    }
  }

  describe("asJson") {
    it("should raise an error if the HTTP call fails") {
      implicit val backend = backendStub.whenAnyRequest.thenRespond(new UnknownHostException())
      val config           = newConfig(Authenticator.noop)
      val request          = sttp.get(uri"http://test")
      val task             = RequestRunner.asJson[Task, String](request, config)

      task.materialize.unsafeRunSync.isFailure shouldBe true
    }

    it("should raise an error if the response's status code is 404") {
      implicit val backend = backendStub.whenAnyRequest.thenRespondNotFound
      val config           = newConfig(Authenticator.noop)
      val request          = sttp.get(uri"http://test")
      val task             = RequestRunner.asJson[Task, String](request, config)

      task.materialize.unsafeRunSync.isFailure shouldBe true
    }

    it("should raise an error if deserialization fails") {
      implicit val backend = backendStub.whenAnyRequest.thenRespond("")
      val config           = newConfig(Authenticator.noop)
      val request          = sttp.get(uri"http://test")
      val task             = RequestRunner.asJson[Task, String](request, config)

      task.materialize.unsafeRunSync.failure.exception shouldBe a[CirceError]
    }
  }

  describe("resolveOwner") {

    it("should use the owner passed as parameter first") {
      val config = newConfig(Authenticator.noop)
      val owner  = RequestRunner.resolveOwner[IO]("owner1".some, config.copy(owner = Some("owner2")))

      owner.unsafeRunSync() shouldBe "owner1"
    }

    it("should default to the owner in the client's configuration otherwise") {
      val config = newConfig(Authenticator.noop)
      val owner  = RequestRunner.resolveOwner[IO](None, config.copy(owner = Some("owner2")))

      owner.unsafeRunSync() shouldBe "owner2"
    }

    it("should fail if no owner is specified at all") {
      val owner = RequestRunner.resolveOwner[IO](None, newConfig(Authenticator.noop))

      owner.attempt.unsafeRunSync().left.value shouldBe a[GithubClientException]
    }
  }
}
