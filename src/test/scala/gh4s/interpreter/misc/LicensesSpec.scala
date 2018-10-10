package gh4s.interpreter.misc

import gh4s.{Gh4sSpec, GithubClientBuilder}
import gh4s.http.GithubClientException
import org.http4s.HttpRoutes
import org.http4s.dsl.io._

class LicensesSpec extends Gh4sSpec {

  describe("Licenses interpreter") {
    describe("fetchAll") {
      it("should fetch the list of all licenses") {
        val client = newClient(HttpRoutes.of {
          case GET -> Root / "licenses" => jsonResponse("misc/licenses.json")
        })
        val githubClient = GithubClientBuilder.anonymous.build(client)

        val licenses = githubClient.misc.licenses.fetchAll.unsafeRunSync

        licenses.map(_.key) should contain allOf ("mit", "unlicense", "apache-2.0")
      }
    }

    describe("fetchOne") {
      it("should fetch the MIT license") {
        val client = newClient(HttpRoutes.of {
          case GET -> Root / "licenses" / "mit" => jsonResponse("misc/license-mit.json")
        })
        val githubClient = GithubClientBuilder.anonymous.build(client)

        val mit = githubClient.misc.licenses.fetchOne("mit").unsafeRunSync

        mit should not be empty
        mit.value.key shouldBe "mit"
      }
    }

    describe("fetchByRepository") {
      it("should fetch benbalter/gman's license") {
        val client = newClient(HttpRoutes.of {
          case GET -> Root / "repos" / "benbalter" / "gman" / "license" =>
            jsonResponse("misc/license-benbalter-gman.json")
        })
        val githubClient = GithubClientBuilder.anonymous.build(client)

        val license = githubClient.misc.licenses.fetchByRepository("gman", Some("benbalter")).unsafeRunSync

        license should not be empty
        license.value.sha shouldBe "401c59dcc4570b954dd6d345e76199e1f4e76266"
      }

      it("should fail if the repository's owner is not specified") {
        val client = newClient(HttpRoutes.of {
          case GET -> Root / "repos" / "benbalter" / "gman" / "license" =>
            jsonResponse("misc/license-benbalter-gman.json")
        })
        val githubClient = GithubClientBuilder.anonymous.build(client)

        val failed = githubClient.misc.licenses.fetchByRepository("gman").attempt.unsafeRunSync

        failed.left.value shouldBe a[GithubClientException]
      }
    }
  }
}
