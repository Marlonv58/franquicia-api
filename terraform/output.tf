output "rds_endpoint" {
  description = "RDS endpoint database"
  value       = aws_db_instance.franquicia_mysql.address
}
output "public_ip" {
  value = aws_instance.franquicia_ec2.public_ip
}