
provider "aws" {
  region = var.aws_region
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

resource "aws_iam_role" "ec2_role" {
  name = "franquicia-ec2-role"
  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [{
      Effect = "Allow",
      Principal = { Service = "ec2.amazonaws.com" },
      Action = "sts:AssumeRole"
    }]
  })
}

resource "aws_iam_role_policy_attachment" "ec2_ecr_readonly" {
  role       = aws_iam_role.ec2_role.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryReadOnly"
}

resource "aws_iam_instance_profile" "ec2_profile" {
  name = "franquicia-ec2-profile"
  role = aws_iam_role.ec2_role.name
}

resource "aws_instance" "franquicia_ec2" {
  ami                    = "ami-0c02fb55956c7d316" # Amazon Linux 2 (x86_64)
  instance_type          = "t2.micro"
  key_name               = var.key_name
  vpc_security_group_ids = [aws_security_group.franquicia_sg.id]
  iam_instance_profile    = aws_iam_instance_profile.ec2_profile.name

  user_data = <<-EOF
              #!/bin/bash
              yum update -y
              yum install docker -y
              service docker start
              usermod -a -G docker ec2-user
              chkconfig docker on

              # Login to ECR and run container
              aws ecr get-login-password --region ${var.aws_region} \
                | docker login --username AWS --password-stdin ${data.aws_caller_identity.current.account_id}.dkr.ecr.${var.aws_region}.amazonaws.com
              docker pull ${aws_ecr_repository.franquicia.repository_url}:latest
              docker run -d --name franquicia_app -p 8080:8080 ${aws_ecr_repository.franquicia.repository_url}:latest
EOF

  tags = {
    Name = "FranquiciaApp"
  }
}