resource "aws_db_subnet_group" "main" {
  name       = "${var.project_name}-db"
  subnet_ids = aws_subnet.private[*].id
}

resource "aws_db_instance" "main" {
  identifier_prefix         = "commerce-"
  engine                    = "postgres"
  instance_class            = var.db_instance_class
  allocated_storage         = 20
  max_allocated_storage     = 100
  db_name                   = "commerce"
  username                  = "commerce"
  manage_master_user_password = true
  multi_az                  = true
  storage_encrypted         = true
  publicly_accessible       = false
  db_subnet_group_name      = aws_db_subnet_group.main.name
  vpc_security_group_ids    = [aws_security_group.db.id]
  backup_retention_period   = 7
  auto_minor_version_upgrade = true
  deletion_protection       = var.deletion_protection
  skip_final_snapshot       = !var.deletion_protection
}
