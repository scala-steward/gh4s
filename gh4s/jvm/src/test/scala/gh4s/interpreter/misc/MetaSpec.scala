package gh4s.interpreter.misc

import gh4s.Gh4sSpec
import gh4s.http.Authenticator
import monix.eval.Task

class MetaSpec extends Gh4sSpec {

  describe("Meta interpreter") {

    val config = newConfig(Authenticator.noop)

    describe("fetch") {
      it("should fetch Github's metadata") {
        implicit val backend = backendStub
          .whenRequestMatches(_.uri.path === List("meta"))
          .thenRespond(jsonResource("misc/meta.json"))

        val meta = MetaInterpreter[Task](config).fetch.unsafeRunSync

        meta.githubServicesSha shouldBe "3a0f86fb8db8eea7ccbb9a95f325ddbedfb25e15"
        meta.git should contain only "127.0.0.1/32"
      }
    }
  }
}
