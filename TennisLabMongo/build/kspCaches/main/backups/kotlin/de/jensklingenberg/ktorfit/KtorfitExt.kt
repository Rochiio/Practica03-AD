// Generated by Ktorfit
package de.jensklingenberg.ktorfit

import service.api.createApiTasks
import service.api.createApiUsers

public inline fun <reified T> Ktorfit.create(): T = when(T::class){
  service.api.ApiTasks::class ->{
      this.createApiTasks() as T
      }
      service.api.ApiUsers::class ->{
      this.createApiUsers() as T
      }

  else ->{
  throw IllegalArgumentException("Could not find any Ktorfit annotations in class"+
      T::class.qualifiedName  )
  }
}