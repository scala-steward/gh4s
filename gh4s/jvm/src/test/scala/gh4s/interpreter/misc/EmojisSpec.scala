package gh4s.interpreter.misc

import gh4s.Gh4sSpec

class EmojisSpec extends Gh4sSpec {

  describe("Emojis interpreter") {
    describe("fetchAll") {
      it("should fetch the list of all emojis") {
        val emojis = client.misc.emojis.fetchAll.unsafeRunSync
        emojis should not be empty
      }
    }
  }
}
