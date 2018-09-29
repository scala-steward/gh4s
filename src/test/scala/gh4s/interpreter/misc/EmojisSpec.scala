package gh4s.interpreter.misc

import gh4s.Gh4sSpec
import gh4s.http.Authenticator
import monix.eval.Task

class EmojisSpec extends Gh4sSpec {

  describe("Emojis interpreter") {

    val config = newConfig(Authenticator.noop)

    describe("fetchAll") {
      it("should fetch the list of all emojis") {
        implicit val backend = backendStub
          .whenRequestMatches(_.uri.path === List("emojis"))
          .thenRespond(jsonResource("misc/emojis.json"))

        val emojis = EmojisInterpreter[Task](config).fetchAll.unsafeRunSync

        emojis.map(_.code) should contain("+1")
      }
    }
  }
}
