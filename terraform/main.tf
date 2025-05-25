provider "aws" {
  region = "us-east-1"
}

variable "key_name" {
  description = "Nombre de la clave SSH en AWS EC2"
  default     = "franquicia-key"
}

resource "aws_security_group" "franquicia_sg" {
  name        = "franquicia-sg"
  description = "Permite tráfico HTTP y SSH"

  ingress {
    description = "Permitir acceso HTTP"
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Permitir SSH"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_instance" "franquicia_ec2" {
  ami           = "ami-0c02fb55956c7d316" # Amazon Linux 2 (x86_64)
  instance_type = "t2.micro"
  key_name      = var.key_name
  security_groups = [aws_security_group.franquicia_sg.name]

  user_data = <<-EOF
              #!/bin/bash
              yum update -y
              yum install docker git -y
              service docker start
              usermod -a -G docker ec2-user
              chkconfig docker on
              git clone https://github.com/Marlonv58/franquicia-api.git
              cd franquicia-api
              docker-compose up -d
              EOF

  tags = {
    Name = "FranquiciaApp"
  }
}

output "public_ip" {
  value = aws_instance.franquicia_ec2.public_ip
}