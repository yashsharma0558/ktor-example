package com.dev.routes

import com.dev.data.createEmployeeOrUpdateEmployeeForID
import com.dev.data.deleteEmployeeForID
import com.dev.data.getEmployeeForID
import com.dev.data.model.Employee
import com.dev.data.requests.DeleteEmployeeRequests
import com.dev.data.requests.EmployeeRequests
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.employeeRoute() {
    route("/get-employee") {
        get {
            val employeeId = call.receive<EmployeeRequests>().id
            val employee = getEmployeeForID(employeeId)
            employee?.let {
                call.respond(
                    HttpStatusCode.OK,
                    it
                )
            } ?: call.respond(
                HttpStatusCode.OK,
                "There is no employee with this id"
            )

        }
    }

    route("/create-update-employee") {
        post {
            val request = try{
                call.receive<Employee>()
            }
            catch (e: ContentTransformationException){
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            if(createEmployeeOrUpdateEmployeeForID(request)){
                call.respond(
                    HttpStatusCode.OK,
                    "Employee successfully created / updated"
                )
            }
            else{
                call.respond(HttpStatusCode.Conflict)
            }
        }
    }

    route("/delete-employee") {
        post {
            val request = try {
                call.receive<DeleteEmployeeRequests>()
            } catch (e : ContentTransformationException){
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            if(deleteEmployeeForID(request.id)){
                call.respond(
                    HttpStatusCode.OK,
                    "Employee deleted successfully"
                )
            }
            else{
                call.respond(
                    HttpStatusCode.OK,
                    "employee not found"
                )
            }
        }
    }


}