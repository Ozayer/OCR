package com.example.thesis_lab_02.ocr_try;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;
import com.example.android.imagetotext.R;

public class ImageActivity extends AppCompatActivity {
    String selectedImagePath;
    TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        output = (TextView) findViewById(R.id.output);
        SharedPreferences sharedPref = getSharedPreferences("kk", Context.MODE_PRIVATE);
        selectedImagePath = sharedPref.getString("ipath", null);
        Log.v("kk", "ghk" + selectedImagePath);
        new OCRAsyncTask(ImageActivity.this, "08a1a26e5088957", false, selectedImagePath, "eng").execute();
    }

    private class OCRAsyncTask extends AsyncTask {

        private String url = "https://api.ocr.space/parse/image"; // OCR API Endpoints

        private String mApiKey;
        private boolean isOverlayRequired = false;
        private String mImageUrl;
        private String mLanguage;
        private Activity mActivity;
        private ProgressDialog mProgressDialog;

        public OCRAsyncTask(Activity activity, String apiKey, boolean isOverlayRequired, String imageUrl, String language) {
            this.mActivity = activity;
            this.mApiKey = apiKey;
            this.isOverlayRequired = isOverlayRequired;
            this.mImageUrl = imageUrl;
            this.mLanguage = language;
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(mActivity);
            mProgressDialog.setTitle("Wait while processing....");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Object[] params) {

            try {
                String s = sendPost(mApiKey, isOverlayRequired, mImageUrl, mLanguage);
                return s;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        private String sendPost(String apiKey, boolean isOverlayRequired, String imageUrl, String language) throws Exception {

            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            //add request header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");


            JSONObject postDataParams = new JSONObject();

            //used direct values for testing, working for parameters also
            postDataParams.put("apikey", "9ec509657e88957");
            postDataParams.put("isOverlayRequired", false);
            postDataParams.put("url", selectedImagePath);
            postDataParams.put("language", "eng");


            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(getPostDataString(postDataParams));
            wr.flush();
            wr.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //returning result
            return String.valueOf(response);
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            if (mProgressDialog != null && mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            String response = (String) result;
            //putting response into output TextView
            output.setText(response);
        }

        public String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while (itr.hasNext()) {

                String key = itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));

            }
            return result.toString();
        }
    }

}


