output "alb_dns_name" {
  value  =  module.ecs.alb_dns_name
}

# For debugging
output "subnet_ids" {
  value  =  module.networking.private_subnets_id
}

output "route53_domain" {
  value  =  aws_route53_record.api-prod.fqdn
}