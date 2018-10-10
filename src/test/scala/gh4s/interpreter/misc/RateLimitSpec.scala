package gh4s.interpreter.misc

import gh4s.{Gh4sSpec, GithubClientBuilder}
import org.http4s.HttpRoutes
import org.http4s.dsl.io._

class RateLimitSpec extends Gh4sSpec {

  describe("Rate limit interpreter") {
    describe("fetch") {
      it("should fetch the user's rate limit") {
        val client = newClient(HttpRoutes.of {
          case GET -> Root / "rate_limit" => jsonResponse("misc/rate-limit.json")
        })
        val githubClient = GithubClientBuilder.anonymous.build(client)

        val rateLimit = githubClient.misc.rateLimit.fetch.unsafeRunSync

        rateLimit.resources.core.limit shouldBe 5000
        rateLimit.resources.core.remaining shouldBe 4999
      }
    }
  }
}
