package com.example.adidharmatorutama.beautylicious;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ReserveRequest extends StringRequest {
    private static final String url = "http://sisfour.ddns.net/Reserve.php";
    private Map<String, String> params;

    public ReserveRequest(String email, String cabang, String layanan, String jadwal, Response.Listener<String> listener) {
        super(Method.POST, url, listener, null);
        params = new HashMap<>();
        params.put("email", email);
        params.put("cabang", cabang);
        params.put("layanan", layanan);
        params.put("jadwal", jadwal);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
