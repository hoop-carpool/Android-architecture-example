package com.hoopcarpool.archexample.core.network.login

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Login API.
 *
 * Link: https://bhagavadgita.io/apidocs/#/auth/post_auth_oauth_token
 */
interface LoginApi {

    @FormUrlEncoded
    @POST("/auth/oauth/token")
    suspend fun oauthGetToken(
        @Field("client_id") clientId: String = "fnDVuAzkFEgiLuJ4gGoCZ6SoGMXDddq5vFirh2Ty",
        @Field("client_secret") clientSecret: String = "eY2YgbmIAyMobTPVZb9cjoceu9LJMwIKDI7LfZqvQ3ANiApH0E",
        @Field("grant_type") grantType: String = "client_credentials",
        @Field("scope") scope: String = "verse chapter"
    ): Response<Auth>

    @JsonClass(generateAdapter = true)
    data class Auth(
        @Json(name = "access_token")
        val accessToken: String,
        @Json(name = "scope")
        val scope: String,
        @Json(name = "token_type")
        val tokenType: String
    )
}
