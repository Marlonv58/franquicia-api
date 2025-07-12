variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "us-east-1"
}

data "aws_caller_identity" "current" {}

variable "github_owner" {
  description = "GitHub repository owner"
  type        = string
  default     = "Marlonv58"
}

variable "github_repo" {
  description = "GitHub repository name"
  type        = string
  default     = "franquicia-api"
}

variable "github_branch" {
  description = "GitHub branch to build"
  type        = string
  default     = "main"
}

variable "github_oauth_token" {
  description = "GitHub OAuth token with repo access"
  type        = string
  sensitive   = true
  default     = "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"
}
