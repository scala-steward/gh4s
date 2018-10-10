package gh4s.http

import org.http4s.Header

sealed abstract class MediaType(mediaType: String) {
  def header: Header = Header("Accept", mediaType)
}

object MediaType {
  case object Default extends MediaType("application/vnd.github.v3+json")

  object Previews {
    case object ScarletWitch extends MediaType("application/vnd.github.scarlet-witch-preview+json")
  }

  sealed abstract class CommentBodyMediaType(mediaType: String) extends MediaType(mediaType)
  object CommentBodyMediaType {
    case object Html extends CommentBodyMediaType("application/vnd.github.v3.html+json")
    case object Raw  extends CommentBodyMediaType("application/vnd.github.v3.raw+json")
    case object Text extends CommentBodyMediaType("application/vnd.github.v3.text+json")
  }

  sealed abstract class GitBlobMediaType(mediaType: String) extends MediaType(mediaType)
  object GitBlobMediaType {
    case object Json extends GitBlobMediaType("application/vnd.github.v3+json")
    case object Raw  extends GitBlobMediaType("application/vnd.github.v3.raw")
  }

  sealed abstract class CommitMediaType(mediaType: String) extends MediaType(mediaType)
  object CommitMediaType {
    case object Diff  extends CommitMediaType("application/vnd.github.v3.diff")
    case object Patch extends CommitMediaType("application/vnd.github.v3.patch")
    case object Sha   extends CommitMediaType("application/vnd.github.v3.sha")
  }

  sealed abstract class RepositoryContentMediaType(mediaType: String) extends MediaType(mediaType)
  object RepositoryContentMediaType {
    case object Html extends RepositoryContentMediaType("application/vnd.github.v3.html")
    case object Raw  extends RepositoryContentMediaType("application/vnd.github.v3.raw")
  }

  sealed abstract class GistMediaType(mediaType: String) extends MediaType(mediaType)
  object GistMediaType {
    case object Base64 extends GistMediaType("application/vnd.github.v3.base64")
    case object Raw    extends GistMediaType("application/vnd.github.v3.raw")
  }
}
