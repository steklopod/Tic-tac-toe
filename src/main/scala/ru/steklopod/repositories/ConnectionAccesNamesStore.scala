package ru.steklopod.repositories

import ru.steklopod.repositories.GameDb._

object ConnectionAccesNamesStore {
  val URL_POSTGRES = "jdbc:postgresql://localhost:5432/home?currentSchema=" + SHEMA_NAME
  val DRIVER_POSTGRES = "org.postgresql.Driver"
  val LOGIN_POSTGRES = "postgres"
  val PSWRD_POSTGRES = "postgres"

  val URL_MARIA = "jdbc:mariadb://127.0.0.1:3306/test" //+ SHEMA_NAME
  val DRIVER_MARIA_DB = "org.mariadb.jdbc.Driver"
  var LOGIN_MARIA = "root"
  val PSWRD_MARIA = "root"

  val URL_H2_IN_MEMORY = "jdbc:h2:mem:" + SHEMA_NAME + ADDITIONAL_ARGUMENTS
  val ADDITIONAL_ARGUMENTS = ";MODE=MYSQL;DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1;"
  val DRIVER_H2_IN_MEMORY = "org.h2.Driver"
  val LOGIN_H2_IN_MEMORY = ""
  val PSWRD_H2_IN_MEMORY = ""
}
