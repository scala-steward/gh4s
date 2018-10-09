package gh4s.interpreter.misc

import cats.effect.Sync
import gh4s.GithubClientConfig
import gh4s.algebra.misc._

object MiscInterpreter {
  def apply[F[_]: Sync](config: GithubClientConfig[F, _]): MiscAlgebra[F] =
    new MiscAlgebra[F] {
      override val codesOfConduct: CodesOfConductAlgebra[F]         = CodesOfConductInterpreter(config)
      override val emojis: EmojisAlgebra[F]                         = EmojisInterpreter(config)
      override val gitignoreTemplates: GitignoreTemplatesAlgebra[F] = GitignoreTemplateInterpreter(config)
      override val licenses: LicensesAlgebra[F]                     = LicensesInterpreter(config)
      override val meta: MetaAlgebra[F]                             = MetaInterpreter(config)
      override val rateLimit: RateLimitAlgebra[F]                   = RateLimitInterpreter(config)
    }
}
