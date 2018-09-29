package gh4s.interpreter.misc

import com.softwaremill.sttp.{HeaderNames, Request}
import gh4s.Gh4sSpec
import gh4s.http.{Authenticator, GithubClientException, MediaType}
import monix.eval.Task

class CodesOfConductSpec extends Gh4sSpec {

  describe("Codes of Conduct interpreter") {

    val config = newConfig(Authenticator.noop)

    describe("fetchAll") {
      it("should fetch the list of all codes of conduct") {
        implicit val backend = backendStub
          .whenRequestMatches(_.uri.path === List("codes_of_conduct"))
          .thenRespond(jsonResource("misc/codes-of-conduct.json"))
          .whenRequestMatches(!hasPreviewMediaType(_))
          .thenRespondServerError

        val codesOfConduct = CodesOfConductInterpreter[Task](config).fetchAll.unsafeRunSync

        codesOfConduct should not be empty
        codesOfConduct.map(_.key) should contain theSameElementsAs Seq("contributor_covenant",
                                                                       "citizen_code_of_conduct")
      }
    }

    describe("fetchOne") {
      it("should be able to fetch the Contributor's Covenant code of conduct") {
        implicit val backend = backendStub
          .whenRequestMatches(_.uri.path === List("codes_of_conduct", "contributor_covenant"))
          .thenRespond(jsonResource("misc/code-of-conduct-contributor-covenant.json"))
          .whenRequestMatches(!hasPreviewMediaType(_))
          .thenRespondServerError

        val contributorCovenant = CodesOfConductInterpreter[Task](config).fetchOne("contributor_covenant").unsafeRunSync

        contributorCovenant should not be empty
      }
    }

    describe("fetchByRepository") {
      it("should be able to fetch Scala's code of conduct") {
        implicit val backend = backendStub
          .whenRequestMatches(_.uri.path === List("repos", "LindseyB", "cosee", "community", "code_of_conduct"))
          .thenRespond(jsonResource("misc/code-of-conduct-LindseyB-cosee.json"))
          .whenRequestMatches(!hasPreviewMediaType(_))
          .thenRespondServerError

        val codeOfConduct =
          CodesOfConductInterpreter[Task](config).fetchByRepository("cosee", Some("LindseyB")).unsafeRunSync

        codeOfConduct should not be empty
        codeOfConduct.value.key shouldBe "contributor_covenant"
      }

      it("should fail if the repository's owner is not specified") {
        implicit val backend = backendStub
          .whenRequestMatches(_.uri.path === List("repos", "LindseyB", "cosee", "community", "code_of_conduct"))
          .thenRespond(jsonResource("misc/licenses.json"))
          .whenRequestMatches(!hasPreviewMediaType(_))
          .thenRespondServerError

        val failed = CodesOfConductInterpreter[Task](config).fetchByRepository("cosee").materialize.unsafeRunSync

        failed.failure.exception shouldBe a[GithubClientException]
      }
    }
  }

  private def hasPreviewMediaType(request: Request[_, _]): Boolean =
    request.headers.exists {
      case (name, mediaType) =>
        name === HeaderNames.Accept && mediaType === MediaType.Previews.ScarletWitch
    }

}
