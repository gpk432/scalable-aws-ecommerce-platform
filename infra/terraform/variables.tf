variable "aws_region" {
  type    = string
  default = "us-east-2"
}

variable "project_name" {
  type    = string
  default = "portfolio-commerce"
}

variable "container_image" {
  type        = string
  description = "Immutable ECR image URI for commerce-api"
}

variable "desired_count" {
  type    = number
  default = 2
}

variable "db_instance_class" {
  type    = string
  default = "db.t4g.micro"
}

variable "deletion_protection" {
  type    = bool
  default = false
}
