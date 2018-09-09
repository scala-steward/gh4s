package gh4s.http

import cats.effect.IO
import cats.implicits._
import io.circe.{Error => CirceError}
import com.softwaremill.sttp._
import gh4s.{Gh4sSpec, GithubClientConfig}
import monix.eval.Task

class RequestRunnerSpec extends Gh4sSpec {
  private val emptyConfig = GithubClientConfig(None, "", Authenticator.noop)

  describe("asJsonK") {
    it("should raise an error if the HTTP call fails") {
      val request = sttp.get(uri"http://foo.foobar")
      val task    = RequestRunner.asJsonK[Task, Option, String](request, emptyConfig)
      task.materialize.unsafeRunSync.isFailure shouldBe true
    }

    it("should return the container's empty value if the response's status code is 404") {
      val request = sttp.get(uri"http://example.com/foo")
      val task    = RequestRunner.asJsonK[Task, Option, String](request, emptyConfig)
      task.unsafeRunSync shouldBe None
    }

    it("should raise an error if deserialization fails") {
      val request = sttp.get(uri"http://example.com")
      val task    = RequestRunner.asJsonK[Task, Option, String](request, emptyConfig)
      task.materialize.unsafeRunSync.failure.exception shouldBe a[CirceError]
    }
  }

  describe("asJson") {
    it("should raise an error if the HTTP call fails") {
      val request = sttp.get(uri"http://foo.foobar")
      val task    = RequestRunner.asJson[Task, String](request, emptyConfig)
      task.materialize.unsafeRunSync.isFailure shouldBe true
    }

    it("should raise an error if the response's status code is 404") {
      val request = sttp.get(uri"http://example.com/foo")
      val task    = RequestRunner.asJson[Task, String](request, emptyConfig)
      task.materialize.unsafeRunSync.isFailure shouldBe true
    }

    it("should raise an error if deserialization fails") {
      val request = sttp.get(uri"http://example.com")
      val task    = RequestRunner.asJson[Task, String](request, emptyConfig)
      task.materialize.unsafeRunSync.failure.exception shouldBe a[CirceError]
    }
  }

  describe("resolveOwner") {

    it("should use the owner passed as parameter first") {
      val owner = RequestRunner.resolveOwner[IO]("owner1".some, emptyConfig.copy(owner = Some("owner2")))
      owner.unsafeRunSync() shouldBe "owner1"
    }

    it("should default to the owner in the client's configuration otherwise") {
      val owner = RequestRunner.resolveOwner[IO](None, emptyConfig.copy(owner = Some("owner2")))
      owner.unsafeRunSync() shouldBe "owner2"
    }

    it("should fail if no owner is specified at all") {
      val owner = RequestRunner.resolveOwner[IO](None, emptyConfig)
      owner.attempt.unsafeRunSync().left.value shouldBe a[GithubClientException]
    }
  }
}
