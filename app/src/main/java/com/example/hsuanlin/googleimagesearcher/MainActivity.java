package com.example.hsuanlin.googleimagesearcher;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements AdvanceFilter.AdvanceFilterListener {

    private ArrayList<SearchPhoto> photos;
    private GridView gvShowImage;
    private ImageResultAdapter aImageResults;
    private EditText etSearchString;
    private String opImageSize;
    private String opColorFilter;
    private String opImageType;
    private String opSiteFilter;

    private static int pageSize = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        photos = new ArrayList<>();
        gvShowImage = (GridView) findViewById(R.id.gvShowImage);
        aImageResults = new ImageResultAdapter(this,photos);
        gvShowImage.setAdapter(aImageResults);
        etSearchString = (EditText) findViewById(R.id.etSearchStr);

        gvShowImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, ImageDisplayActivity.class);
                SearchPhoto photo = photos.get(position);
                i.putExtra("url", photo.FullUrl);
                startActivity(i);
            }
        });

        gvShowImage.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                fetchSearchImage(page);
            }
        });
    }

    @Override
    public void onFinishSettingFragment(String imageSize, String colorFilter, String imageType, String siteFilter)
    {
        opImageSize = imageSize;
        opColorFilter =colorFilter ;
        opImageType = imageType;
        opSiteFilter = siteFilter;
    }

    public void onClickSettings( MenuItem item )
    {
        FragmentManager fm = getSupportFragmentManager();
        AdvanceFilter advanceFilter = AdvanceFilter.newInstance();
        advanceFilter.show(fm,"test");
    }

    private String getQueryUrl(int page)
    {
        String url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + this.etSearchString.getText().toString();
        if( opImageSize!=null && !opImageSize.isEmpty())
            url += "&imgsz=" + opImageSize;

        if( opColorFilter!=null && !opColorFilter.isEmpty())
            url += "&imgcolor=" + opColorFilter;

        if( opImageType!=null && !opImageType.isEmpty())
            url += "&imgtype=" + opImageType;

        if( opSiteFilter!=null && !opSiteFilter.isEmpty())
            url += "&as_sitesearch=" + opSiteFilter;

        url += "&rsz=" + this.pageSize;
        url += "&start=" + page * this.pageSize;

        return url;
    }

    private boolean checkNetwork()
    {
        ConnectivityManager cm =
                (ConnectivityManager)MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    public void onClickSearch( View view)
    {
        if(this.etSearchString.getText().length() == 0)
            return;

        if(checkNetwork()) {
            Toast.makeText(view.getContext(), "Network is available", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(view.getContext(), "Network is not available", Toast.LENGTH_SHORT).show();
            return;
        }

        photos.clear();
        fetchSearchImage(0);
    }

    public void fetchSearchImage(int page)
    {
        String url = getQueryUrl(page);
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, null, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        JSONArray photosJSON = null;
                        try {
                            photosJSON = response.getJSONObject("responseData").getJSONArray("results");
                            for( int i = 0; i< photosJSON.length(); i++){
                                JSONObject photoJSON = photosJSON.getJSONObject(i);

                                SearchPhoto photo = new SearchPhoto();

                                photo.FullUrl = photoJSON.getString("url");
                                photo.tbUrl = photoJSON.getString("tbUrl");
                                photo.title = photoJSON.getString("title");
                                photo.imageHeight = photoJSON.getInt("height");
                                photo.imageWidth = photoJSON.getInt("width");

                                photos.add(photo);
                            }
                            aImageResults.notifyDataSetChanged();
                        }
                        catch( JSONException e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.i("DEBUG",errorResponse.toString());
                    }
                }
        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            onClickSettings(item);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
