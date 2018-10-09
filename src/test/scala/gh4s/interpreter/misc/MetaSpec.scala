package gh4s.interpreter.misc

import gh4s.{Gh4sSpec, GithubClientBuilder}
import org.http4s.HttpRoutes
import org.http4s.dsl.io._

class MetaSpec extends Gh4sSpec {

  describe("Meta interpreter") {
    describe("fetch") {
      it("should fetch Github's metadata") {
        val client = newClient(HttpRoutes.of {
          case GET -> Root / "meta" => jsonResponse("misc/meta.json")
        })
        val githubClient = GithubClientBuilder.anonymous.build(client)

        val meta = githubClient.misc.meta.fetch.unsafeRunSync

        meta.githubServicesSha shouldBe "3a0f86fb8db8eea7ccbb9a95f325ddbedfb25e15"
        meta.git should contain only "127.0.0.1/32"
      }
    }
  }
}
