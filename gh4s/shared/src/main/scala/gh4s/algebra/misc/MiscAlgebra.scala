package gh4s.algebra.misc

trait MiscAlgebra[F[_]] {
  def codesOfConduct: CodesOfConductAlgebra[F]
  def emojis: EmojisAlgebra[F]
  def gitignoreTemplates: GitignoreTemplatesAlgebra[F]
  def licenses: LicensesAlgebra[F]
  def meta: MetaAlgebra[F]
  def rateLimit: RateLimitAlgebra[F]
}
