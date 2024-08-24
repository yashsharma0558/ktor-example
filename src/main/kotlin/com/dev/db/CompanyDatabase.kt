package com.dev.db

import com.dev.data.model.Employee
import com.mongodb.client.model.Filters
import org.bson.types.ObjectId
import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.bson.Document


private val uri = "mongodb+srv://yashsharma0558:${System.getenv("MONGODB_ATLAS_PASSWORD")}@cluster0.fqpfwvm.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"

private val mongoClient = MongoClient.create(uri)
private val database = mongoClient.getDatabase("CompanyDatabase")
// Get a collection of documents of type Movie
private val employees = database.getCollection<Employee>("Employees")

suspend fun getEmployeeForID(id: String): Employee? {
    val employee = employees.find(eq("_id", id)).firstOrNull()
    return employee
}



suspend fun createEmployeeOrUpdateEmployeeForID(employee: Employee): Boolean {

    val filter = eq("id", employee.id)
    val existingEmployee = employees.find(filter).firstOrNull()
    return if (existingEmployee != null) {
        val updateResult = employees.updateOne(
            filter,
            Document("\$set", Employee(employee.name, employee.surname, employee.year, employee.id))
        )
        updateResult.wasAcknowledged()
    } else {
        employee.id = ObjectId().toString()
        val newEmployee = Employee(employee.name, employee.surname, employee.year, employee.id)
        employees.insertOne(newEmployee).wasAcknowledged()
    }

}

suspend fun deleteEmployeeForID(employeeId: String): Boolean {
    val filter = eq("_id", employeeId)
    val deleteResult = employees.deleteOne(filter)
    return deleteResult.deletedCount > 0
}

