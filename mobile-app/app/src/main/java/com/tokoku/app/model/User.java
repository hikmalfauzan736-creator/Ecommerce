package com.tokoku.app.model;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("email")
    public String email;

    @SerializedName("password")
    public String password;
}