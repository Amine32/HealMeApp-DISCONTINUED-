package ru.tsu.hits.healmeapp.misc;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CustomStringRequest extends StringRequest {

    private Map<String, String> params = null;
    private Map<String, String> headers = null;
    private String token;

    public CustomStringRequest(int method, String url, Map<String, String> params, String token, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.setParams(params);
        this.setToken(token);
    }

    public CustomStringRequest(int method, String url, String token, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.setToken(token);
    }

    private void setToken(String token) {
        this.token = token;
    }

    private void setParams(Map<String, String> params) {
        if (params == null) {
            this.params = new HashMap<String, String>();
        } else {
            this.params = params;
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (this.headers == null) {
            this.headers = new HashMap<String, String>();
        }
        //this.headers.put("x-rapidapi-key", token);
        return this.headers;
    }


    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        if (this.params != null) {
            return this.params;
        } else {
            return super.getParams();
        }
    }

}
