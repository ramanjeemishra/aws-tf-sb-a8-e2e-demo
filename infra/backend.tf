terraform {
  backend "s3" {

    bucket         = "infra-for-scb-assignment"
    key            = "global/s3/terraform.tfstate"
    region = "ap-southeast-1"

    # Replace this with your DynamoDB table name!
    dynamodb_table = "infra-for-scb-assignment-locks"
    encrypt        = true


  }
}