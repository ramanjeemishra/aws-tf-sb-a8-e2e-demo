provider "aws" {
  region = var.region
}

#ramanjeemishra.me.uk

locals {
  bucket_name =  "${var.environment}-static-site-bucket"
}


resource "aws_s3_bucket" "static_site" {
  bucket  =  local.bucket_name
  acl     =  "public-read"
  policy  =  templatefile("policy.json",{bucket_name = local.bucket_name})

  website {
    index_document  =  "index.html"
    error_document  =  "error.html"
    routing_rules   =  file("routing_rules.json")
  }


  tags  =  {
    Name         =  local.bucket_name
    Environment  =  var.environment
  }
}