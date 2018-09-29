package gh4s.interpreter.misc

import gh4s.Gh4sSpec
import gh4s.http.{Authenticator, GithubClientException}
import monix.eval.Task

class LicensesSpec extends Gh4sSpec {

  describe("Licenses interpreter") {

    val config = newConfig(Authenticator.noop)

    describe("fetchAll") {
      it("should fetch the list of all licenses") {
        implicit val backend = backendStub
          .whenRequestMatches(_.uri.path === List("licenses"))
          .thenRespond(jsonResource("misc/licenses.json"))

        val licenses = LicensesInterpreter[Task](config).fetchAll.unsafeRunSync

        licenses.map(_.key) should contain allOf ("mit", "unlicense", "apache-2.0")
      }
    }

    describe("fetchOne") {
      it("should fetch the MIT license") {
        implicit val backend = backendStub
          .whenRequestMatches(_.uri.path === List("licenses", "mit"))
          .thenRespond(jsonResource("misc/license-mit.json"))

        val mit = LicensesInterpreter[Task](config).fetchOne("mit").unsafeRunSync

        mit should not be empty
        mit.value.key shouldBe "mit"
      }
    }

    describe("fetchByRepository") {
      it("should fetch benbalter/gman's license") {
        implicit val backend = backendStub
          .whenRequestMatches(_.uri.path === List("repos", "benbalter", "gman", "license"))
          .thenRespond(jsonResource("misc/license-benbalter-gman.json"))

        val license = LicensesInterpreter[Task](config).fetchByRepository("gman", Some("benbalter")).unsafeRunSync

        license should not be empty
        license.value.sha shouldBe "401c59dcc4570b954dd6d345e76199e1f4e76266"
      }

      it("should fail if the repository's owner is not specified") {
        implicit val backend = backendStub
          .whenRequestMatches(_.uri.path === List("repos", "benbalter", "gman", "license"))
          .thenRespond(jsonResource("misc/license-benbalter-gman.json"))

        val failed = LicensesInterpreter[Task](config).fetchByRepository("gman").materialize.unsafeRunSync

        failed.failure.exception shouldBe a[GithubClientException]
      }
    }
  }
}
