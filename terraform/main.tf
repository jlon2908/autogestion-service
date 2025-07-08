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

output "ecr_repository_url" {
  value = aws_ecr_repository.autogestion_service.repository_url
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
