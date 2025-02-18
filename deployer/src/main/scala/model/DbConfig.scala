package model

case class DbConfig
(
  host: String,
  port: Int,
  user: String,
  password: String,
  database: String
)
