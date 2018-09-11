package gh4s

import com.softwaremill.sttp.okhttp.monix.OkHttpMonixBackend
import monix.eval.Task
import monix.execution.Scheduler
import org.scalatest._

import scala.concurrent.duration.Duration

abstract class Gh4sSpec extends FunSpec with Matchers with OptionValues with TryValues with EitherValues {
  private val githubToken = sys.env.getOrElse("GITHUB_TOKEN", "")

  implicit protected val scheduler = Scheduler.Implicits.global
  implicit protected val backend   = OkHttpMonixBackend()

  protected val client = GithubClientBuilder.apiToken(githubToken).build[Task]

  implicit class RichTask[T](task: Task[T]) {
    def unsafeRunSync: T = task.runSyncUnsafe(Duration.Inf)
  }

}
