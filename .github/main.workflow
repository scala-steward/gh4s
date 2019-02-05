workflow "Delete branch on PR merge" {
  on = "pull_request"
  resolves = ["Delete merged branch"]
}

action "Delete merged branch" {
  uses = "jessfraz/branch-cleanup-action@master"
  secrets = ["GITHUB_TOKEN"]
}
