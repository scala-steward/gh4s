package gh4s.interpreter.misc

import gh4s.Gh4sSpec

class RateLimitSpec extends Gh4sSpec {

  describe("Rate limit interpreter") {
    describe("fetch") {
      it("should fetch the user's rate limit") {
        val rateLimit = client.misc.rateLimit.fetch.unsafeRunSync
        rateLimit.resources.core.limit should be > 0
      }
    }
  }
}
