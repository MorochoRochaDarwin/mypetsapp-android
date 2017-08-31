package com.darwindeveloper.MyPetsApp.api;

import com.darwindeveloper.MyPetsApp.api.modelos.Cita;
import com.darwindeveloper.MyPetsApp.api.modelos.Establecimiento;
import com.darwindeveloper.MyPetsApp.api.modelos.Mascota;
import com.darwindeveloper.MyPetsApp.api.modelos.Provincia;
import com.darwindeveloper.MyPetsApp.api.responses.CheckTokenResponse;
import com.darwindeveloper.MyPetsApp.api.responses.ClientResponse;
import com.darwindeveloper.MyPetsApp.api.responses.DefaultResponse;
import com.darwindeveloper.MyPetsApp.api.responses.PasswordResetResponse;
import com.darwindeveloper.MyPetsApp.api.responses.UploadResponse;
import com.darwindeveloper.MyPetsApp.api.responses.UserResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Darwin Morocho on 18/06/2017.
 */

public interface WebService {


    @POST("api-login")
    @FormUrlEncoded
    Call<UserResponse> api_login(@Field("email") String email,
                                 @Field("password") String password,
                                 @Field("firebase_token") String firebase_token);

    @POST("password-reset")
    @FormUrlEncoded
    Call<PasswordResetResponse> password_reset(@Field("email") String email);


    @POST("check-token")
    @FormUrlEncoded
    Call<CheckTokenResponse> check_token(@Field("user_id") String user_id,
                                         @Field("api_token") String api_token);


    @POST("establecimientos")
    @FormUrlEncoded
    Call<List<Establecimiento>> establecimientos(@Field("user_id") String user_id);

    @POST("establecimientos_mascota")
    @FormUrlEncoded
    Call<List<Establecimiento>> establecimientos_mascota(@Field("mascota_id") String mascota_id);


    @POST("mascotas")
    @FormUrlEncoded
    Call<List<Mascota>> mascotas(@Field("user_id") String user_id);

    @POST("mascota")
    @FormUrlEncoded
    Call<Mascota> mascota(@Field("mascota_id") String mascota_id);


    @POST("cliente")
    @FormUrlEncoded
    Call<ClientResponse> cliente(@Field("user_id") String user_id,
                                 @Field("api_token") String api_token);


    @GET("provincias")
    Call<List<Provincia>> provincias();


    @POST("foto_cliente")
    @FormUrlEncoded
    Call<UploadResponse> subir_foto(@Field("user_id") String user_id,
                                    @Field("api_token") String api_token, @Field("url") String url);


    @POST("eventos")
    @FormUrlEncoded
    Call<List<Cita>> citas(@Field("user_id") String user_id,
                           @Field("api_token") String api_token);

    @POST("eventos_mascota")
    @FormUrlEncoded
    Call<List<Cita>> citas_mascota(@Field("mascota_id") String mascota_id);

    @POST("actualizar_mascota")
    @FormUrlEncoded
    Call<DefaultResponse> actualizar_mascota(@Field("mascota_id") String mascota_id,
                                             @Field("user_id") String user_id,
                                             @Field("nombre") String nombre,
                                             @Field("raza") String raza,
                                             @Field("genero") String sexo,
                                             @Field("nacimiento") String nacimiento,
                                             @Field("muerte") String muerte,
                                             @Field("color") String color,
                                             @Field("especie") String especie,
                                             @Field("microchip") String microchip,
                                             @Field("estado") String estado,
                                             @Field("esterilizado") String esterilizado,
                                             @Field("tamanio") String tamanio,
                                             @Field("alimentacion") String alimentacion,
                                             @Field("observaciones") String obs
    );


}
