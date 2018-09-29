package gh4s

import cats.effect.IO
import com.softwaremill.sttp.impl.monix.TaskMonadAsyncError
import com.softwaremill.sttp.testing.SttpBackendStub
import gh4s.http.{Authentication, Authenticator}
import monix.eval.Task
import monix.execution.Scheduler
import org.scalatest._

import scala.concurrent.duration.Duration
import scala.io.Source

class Gh4sSpec extends FunSpec with Matchers with OptionValues with TryValues with EitherValues {

  implicit protected val scheduler = Scheduler.Implicits.global
  protected val backendStub        = SttpBackendStub[Task, Nothing](TaskMonadAsyncError)

  def newConfig[A <: Authentication](authenticator: Authenticator[A]) =
    GithubClientConfig(None, "http://dummy.host", authenticator)

  def jsonResource(resourcePath: String): String =
    IO(getClass.getClassLoader.getResourceAsStream(resourcePath))
      .map(Source.fromInputStream)
      .bracket(in => IO(in.mkString))(in => IO(in.close()))
      .unsafeRunSync

  implicit class RichTask[T](task: Task[T]) {
    def unsafeRunSync: T = task.runSyncUnsafe(Duration.Inf)
  }
}
