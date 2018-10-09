package gh4s.interpreter.misc

import gh4s.{Gh4sSpec, GithubClientBuilder}
import org.http4s.HttpRoutes
import org.http4s.dsl.io._

class GitignoreTemplateSpec extends Gh4sSpec {

  describe("Gitignore Template interpreter") {
    describe("fetchAll") {
      it("should fetch the list of all templates") {
        val client = newClient(HttpRoutes.of {
          case GET -> Root / "gitignore" / "templates" => jsonResponse("misc/gitignore-templates.json")
        })
        val githubClient = GithubClientBuilder.anonymous.build(client)

        val templates = githubClient.misc.gitignoreTemplates.fetchAll.unsafeRunSync

        templates.map(_.value) should contain theSameElementsAs
          Seq(
            "Actionscript",
            "Android",
            "AppceleratorTitanium",
            "Autotools",
            "Bancha",
            "C",
            "C++"
          )
      }
    }

    describe("fetchOne") {
      it("should fetch a single template") {
        val client = newClient(HttpRoutes.of {
          case GET -> Root / "gitignore" / "templates" / "C" => jsonResponse("misc/gitignore-template-c.json")
        })
        val githubClient = GithubClientBuilder.anonymous.build(client)

        val scalaTemplate = githubClient.misc.gitignoreTemplates.fetchOne("C").unsafeRunSync

        scalaTemplate.value.name shouldBe "C"
      }
    }
  }
}
