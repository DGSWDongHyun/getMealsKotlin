package com.project.getmeals.network.server

import com.project.getmeals.network.api.ServerAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Server private constructor() {
    private val api: ServerAPI
    fun getApi(): ServerAPI {
        return api
    }

    companion object {
        var instance: Server? = null
            get() {
                if (field == null) field = Server()
                return field
            }
            private set
    }

    init {
        val server = Retrofit.Builder()
            .baseUrl("http://apis.data.go.kr/1360000/VilageFcstInfoService/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = server.create(ServerAPI::class.java)
    }
}