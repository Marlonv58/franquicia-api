provider "aws" {
  region = "us-east-1"
}

variable "key_name" {
  description = "Name of SSH key in AWS EC2"
  default     = "franquicia-key"
}

resource "aws_security_group" "franquicia_sg" {
  name        = "franquicia-sg"
  description = "Allow HTTP and SSH traffic"

  ingress {
    description = "Allow HTTP acces"
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Allow SSH"
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

              curl -L "https://github.com/docker/compose/releases/download/v2.24.6/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
              chmod +x /usr/local/bin/docker-compose

              git clone https://github.com/Marlonv58/franquicia-api.git
              cd franquicia-api

              echo "version: '3.8'
              services:
                app:
                  build: .
                  container_name: franquicia_app
                  ports:
                    - \"8080:8080\"
                  environment:
                    SPRING_DATASOURCE_URL: jdbc:mysql://${aws_db_instance.franquicia_mysql.address}:3306/franquicia_db
                    SPRING_DATASOURCE_USERNAME: admin
                    SPRING_DATASOURCE_PASSWORD: franquicia123
                    SPRING_JPA_HIBERNATE_DDL_AUTO: update
                  restart: always
              " > docker-compose.yml

              docker-compose up -d
              EOF

  tags = {
    Name = "FranquiciaApp"
  }
}