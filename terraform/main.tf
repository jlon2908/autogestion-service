terraform {
  backend "s3" {
    bucket = "arka-dev-artifacts"
    key    = "terraform/state/terraform.tfstate"
    region = "us-east-1"
    encrypt = true
  }
}

# Terraform para recursos de autogestion-service

provider "aws" {
  region = "us-east-1"
}

# Repositorio ECR para la imagen Docker del microservicio
resource "aws_ecr_repository" "autogestion_service" {
  name = "autogestion-service"
  image_scanning_configuration {
    scan_on_push = true
  }
}

# Uso de bucket S3 existente (arka-dev-artifacts)
# No se crea el bucket, solo se referencia
output "s3_bucket_name" {
  value = "arka-dev-artifacts"
}

# Carpeta/prefijo recomendado para este microservicio: autogestion-service/
output "s3_prefix" {
  value = "autogestion-service/"
}

resource "aws_ecs_task_definition" "autogestion_service" {
  family                   = "autogestion-service-task"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = "512"
  memory                   = "1024"
  execution_role_arn       = aws_iam_role.ecs_task_execution_role.arn
  task_role_arn            = aws_iam_role.ecs_task_execution_role.arn

  container_definitions = jsonencode([
    {
      name      = "autogestion-service"
      image     = "${aws_ecr_repository.autogestion_service.repository_url}:latest"
      portMappings = [
        {
          containerPort = 8085
          hostPort      = 8085
        }
      ]
      environment = [
        { name = "DB_HOST", value = var.db_host },
        { name = "DB_JDBC_URL", value = var.db_jdbc_url },
        { name = "DB_PASSWORD", value = var.db_password },
        { name = "DB_USERNAME", value = var.db_username },
        { name = "JWT_SECRET", value = var.jwt_secret },
        { name = "SECURITY_GROUP_ID", value = var.security_group_id },
        { name = "TARGETGROUP", value = var.targetgroup },
        { name = "VPC_ID", value = var.vpc_id }
      ]
    }
  ])
}

resource "aws_ecs_service" "autogestion_service" {
  name            = "autogestion-service"
  cluster         = "arka-ecs-cluster"
  task_definition = aws_ecs_task_definition.autogestion_service.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = ["subnet-09ee219f4838cb06f", "subnet-057a322f484b9f962"]
    security_groups  = [var.security_group_id]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = var.targetgroup
    container_name   = "autogestion-service"
    container_port   = 8085
  }
  depends_on = [aws_ecr_repository.autogestion_service]
}

variable "db_host" {}
variable "db_jdbc_url" {}
variable "db_password" {}
variable "db_username" {}
variable "jwt_secret" {}
variable "security_group_id" {}
variable "targetgroup" {}
variable "vpc_id" {}

resource "aws_iam_role" "ecs_task_execution_role" {
  name = "ecsTaskExecutionRole-autogestion-service"
  assume_role_policy = data.aws_iam_policy_document.ecs_task_assume_role_policy.json
}

data "aws_iam_policy_document" "ecs_task_assume_role_policy" {
  statement {
    actions = ["sts:AssumeRole"]
    principals {
      type        = "Service"
      identifiers = ["ecs-tasks.amazonaws.com"]
    }
  }
}

resource "aws_iam_role_policy_attachment" "ecs_task_execution_role_policy" {
  role       = aws_iam_role.ecs_task_execution_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

