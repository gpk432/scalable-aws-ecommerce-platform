output "cloudfront_domain" {
  value = aws_cloudfront_distribution.api.domain_name
}

output "alb_dns_name" {
  value = aws_lb.main.dns_name
}

output "database_endpoint" {
  value     = aws_db_instance.main.address
  sensitive = true
}
