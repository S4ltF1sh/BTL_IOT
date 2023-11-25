package com.example.btliot.common

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T> Gson.fromJson(json: String): T =
    fromJson(json, object : TypeToken<T>() {}.type)

inline fun <reified T> Gson.toJson(data: T): String =
    toJson(data)