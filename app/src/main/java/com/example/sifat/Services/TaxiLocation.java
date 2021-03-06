package com.example.sifat.Services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.sifat.Controller.HttpConnection;
import com.example.sifat.Domain.TaxiDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.sifat.Utilities.CommonUtilities.*;

public class TaxiLocation extends IntentService{

    public ResultReceiver resultReceiver;
    String URL,header,result;
    private HttpConnection http;
    private ArrayList<TaxiDetail> taxiDetails= new ArrayList<>();

    public TaxiLocation() {
        super("TaxiLocation");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        http= new HttpConnection();
        header="taxi";
        result="";
        URL=TAXI_POSITION_ADDRESS;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        TaxiDetail taxiDetail;
        resultReceiver=intent.getParcelableExtra(TAXIDETAIL_RECIEVER);

        try {
            result = http.readUrl(URL);
            Log.i(LOG_TAG_TAXIPOSITIONSERVICE,result);
        } catch (IOException e) {
            Log.e(LOG_TAG_TAXIPOSITIONSERVICE,e.toString());
        }

        try {
            JSONObject rootJsonObject = new JSONObject(result);
            JSONArray jsonLocationArray = rootJsonObject.optJSONArray(header);
            Log.i(LOG_TAG_TAXIPOSITIONSERVICE,"Length: "+jsonLocationArray.length());

            for(int i=0;i<jsonLocationArray.length();i++)
            {
                JSONObject childObject= jsonLocationArray.getJSONObject(i);
                int driverId = childObject.getInt("driver_id");
                String drivername=childObject.getString("driver");
                String mobile = childObject.getString("mobile");
                double lat= childObject.getDouble("lat");
                double lng= childObject.getDouble("lng");
                float rating = (float) childObject.getDouble("rating");
                taxiDetail = new TaxiDetail(driverId, drivername, lat, lng, rating, mobile);
                taxiDetails.add(taxiDetail);
            }

            deliverResultToReceiver(SUCCESS_RESULT,taxiDetails);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        stopSelf();
    }

    private void deliverResultToReceiver(int resultCode, ArrayList<TaxiDetail> taxiInfo)
    {
        //Log.i(LOG_TAG_SERVICE,"Deliver -> "+message);
        Bundle bundle = new Bundle();
        bundle.putSerializable(RESULT_TAXIDETAIL_KEY, taxiInfo);
        //Log.i(LOG_TAG_TAXIPOSITIONSERVICE, "Sending -> " + taxiInfo.get(3).getDriverName() + " " + resultReceiver.getClass());
        Intent i = new Intent("taxi.position.information");
        i.putExtras(bundle);
        sendBroadcast(i);
        //resultReceiver.send(resultCode, bundle);
    }
}