package gh4s.interpreter.misc

import gh4s.Gh4sSpec
import gh4s.http.Authenticator
import monix.eval.Task

class GitignoreTemplateSpec extends Gh4sSpec {

  describe("Gitignore Template interpreter") {

    val config = newConfig(Authenticator.noop)

    describe("fetchAll") {
      it("should fetch the list of all templates") {
        implicit val backend = backendStub
          .whenRequestMatches(_.uri.path === List("gitignore", "templates"))
          .thenRespond(jsonResource("misc/gitignore-templates.json"))

        val templates = GitignoreTemplateInterpreter[Task](config).fetchAll.unsafeRunSync

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
        implicit val backend = backendStub
          .whenRequestMatches(_.uri.path === List("gitignore", "templates", "C"))
          .thenRespond(jsonResource("misc/gitignore-template-c.json"))

        val scalaTemplate = GitignoreTemplateInterpreter[Task](config).fetchOne("C").unsafeRunSync

        scalaTemplate.value.name shouldBe "C"
      }
    }
  }
}
