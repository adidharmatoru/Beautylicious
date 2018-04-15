package com.example.adidharmatorutama.beautylicious;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class OrderRequest extends StringRequest {
    private static final String url = "http://sisfour.ddns.net/order.php";
    private Map<String, String> params;

    public OrderRequest(String email, String jumlah, Response.Listener<String> listener) {
        super(Method.POST, url, listener, null);
        params = new HashMap<>();
        params.put("email", email);
        params.put("jumlah", jumlah);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
