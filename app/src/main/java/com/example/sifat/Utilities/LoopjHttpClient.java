package com.example.sifat.Utilities;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

import static com.example.sifat.Utilities.CommonUtilities.*;

/**
 * Created by sifat on 11/3/2015.
 */

public class LoopjHttpClient {
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }

    public static void post(String url, RequestParams requestParams, AsyncHttpResponseHandler responseHandler) {
        client.post(url, requestParams, responseHandler);
    }

    public static void debugLoopJ(String TAG, String methodName,String url, RequestParams requestParams, byte[] response, Header[] headers, int statusCode, Throwable t,Context context) {

        Log.i(LOG_TAG_SIGNUP, client.getUrlWithQueryString(false, url, requestParams));

        if (headers != null) {
            Log.e(LOG_TAG_SIGNUP, methodName);
            Log.d(LOG_TAG_SIGNUP, "Return Headers:");
            for (Header h : headers) {
                String _h = String.format(Locale.US, "%s : %s", h.getName(), h.getValue());
                Log.i(LOG_TAG_SIGNUP, _h);
            }

            if (t != null) {
                Log.i(LOG_TAG_SIGNUP, "Throwable:" + t);
            }

            Log.e(TAG, "StatusCode: " + statusCode);

            if (response != null) {
                Log.i(LOG_TAG_SIGNUP, "Response: " + new String(response));
                Toast.makeText(context,new String(response),Toast.LENGTH_SHORT).show();
            }

        }
    }
}
