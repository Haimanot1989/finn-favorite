package android.itsisiterhood.myfavorite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.itsisiterhood.myfavorite.utilities.NetworkUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{
    final static String REMOTE_JSON = "https://gist.githubusercontent.com/3lvis/3799feea005ed49942dcb56386ecec2b/raw/63249144485884d279d55f4f3907e37098f55c74/discover.json";
    private TextView mSearchResultsTextView;
    private TextView mErrorMessageTextView;
    private ProgressBar mLoadingIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSearchResultsTextView = (TextView) findViewById(R.id.tv_finn_search_results_json);
        mErrorMessageTextView = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        makeFinnQuery();
    }

    private void makeFinnQuery(){
        URL finnSearchUrl = NetworkUtils.buildUrl(REMOTE_JSON);
        new FinnQueryTask().execute(finnSearchUrl);
    }

    private void showJsonDataView(){
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        mSearchResultsTextView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(){
        mSearchResultsTextView.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
    }

    public class FinnQueryTask extends AsyncTask<URL, Void, String>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String finnSearchResults = null;

            try{
                finnSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return finnSearchResults;
        }

        @Override
        protected void onPostExecute(String s){
            //TODO s is the jsonResul
            //I should parse it here and format it to what I want
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if(s != null && !s.equals("")){
                showJsonDataView();
                mSearchResultsTextView.setText(s);
            } else{
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        //Switch switch1 = menu.findItem(R.id.toggle_favorite).getActionView().findViewById()
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if(isChecked){
            Context context = MainActivity.this;
            Toast.makeText(context, "toggled ON", Toast.LENGTH_LONG).show();
        } else{
            Context context = MainActivity.this;
            Toast.makeText(context, "toggled OFF", Toast.LENGTH_LONG).show();

        }
    }
}

