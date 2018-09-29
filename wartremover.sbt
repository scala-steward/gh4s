wartremoverErrors in (Compile, compile) := Warts.allBut(
  Wart.DefaultArguments,
  Wart.ExplicitImplicitTypes,
  Wart.ImplicitParameter,
  Wart.Nothing,
  Wart.PublicInference,
)

wartremoverErrors in (Test, compile) := (wartremoverErrors in (Compile, compile)).value.diff(
  Seq(
    Wart.Any,
    Wart.NonUnitStatements
  )
)
