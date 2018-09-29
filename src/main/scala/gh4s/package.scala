import com.softwaremill.sttp.Request

package object gh4s {
  type HttpRequest = Request[String, Nothing]
}
