package gh4s.interpreter.misc

import gh4s.Gh4sSpec

class MetaSpec extends Gh4sSpec {

  describe("Meta interpreter") {
    describe("fetch") {
      it("should fetch Github's metadata") {
        val meta = client.misc.meta.fetch.unsafeRunSync
        meta.git should not be empty
      }
    }
  }
}
