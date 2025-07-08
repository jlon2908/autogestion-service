resource "aws_ecs_task_definition" "autogestion" {
  family                   = "autogestion-task"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = "512"
  memory                   = "1024"
  execution_role_arn       = aws_iam_role.ecs_task_execution_role.arn
  container_definitions    = jsonencode([
    {
      name      = "autogestion-service"
      image     = "<ECR_REPO_URL>:latest" # Reemplaza esto al hacer el deploy
      portMappings = [
        {
          containerPort = 8085
          protocol      = "tcp"
        }
      ]
      environment = [
        # Puedes agregar variables no sensibles aquí si lo necesitas
      ]
      secrets = [
        { name = "DB_HOST", valueFrom = "arn:aws:secretsmanager:us-east-1:148761677807:secret:DB_HOST" },
        { name = "DB_JDBC_URL", valueFrom = "arn:aws:secretsmanager:us-east-1:148761677807:secret:DB_JDBC_URL" },
        { name = "DB_PASSWORD", valueFrom = "arn:aws:secretsmanager:us-east-1:148761677807:secret:DB_PASSWORD" },
        { name = "DB_USERNAME", valueFrom = "arn:aws:secretsmanager:us-east-1:148761677807:secret:DB_USERNAME" },
        { name = "JWT_SECRET", valueFrom = "arn:aws:secretsmanager:us-east-1:148761677807:secret:JWT_SECRET" }
      ]
    }
  ])
}

