package com.example.simon.vilshofenguide.view;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.simon.vilshofenguide.R;
import com.example.simon.vilshofenguide.pathfinding.PathChangeController;
import com.example.simon.vilshofenguide.pathfinding.Sight;
import com.example.simon.vilshofenguide.pathfinding.SightInitializer;
import com.example.simon.vilshofenguide.pathfinding.SightManager;

class DisableFollowLinksWebViewClient extends WebViewClient{

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url){
        if (Uri.parse(url).getHost().equals(SightInitializer.serverURL)) {
            // This is my web site, so do not override; let my WebView load the page
            return false;
        }else{
            return true;
        }
    }
}

public class SightDetailActivity extends AppCompatActivity {

    private static final String englishHTMLFileEnding = "EN";
    private static final String germanHTMLFileEnding = "DE";

    private PathChangeController pathChanger;
    private SightManager manager;
    private Sight s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sight_detail);

        if (getIntent().getSerializableExtra("path") != null){
            this.pathChanger = (PathChangeController)getIntent().getSerializableExtra("path");
        }
        this.manager = (SightManager)getIntent().getSerializableExtra("sightManager");
        this.s = (Sight)getIntent().getSerializableExtra("sight");

        WebView webView = (WebView)findViewById(R.id.sight_detail_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new DisableFollowLinksWebViewClient());
        //webView.loadUrl("http://www.wikipedia.de");
        //webView.loadDataWithBaseURL("file:///android_res/mipmap/", this.manager.getSightDetailDescriptionHtmlCode(s), "text/html; charset=UTF-8", null, null);
        //webView.loadData(this.manager.getSightDetailDescriptionHtmlCode(s), "text/html; charset=UTF-8", null);
        //new File("file:///android_res/");
        webView.loadUrl(SightInitializer.serverAdress + "/" + s.getHtmlFileName() +
                (Sight.useEnglish?englishHTMLFileEnding:germanHTMLFileEnding) + ".html");
    }
}