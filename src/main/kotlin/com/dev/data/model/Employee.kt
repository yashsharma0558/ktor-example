package com.dev.data.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

data class Employee(
    val name: String,
    val surname: String,
    val year: String,
    @BsonId
    var id: String = ObjectId().toString()
)
