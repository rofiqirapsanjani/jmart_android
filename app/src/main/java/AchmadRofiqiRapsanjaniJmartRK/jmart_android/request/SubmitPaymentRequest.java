package AchmadRofiqiRapsanjaniJmartRK.jmart_android.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SubmitPaymentRequest extends StringRequest {
    private Map<String, String> params;

    public SubmitPaymentRequest(String receipt, String url, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST, url, listener, errorListener);
        params = new HashMap<>();
        params.put("receipt", receipt);
    }

    public Map<String, String> getParams(){
        return params;
    }
}
