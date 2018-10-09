package gh4s.interpreter.misc

import gh4s.{Gh4sSpec, GithubClientBuilder}
import gh4s.http._
import org.http4s.HttpRoutes
import org.http4s.dsl.io._

class CodesOfConductSpec extends Gh4sSpec {

  describe("Codes of Conduct interpreter") {
    describe("fetchAll") {
      it("should fetch the list of all codes of conduct") {
        val client = newClient(requireScarletWitchHeader(HttpRoutes.of {
          case GET -> Root / "codes_of_conduct" => jsonResponse("misc/codes-of-conduct.json")
        }))
        val githubClient = GithubClientBuilder.anonymous.build(client)

        val codesOfConduct = githubClient.misc.codesOfConduct.fetchAll.unsafeRunSync

        codesOfConduct should not be empty
        codesOfConduct.map(_.key) should contain only ("contributor_covenant", "citizen_code_of_conduct")
      }
    }

    describe("fetchOne") {
      it("should be able to fetch the Contributor's Covenant code of conduct") {
        val client = newClient(requireScarletWitchHeader(HttpRoutes.of {
          case GET -> Root / "codes_of_conduct" / "contributor_covenant" =>
            jsonResponse("misc/code-of-conduct-contributor-covenant.json")
        }))
        val githubClient = GithubClientBuilder.anonymous.build(client)

        val contributorCovenant = githubClient.misc.codesOfConduct.fetchOne("contributor_covenant").unsafeRunSync

        contributorCovenant should not be empty
      }
    }

    describe("fetchByRepository") {
      it("should be able to fetch Scala's code of conduct") {
        val client = newClient(requireScarletWitchHeader(HttpRoutes.of {
          case GET -> Root / "repos" / "LindseyB" / "cosee" / "community" / "code_of_conduct" =>
            jsonResponse("misc/code-of-conduct-contributor-covenant.json")
        }))
        val githubClient = GithubClientBuilder.anonymous.build(client)

        val codeOfConduct = githubClient.misc.codesOfConduct.fetchByRepository("cosee", Some("LindseyB")).unsafeRunSync

        codeOfConduct should not be empty
        codeOfConduct.value.key shouldBe "contributor_covenant"
      }

      it("should fail if the repository's owner is not specified") {
        val client = newClient(requireScarletWitchHeader(HttpRoutes.of {
          case GET -> Root / "repos" / "LindseyB" / "cosee" / "community" / "code_of_conduct" =>
            jsonResponse("misc/code-of-conduct-contributor-covenant.json")
        }))
        val githubClient = GithubClientBuilder.anonymous.build(client)

        val failed = githubClient.misc.codesOfConduct.fetchByRepository("cosee").attempt.unsafeRunSync

        failed.left.value shouldBe a[GithubClientException]
      }
    }
  }

  private def requireScarletWitchHeader =
    requireHeader(MediaType.Previews.ScarletWitch.header)(_)

}
