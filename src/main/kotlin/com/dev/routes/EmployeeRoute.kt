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
    route("/get-employee/{id}") {
        get {
            val employeeId = call.parameters["id"].toString()
//            val employeeId = call.receive<String>()
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

    route("/delete-employee/{id}") {
        delete {
            val employeeId = call.parameters["id"].toString()
            if (employeeId == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing or malformed ID")
                return@delete
            }
//            val request = try {
//                employeeId = call.parameters["id"]
//            } catch (e : ContentTransformationException){
//                call.respond(HttpStatusCode.BadRequest)
//                return@post
//            }
            if(deleteEmployeeForID(employeeId)){
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