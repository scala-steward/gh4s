package gh4s.interpreter.misc

import gh4s.Gh4sSpec
import gh4s.http.GithubClientException

class CodesOfConductSpec extends Gh4sSpec {

  describe("Codes of Conduct interpreter") {
    describe("fetchAll") {
      it("should fetch the list of all codes of conduct") {
        val codesOfConduct = client.misc.codesOfConduct.fetchAll.unsafeRunSync
        codesOfConduct should not be empty
      }
    }

    describe("fetchOne") {
      it("should be able to fetch the Contributor's Covenant code of conduct") {
        val contributorCovenant = client.misc.codesOfConduct.fetchOne("contributor_covenant").unsafeRunSync
        contributorCovenant should not be empty
      }
    }

    describe("fetchByRepository") {
      it("should be able to fetch Scala's code of conduct") {
        val scalaCodeOfConduct = client.misc.codesOfConduct.fetchByRepository("scala", Some("scala")).unsafeRunSync
        scalaCodeOfConduct should not be empty
      }

      it("should fail if the repository's owner is not specified") {
        val failed = client.misc.licenses.fetchByRepository("scala").materialize.unsafeRunSync
        failed.failure.exception shouldBe a[GithubClientException]
      }
    }
  }

}
