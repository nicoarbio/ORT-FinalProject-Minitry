package com.dteam.ministerio.network

import com.dteam.ministerio.R
import com.dteam.ministerio.entities.Usuario
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.Retrofit
import retrofit2.http.*

//IP Publica Nico
private const val IP = "190.247.194.64"

private const val BASE_URL = "http://${IP}:1026/v2/"

// Documentación ejemplo Moshi en Retrofit
// https://developer.android.com/codelabs/basic-android-kotlin-training-getting-data-internet?continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fandroid-basics-kotlin-unit-4-pathway-2%23codelab-https%3A%2F%2Fdeveloper.android.com%2Fcodelabs%2Fbasic-android-kotlin-training-getting-data-internet#7
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// Documentación Retrofit
// https://square.github.io/retrofit/
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface OrionApiService {

    // Documentación de la API ORION
    // https://telefonicaid.github.io/fiware-orion/api/v2/stable/

    @GET("/version")
    suspend fun verificarConexion(): Response<Unit>

    @GET("entities?options=keyValues&type=Usuario&q=rol:Responsable;isEnabled:"+OrionApi.USER_ENABLED)
    suspend fun getUsuariosResponsables(): List<Usuario>

    @GET("entities/{id}?options=keyValues&type=Usuario&q=isEnabled:"+OrionApi.USER_ENABLED)
    suspend fun getUsuarioByUID(@Path("id") UID: String): Usuario

    @GET("entities?options=keyValues&type=Usuario")
    suspend fun getUsuarioByQuery(@Query("q") query: String): List<Usuario>

    @POST("entities?options=keyValues")
    suspend fun registrarUsuario(@Body usuario: Usuario)

    @PATCH("entities/{id}/attrs?options=keyValues")
    suspend fun actualizarUsuario(@Path("id") UID: String,
                                  @Body usuarioSinIdOtype: UsuarioPaylodOrion
                                    ) : Response<Unit>

}

object OrionApi {
    const val USER_ENABLED = "enabled"
    const val USER_DISABLED = "disabled"
    val retrofitService : OrionApiService by lazy {
        retrofit.create(OrionApiService::class.java)
    }

}