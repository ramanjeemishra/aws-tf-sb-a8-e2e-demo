locals {
  production_availability_zones = ["ap-southeast-1a", "ap-southeast-1b"]
}

provider "aws" {
  region  =  var.region
}

/*resource "aws_key_pair" "key" {
  key_name    =  "${var.environment}_key"
  public_key  =  file("${var.environment}_key.pub")
}*/

module "networking" {
  source                =  "./modules/networking"
  environment           =  var.environment
  vpc_cidr              =  "10.0.0.0/16"
  public_subnets_cidr   =  ["10.0.1.0/24", "10.0.2.0/24"]
  private_subnets_cidr  =  ["10.0.10.0/24", "10.0.20.0/24"]
  region                =  var.region
  availability_zones    =  local.production_availability_zones
  key_name              =  "${var.environment}_key"
}

module "rds" {
  source                =  "./modules/rds"
  environment           =  var.environment
  allocated_storage     =  "8"
  database_name         =  var.database_name
  database_username     =  var.database_username
  database_password     =  var.database_password
  subnet_ids            =  module.networking.private_subnets_id
  vpc_id                =  module.networking.vpc_id
  instance_class        =  "db.t2.micro"
}

module "ecs" {
  source                =  "./modules/ecs"
  environment           =  var.environment
  region=var.region
  vpc_id                =  module.networking.vpc_id
  availability_zones    =  local.production_availability_zones
  repository_name       =  "customerservice/production"
  subnets_ids           =  module.networking.private_subnets_id
  public_subnet_ids     =  module.networking.public_subnets_id
  security_groups_ids   =  [module.networking.security_groups_ids,module.rds.db_access_sg_id]
  database_endpoint     =  module.rds.rds_address
  database_name         =  var.database_name
  database_username     =  var.database_username
  database_password     =  var.database_password
  secret_key_base       =  var.secret_key_base
}

