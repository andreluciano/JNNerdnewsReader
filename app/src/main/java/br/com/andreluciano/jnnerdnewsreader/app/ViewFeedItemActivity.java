package br.com.andreluciano.jnnerdnewsreader.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.andreluciano.jnnerdnewsreader.app.bean.FeedMessage;

/**
 * Created by André on 03/06/2014.
 */
public class ViewFeedItemActivity extends AppCompatActivity {

    private TextView textTitle;
    private WebView webContent;
    private FeedMessage feedMessage;

    private AdView adView;
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewfeeditem);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        webContent = (WebView) findViewById(R.id.webContent);
        textTitle = (TextView) findViewById(R.id.textTitle);
        adView = (AdView) findViewById(R.id.adView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.app_name));

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        feedMessage = (FeedMessage) getIntent().getSerializableExtra("feedMessage");
        textTitle.setText(feedMessage.getTitle());

        String contentData = changeHTMLFormat(feedMessage.getContentEncoded());

        webContent.setWebChromeClient(new WebChromeClient());
        webContent.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
        webContent.getSettings().setJavaScriptEnabled(true);
        webContent.setBackgroundColor(0xFFFFFFFF);
        webContent.setVerticalScrollBarEnabled(false);
        webContent.setHorizontalScrollBarEnabled(false);
        webContent.loadDataWithBaseURL("", contentData, "text/html", "UTF-8", "");
    }

    public String changeHTMLFormat(String htmlObject) {
        String result = "", CSSCoding = "", imagem = "", video = "";

        CSSCoding = htmlObject;
        //CSSCoding = CSSCoding.replaceAll("<[aA].*?>", "<span>");
        //CSSCoding = CSSCoding.replaceAll("</[aA]>", "</span>");

        CSSCoding = CSSCoding.replaceAll("width=\"590\"", "width=\"100%\"");
        CSSCoding = CSSCoding.replaceAll("height=\"350\"", "");
        //CSSCoding = CSSCoding.replaceAll("//www.", "http://www.");

        CSSCoding = CSSCoding.replaceAll("width=\"100%\" height=\"332\"", "width=\"100%\"");
        //result = "<font color=\"#FFFFFF\">"+CSSCoding+"</font>";
        result = CSSCoding;

        Pattern pattern = Pattern.compile("<img(.+?)/>");
        Matcher matcher = pattern.matcher(result);
        if (matcher.find()) {
            imagem = matcher.group();
        }

        Pattern pattern2 = Pattern.compile("<iframe(.+?)></iframe>");
        Matcher matcher2 = pattern2.matcher(result);
        if (matcher2.find()) {
            video = matcher2.group();
            Log.d("source do video", video.split("src\\s*=\"")[1].split("\\s*\"")[0]);
            Log.d("source do iframe", video);
            CSSCoding = CSSCoding.replaceAll(video, video + "<center><a href=\"" + video.split("src\\s*=\"")[1].split("\\s*\"")[0] + "\">(Abrir no Youtube - Tela Cheia)</a></center>");
            Log.d("novo video", video + "<center><a href=\"" + video.split("src\\s*=\"")[1].split("\\s*\"")[0] + "\">(Abrir no Youtube - tela cheia)</a></center>");
        }

        result = CSSCoding;

        return result;
    }

    @Override
    public void onPause() {
        adView.pause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        adView.resume();
    }

    @Override
    public void onDestroy() {
        adView.destroy();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_share){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, feedMessage.getTitle());
            sendIntent.putExtra(Intent.EXTRA_TEXT, feedMessage.getLink());
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Compartilhar"));
        }else if (id == R.id.action_browser) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(feedMessage.getLink()));
            startActivity(browserIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}