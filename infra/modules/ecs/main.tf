resource "aws_cloudwatch_log_group" "customerservice" {
  name = "customerservice"

  tags = {
    Environment = var.environment
    Application = "customerservice"
  }
}


resource "aws_ecr_repository" "customerservice_app" {
  name = var.repository_name
}


resource "aws_ecs_cluster" "cluster" {
  name = "${var.environment}-ecs-cluster"
}


data "template_file" "api_task" {
  template = file("${path.module}/tasks/api_task_definition.json")

  vars = {
    image               =    aws_ecr_repository.customerservice_app.repository_url
    secret_key_base     =    var.secret_key_base
    aws_region          =    var.region
    database_server     =    var.database_endpoint
    database_user       =    var.database_username
    database_password   =    var.database_password
    log_group           =    aws_cloudwatch_log_group.customerservice.name
  }
}

resource "aws_ecs_task_definition" "api" {
  family                   =    "${var.environment}_api"
  container_definitions    =    data.template_file.api_task.rendered
  requires_compatibilities =    ["FARGATE"]
  network_mode             =    "awsvpc"
  cpu                      =    "1024"
  memory                   =    "2048"
  execution_role_arn       =    aws_iam_role.ecs_execution_role.arn
  task_role_arn            =    aws_iam_role.ecs_execution_role.arn
}

resource "random_id" "target_group_sufix" {
  byte_length = 2
}

/*resource "aws_alb_target_group" "ecs_app_target_group" {
  name        = "${var.ecs_service_name}-TG"
  port        = "${var.docker_container_port}"
  protocol    = "HTTP"
  vpc_id      = "${data.terraform_remote_state.platform.vpc_id}"
  target_type = "ip"

  health_check {
    path                = "/actuator/health"
    protocol            = "HTTP"
    matcher             = "200"
    interval            = 60
    timeout             = 30
    unhealthy_threshold = "3"
    healthy_threshold   = "3"
  }

  tags {
    Name = "${var.ecs_service_name}-TG"
  }
}*/

resource "aws_alb_target_group" "alb_target_group" {
  name               =    "${var.environment}-alb-target-group-NEW-${random_id.target_group_sufix.hex}"
  port               =    8080
  protocol           =    "HTTP"
  vpc_id             =    var.vpc_id
  target_type        =    "ip"

  health_check {
    path                = "/actuator/health"
    protocol            = "HTTP"
    matcher             = "200"
    interval            = 60
    timeout             = 30
    unhealthy_threshold = "3"
    healthy_threshold   = "3"
  }

  lifecycle {
    create_before_destroy = true
  }
}


resource "aws_security_group" "api_inbound_sg" {
  name               =    "${var.environment}-api-inbound-sg"
  description        =    "Allow HTTP from Anywhere into ALB"
  vpc_id             =    var.vpc_id

  ingress {
    from_port        =     80
    to_port          =     80
    protocol         =     "tcp"
    cidr_blocks      =     ["0.0.0.0/0"]
  }

  ingress {
    from_port        =    8
    to_port          =    0
    protocol         =    "icmp"
    cidr_blocks      =    ["0.0.0.0/0"]
  }

  egress {
    from_port        =    0
    to_port          =    0
    protocol         =    "-1"
    cidr_blocks      =    ["0.0.0.0/0"]
  }

  tags = {
    Name = "${var.environment}-api-inbound-sg"
  }
}

data "aws_elb_service_account" "main" {}

locals {
  access_log_bucket_name = "${var.environment}-alb-customerservice-logs"
}

data "aws_iam_policy_document" "alb_access_log_policy" {
  statement {
    effect     =  "Allow"
    resources  =  [  "arn:aws:s3:::${local.access_log_bucket_name}",
                     "arn:aws:s3:::${local.access_log_bucket_name}/*"]
    actions    =  ["s3:PutObject"]

    principals {
      type         =  "AWS"
      identifiers  =  [data.aws_elb_service_account.main.arn]
    }
  }
}


resource "aws_s3_bucket" "alb_access_log" {
  bucket         =  local.access_log_bucket_name
  acl            =  "log-delivery-write"
  force_destroy  =  true
  policy         =  data.aws_iam_policy_document.alb_access_log_policy.json
}

resource "aws_alb" "alb_customerservice" {
  name             =  "${var.environment}-alb-customerservice"
  subnets          =  var.public_subnet_ids
  security_groups  =  concat(var.security_groups_ids, aws_security_group.api_inbound_sg.*.id)

  access_logs {
    bucket   =  aws_s3_bucket.alb_access_log.bucket
    enabled  =  true
    prefix   =  "access"
  }

  tags = {
    Name         =  "${var.environment}-alb-customerservice"
    Environment  =  var.environment
  }
}

resource "aws_alb_listener" "customerservice" {
  load_balancer_arn  =  aws_alb.alb_customerservice.arn
  port               =  "80"
  protocol           =  "HTTP"
  depends_on         =  ["aws_alb_target_group.alb_target_group"]

  default_action {
    target_group_arn  =  aws_alb_target_group.alb_target_group.arn
    type              =  "forward"
  }
}

data "aws_iam_policy_document" "ecs_service_role" {
  statement {
    effect          =  "Allow"
    actions         =  ["sts:AssumeRole"]
    principals {
      type          =  "Service"
      identifiers   =  ["ecs.amazonaws.com"]
    }
  }
}

resource "aws_iam_role" "ecs_role" {
  name               =  "ecs_role"
  assume_role_policy =  data.aws_iam_policy_document.ecs_service_role.json
}

data "aws_iam_policy_document" "ecs_service_policy" {
  statement {
    effect    =  "Allow"
    resources =  ["*"]
    actions   =  [
      "elasticloadbalancing:Describe*",
      "elasticloadbalancing:DeregisterInstancesFromLoadBalancer",
      "elasticloadbalancing:RegisterInstancesWithLoadBalancer",
      "ec2:Describe*",
      "ec2:AuthorizeSecurityGroupIngress"
    ]
  }
}


resource "aws_iam_role_policy" "ecs_service_role_policy" {
  name     =  "ecs_service_role_policy"
  #policy  =  "${file("${path.module}/policies/ecs-service-role.json")}"
  policy   =  data.aws_iam_policy_document.ecs_service_policy.json
  role     =  aws_iam_role.ecs_role.id
}


resource "aws_iam_role" "ecs_execution_role" {
  name               = "ecs_task_execution_role"
  assume_role_policy = file("${path.module}/policies/ecs-task-execution-role.json")
}
resource "aws_iam_role_policy" "ecs_execution_role_policy" {
  name    =  "ecs_execution_role_policy"
  policy  =  file("${path.module}/policies/ecs-execution-role-policy.json")
  role    =  aws_iam_role.ecs_execution_role.id
}

resource "aws_security_group" "ecs_service" {
  vpc_id       =  var.vpc_id
  name         =  "${var.environment}-ecs-service-sg"
  description  =  "Allow egress from container"

  egress {
    from_port    =  0
    to_port      =  0
    protocol     =  "-1"
    cidr_blocks  =  ["0.0.0.0/0"]
  }

  ingress {
    from_port    =  8
    to_port      =  0
    protocol     =  "icmp"
    cidr_blocks  =  ["0.0.0.0/0"]
  }

  tags = {
    Name         =  "${var.environment}-ecs-service-sg"
    Environment  =  var.environment
  }
}


data "aws_ecs_task_definition" "api" {
  task_definition  =  aws_ecs_task_definition.api.family
  depends_on       =  [ "aws_ecs_task_definition.api" ]
}

resource "aws_ecs_service" "api" {
  name                               =  "${var.environment}-api"
  task_definition                    =  "${aws_ecs_task_definition.api.family}:${max("${aws_ecs_task_definition.api.revision}", "${data.aws_ecs_task_definition.api.revision}")}"
  desired_count                      =  1
  launch_type                        =  "FARGATE"
  cluster                            =  aws_ecs_cluster.cluster.id
  depends_on                         =  ["aws_iam_role_policy.ecs_service_role_policy"]
  health_check_grace_period_seconds  =  180

  network_configuration {
    security_groups  =  concat(var.security_groups_ids, aws_security_group.ecs_service.*.id)
    subnets          =  var.subnets_ids
  }

  load_balancer {
    target_group_arn  =  aws_alb_target_group.alb_target_group.arn
    container_name    =  "api"
    container_port    =  "8080"
  }

}


resource "aws_iam_role" "ecs_autoscale_role" {
  name                =  "${var.environment}_ecs_autoscale_role"
  assume_role_policy  =  file("${path.module}/policies/ecs-autoscale-role.json")
}
resource "aws_iam_role_policy" "ecs_autoscale_role_policy" {
  name    =  "ecs_autoscale_role_policy"
  policy  =  file("${path.module}/policies/ecs-autoscale-role-policy.json")
  role    =  aws_iam_role.ecs_autoscale_role.id
}

resource "aws_appautoscaling_target" "target" {
  service_namespace   =  "ecs"
  resource_id         =  "service/${aws_ecs_cluster.cluster.name}/${aws_ecs_service.api.name}"
  scalable_dimension  =  "ecs:service:DesiredCount"
  role_arn            =  aws_iam_role.ecs_autoscale_role.arn
  min_capacity        =  1
  max_capacity        =  1
}

resource "aws_appautoscaling_policy" "up" {
  name                =  "${var.environment}_scale_up"
  service_namespace   =  "ecs"
  resource_id         =  "service/${aws_ecs_cluster.cluster.name}/${aws_ecs_service.api.name}"
  scalable_dimension  =  "ecs:service:DesiredCount"


  step_scaling_policy_configuration {
    adjustment_type          =  "ChangeInCapacity"
    cooldown                 =  60
    metric_aggregation_type  =  "Maximum"

    step_adjustment {
      metric_interval_lower_bound  =  0
      scaling_adjustment           =  1
    }
  }

  depends_on = ["aws_appautoscaling_target.target"]
}

resource "aws_appautoscaling_policy" "down" {
  name                    =    "${var.environment}_scale_down"
  service_namespace       =    "ecs"
  resource_id             =    "service/${aws_ecs_cluster.cluster.name}/${aws_ecs_service.api.name}"
  scalable_dimension      =    "ecs:service:DesiredCount"

  step_scaling_policy_configuration {
    adjustment_type         =   "ChangeInCapacity"
    cooldown                =   60
    metric_aggregation_type =   "Maximum"

    step_adjustment {
      metric_interval_lower_bound =   0
      scaling_adjustment          =   -1
    }
  }

  depends_on = ["aws_appautoscaling_target.target"]
}


resource "aws_cloudwatch_metric_alarm" "service_cpu_high" {
  alarm_name          =   "${var.environment}_customerservice_api_cpu_utilization_high"
  comparison_operator =   "GreaterThanOrEqualToThreshold"
  evaluation_periods  =   "2"
  metric_name         =   "CPUUtilization"
  namespace           =   "AWS/ECS"
  period              =   "60"
  statistic           =   "Maximum"
  threshold           =   "85"

  dimensions = {
    ClusterName = aws_ecs_cluster.cluster.name
    ServiceName = aws_ecs_service.api.name
  }

  alarm_actions       =    [aws_appautoscaling_policy.up.arn]
  ok_actions          =    [aws_appautoscaling_policy.down.arn]
}
