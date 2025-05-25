
resource "aws_db_subnet_group" "franquicia_db_subnet_group" {
  name       = "franquicia-db-subnet-group"
  subnet_ids = data.aws_subnets.default.ids

  tags = {
    Name = "Franquicia DB Subnet Group"
  }
}

resource "aws_security_group" "franquicia_db_sg" {
  name        = "franquicia-db-sg"
  description = "Permite acceso MySQL desde EC2"

  ingress {
    description = "Permitir acceso MySQL desde EC2"
    from_port   = 3306
    to_port     = 3306
    protocol    = "tcp"
    security_groups = [aws_security_group.franquicia_sg.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_db_instance" "franquicia_mysql" {
  identifier         = "franquicia-db"
  engine             = "mysql"
  instance_class     = "db.t3.micro"
  allocated_storage  = 20
  username           = "admin"
  password           = "franquicia123"
  db_name            = "franquicia_db"
  port               = 3306
  publicly_accessible = true

  vpc_security_group_ids = [aws_security_group.franquicia_db_sg.id]
  db_subnet_group_name   = aws_db_subnet_group.franquicia_db_subnet_group.name

  skip_final_snapshot = true

  tags = {
    Name = "Franquicia RDS MySQL"
  }
}

data "aws_subnets" "default" {
  filter {
    name   = "default-for-az"
    values = ["true"]
  }
}