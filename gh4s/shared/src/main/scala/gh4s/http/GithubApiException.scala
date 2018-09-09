package gh4s.http

sealed abstract class GithubException              extends Exception
final class GithubClientException(message: String) extends GithubException
final class GithubApiException(statusCode: Int, message: String) extends GithubException {
  override def getMessage: String = s"GithubApiException: statusCode=$statusCode,message=$message"
}
