package gh4s.http

import org.http4s.Status

sealed abstract class GithubException              extends Exception
final class GithubClientException(message: String) extends GithubException
final class GithubApiException(statusCode: Status, message: String) extends GithubException {
  override def getMessage: String = s"GithubApiException: statusCode=${statusCode.code},message=$message"
}
