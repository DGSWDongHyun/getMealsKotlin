package com.project.getmeals.network.server

import com.project.getmeals.network.api.ServerAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Server {

    companion object {
        private val retrofitClient: Server = Server()

        fun getInstance(): Server {
            return retrofitClient
        }
    }

    fun buildRetrofit(): ServerAPI {
        val retrofit: Retrofit? = Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: ServerAPI = retrofit!!.create(ServerAPI :: class.java)
        return service
    }

}