resource "aws_codecommit_repository" "customer-service" {
  repository_name = "customer-service-repository"
  description     = "Customer Service App Repository"
}
resource "aws_codecommit_repository" "customer-frontend-service" {
  repository_name = "customer-frontend-service-repository"
  description     = "Customer UI Backend Repository"
}
resource "aws_codecommit_repository" "customer-ui" {
  repository_name = "customer-ui-repository"
  description     = "Customer ui Repository"
}