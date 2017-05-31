package com.sourcey.materiallogindemo;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kpk03_000 on 2017-05-31.
 */

public class LoginRequest  extends StringRequest{

    final static private String URL = "http://test.huy.kr/api/v1/user/signin.json";
    private Map<String, String> parameters;

    public LoginRequest(String userID, String userPassword, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userId", userID);
        parameters.put("userPassword", userPassword);
    }

    @Override
    public Map<String, String> getParams(){ return parameters ;}

}
