package com.example.myapplication.models;

import java.io.Serializable;


public class AuthTokenResponse implements Serializable{
    private String token;

    public String getToken(){
        return token;
    }
}
