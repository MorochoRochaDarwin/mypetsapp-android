package com.darwindeveloper.MyPetsApp.api;

import com.darwindeveloper.MyPetsApp.api.modelos.Cita;
import com.darwindeveloper.MyPetsApp.api.modelos.Especies;
import com.darwindeveloper.MyPetsApp.api.modelos.Establecimiento;
import com.darwindeveloper.MyPetsApp.api.modelos.Mascota;
import com.darwindeveloper.MyPetsApp.api.modelos.Provincia;
import com.darwindeveloper.MyPetsApp.api.responses.CheckTokenResponse;
import com.darwindeveloper.MyPetsApp.api.responses.ClientResponse;
import com.darwindeveloper.MyPetsApp.api.responses.DefaultResponse;
import com.darwindeveloper.MyPetsApp.api.responses.PasswordResetResponse;
import com.darwindeveloper.MyPetsApp.api.responses.QRResponse;
import com.darwindeveloper.MyPetsApp.api.responses.UploadResponse;
import com.darwindeveloper.MyPetsApp.api.responses.UserResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Darwin Morocho on 18/06/2017.
 */

public interface WebService {


    @POST("api-login")
    @FormUrlEncoded
    Call<UserResponse> api_login(@Field("email") String email,
                                 @Field("password") String password,
                                 @Field("est_id") String est_id,
                                 @Field("firebase_token") String firebase_token);


    @POST("login-qr")
    @FormUrlEncoded
    Call<QRResponse> loginQR(@Field("user_id") String email,
                             @Field("est_id") String password,
                             @Field("firebase_token") String firebase_token);

    @POST("password")
    @FormUrlEncoded
    Call<DefaultResponse> change_password(@Field("user_id") String user_id,
                                          @Field("api_token") String api_token,
                                          @Field("pass") String password,
                                          @Field("npass") String npassword);

    @POST("password-reset")
    @FormUrlEncoded
    Call<PasswordResetResponse> password_reset(@Field("email") String email);


    @POST("check-token")
    @FormUrlEncoded
    Call<CheckTokenResponse> check_token(@Field("user_id") String user_id, @Field("est_id") String est_id,
                                         @Field("api_token") String api_token);


    @POST("establecimientos")
    @FormUrlEncoded
    Call<List<Establecimiento>> establecimientos(@Field("user_id") String user_id);

    @POST("getestablecimiento")
    @FormUrlEncoded
    Call<Establecimiento> establecimiento(@Field("user_id") String user_id, @Field("est_id") String est_id);

    @POST("establecimientos_mascota")
    @FormUrlEncoded
    Call<List<Establecimiento>> establecimientos_mascota(@Field("mascota_id") String mascota_id);

    @POST("establecimiento_mascotas")
    @FormUrlEncoded
    Call<List<Mascota>> mascotas_establecimiento(@Field("user_id") String user_id, @Field("est_id") String est_id);


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

    @POST("datoscliente")
    @FormUrlEncoded
    Call<ClientResponse> datoscliente(@Field("user_id") String user_id,
                                      @Field("api_token") String api_token);


    @GET("provincias")
    Call<List<Provincia>> provincias();

    @GET("especies")
    Call<Especies> especies();


    @GET("sitios/{categoria}")
    Call<List<Provincia>> sitios(@Path("categoria") String categoria);


    @Multipart
    @POST("foto_cliente")
    Call<UploadResponse> subir_foto(@Part("user_id") RequestBody user_id,
                                    @Part("api_token") RequestBody uapi_token, @Part MultipartBody.Part image);



    @POST("insertar_mascota")
    @FormUrlEncoded
    Call<DefaultResponse> nueva_mascota(
            @Field("user_id") String user_id,
            @Field("est_id") String est_id,
            @Field("nombre") String nombre,
            @Field("raza") String raza,
            @Field("genero") String sexo,
            @Field("nacimiento") String nacimiento,
            @Field("color") String color,
            @Field("especie") String especie,
            @Field("microchip") String microchip,
            @Field("esterilizado") String esterilizado,
            @Field("tamanio") String tamanio,
            @Field("alimentacion") String alimentacion,
            @Field("observaciones") String obs);


    @POST("eventos")
    @FormUrlEncoded
    Call<List<Cita>> citas(@Field("user_id") String user_id,
                           @Field("api_token") String api_token);

    @POST("eventos_establecimiento")
    @FormUrlEncoded
    Call<List<Cita>> citas_est(@Field("user_id") String user_id,
                               @Field("est_id") String est_id);

    @POST("eventos_mascota")
    @FormUrlEncoded
    Call<List<Cita>> citas_mascota(@Field("mascota_id") String mascota_id, @Field("est_id") String est_id);


    @POST("agendar_cita")
    @FormUrlEncoded
    Call<DefaultResponse> agendar_cita(@Field("user_id") String user_id,
                                       @Field("api_token") String api_token,
                                       @Field("mascota_id") String mascota_id,
                                       @Field("est_id") String est_id,
                                       @Field("motivo") String motivo,
                                       @Field("hora") String hora,
                                       @Field("fecha") String fecha,
                                       @Field("obs") String obs
    );


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


    @POST("actualizar_cliente")
    @FormUrlEncoded
    Call<DefaultResponse> actualizar_cliente(@Field("api_token") String api_token,
                                             @Field("user_id") String user_id,
                                             @Field("nombres") String nombres,
                                             @Field("apellidos") String apellidos,
                                             @Field("cedula") String cedula,
                                             @Field("email") String email,
                                             @Field("genero") String genero,
                                             @Field("nacimiento") String nacimiento,
                                             @Field("nacionalidad") String nacionalidad,
                                             @Field("celular") String celular,
                                             @Field("telefono") String telefono,
                                             @Field("nivel") String nivel,
                                             @Field("direccion") String direccion,
                                             @Field("provincia") String provincia,
                                             @Field("ciudad") String ciudad,
                                             @Field("sector") String sector

    );


    @Multipart
    @POST("actualizar_foto_mascota")
    Call<UploadResponse> actualizar_foto_mascota(@Part("user_id") RequestBody user_id, @Part("mascota_id") RequestBody mascota_id, @Part MultipartBody.Part image);

}
