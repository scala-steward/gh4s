package gh4s.interpreter.misc

import gh4s.Gh4sSpec
import gh4s.http.GithubClientException

class LicensesSpec extends Gh4sSpec {

  describe("Licenses interpreter") {
    describe("fetchAll") {
      it("should fetch the list of all licenses") {
        val licenses = client.misc.licenses.fetchAll.unsafeRunSync
        licenses should not be empty
      }
    }

    describe("fetchOne") {
      it("should fetch the MIT license") {
        val mit = client.misc.licenses.fetchOne("mit").unsafeRunSync
        mit should not be empty
        mit.value.key shouldBe "mit"
      }
    }

    describe("fetchByRepository") {
      it("should fetch Scala's license") {
        val scalaLicense = client.misc.licenses.fetchByRepository("scala", Some("scala")).unsafeRunSync
        scalaLicense should not be empty
      }

      it("should fail if the repository's owner is not specified") {
        val failed = client.misc.licenses.fetchByRepository("scala").materialize.unsafeRunSync
        failed.failure.exception shouldBe a[GithubClientException]
      }
    }
  }
}
