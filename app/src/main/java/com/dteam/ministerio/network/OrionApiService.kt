package com.dteam.ministerio.network

import com.dteam.ministerio.entities.Usuario
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.Retrofit
import retrofit2.http.*

private const val IP = "192.168.0.11"
private const val BASE_URL = "http://${IP}:1026/v2/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface OrionApiService {

    // Documentación de la API ORION
    // https://telefonicaid.github.io/fiware-orion/api/v2/stable/

    @GET("entities?options=keyValues&type=Usuario")
    suspend fun getUsuarios(): List<Usuario>

    @GET("entities?options=keyValues&type=Usuario&q=rol:Responsable")
    suspend fun getUsuariosResponsables(): List<Usuario>

    @GET("entities/{id}?options=keyValues")
    suspend fun getUsuarioByUID(@Path("id") UID: String): Usuario

    @GET("entities?options=keyValues&type=Usuario")
    suspend fun getUsuarioByEmail(@Query("q") email: String): List<Usuario>

    @POST("entities?options=keyValues")
    suspend fun registrarUsuario(@Body usuario: Usuario)


}

object OrionApi {
    val retrofitService : OrionApiService by lazy {
        retrofit.create(OrionApiService::class.java)
    }

}