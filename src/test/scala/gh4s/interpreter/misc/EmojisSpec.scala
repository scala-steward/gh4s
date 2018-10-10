package gh4s.interpreter.misc

import gh4s.{Gh4sSpec, GithubClientBuilder}
import org.http4s.HttpRoutes
import org.http4s.dsl.io._

class EmojisSpec extends Gh4sSpec {

  describe("Emojis interpreter") {
    describe("fetchAll") {
      it("should fetch the list of all emojis") {
        val client = newClient(HttpRoutes.of {
          case GET -> Root / "emojis" => jsonResponse("misc/emojis.json")
        })
        val githubClient = GithubClientBuilder.anonymous.build(client)

        val emojis = githubClient.misc.emojis.fetchAll.unsafeRunSync

        emojis.map(_.code) should contain("+1")
      }
    }
  }
}
