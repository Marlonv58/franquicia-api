output "rds_endpoint" {
  description = "Endpoint de la base de datos RDS"
  value       = aws_db_instance.franquicia_mysql.address
}
output "public_ip" {
  value = aws_instance.franquicia_ec2.public_ip
}