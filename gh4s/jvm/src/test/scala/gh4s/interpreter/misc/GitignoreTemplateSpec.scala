package gh4s.interpreter.misc

import gh4s.Gh4sSpec

class GitignoreTemplateSpec extends Gh4sSpec {

  describe("Gitignore Template interpreter") {
    describe("fetchAll") {
      it("should fetch the list of all templates") {
        val templates = client.misc.gitignoreTemplates.fetchAll.unsafeRunSync
        templates should not be empty
      }
    }

    describe("fetchOne") {
      it("should fetch a single template") {
        val scalaTemplate = client.misc.gitignoreTemplates.fetchOne("Scala").unsafeRunSync
        scalaTemplate.value.name shouldBe "Scala"
      }
    }
  }
}
