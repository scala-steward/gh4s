package gh4s.interpreter.misc

import gh4s.Gh4sSpec
import gh4s.http.Authenticator
import monix.eval.Task

class RateLimitSpec extends Gh4sSpec {

  describe("Rate limit interpreter") {

    val config = newConfig(Authenticator.noop)

    describe("fetch") {
      it("should fetch the user's rate limit") {
        implicit val backend = backendStub
          .whenRequestMatches(_.uri.path === List("rate_limit"))
          .thenRespond(jsonResource("misc/rate-limit.json"))

        val rateLimit = RateLimitInterpreter[Task](config).fetch.unsafeRunSync

        rateLimit.resources.core.limit shouldBe 5000
        rateLimit.resources.core.remaining shouldBe 4999
      }
    }
  }
}
