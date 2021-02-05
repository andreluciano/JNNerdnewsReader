package br.com.andreluciano.jnnerdnewsreader.app;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import br.com.andreluciano.jnnerdnewsreader.app.adapter.FeedMessagesAdapter;
import br.com.andreluciano.jnnerdnewsreader.app.bean.Feed;
import br.com.andreluciano.jnnerdnewsreader.app.bean.FeedMessage;
import br.com.andreluciano.jnnerdnewsreader.app.control.FeedReader;

public class MainActivity extends AppCompatActivity {

    private Feed feedCache;
    private SwipeRefreshLayout mSwipeLayout;
    private RecyclerView mRecyclerView;
    private FeedMessagesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.mSwipelayout);
        this.mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FeedTask().execute("http://jovemnerd.com.br/categoria/jovem-nerd-news/feed/");
            }
        });

        this.mRecyclerView = (RecyclerView) findViewById(R.id.recyclerFeed);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (savedInstanceState == null) {
            if (isOnline()) {
                mSwipeLayout.setRefreshing(true);
                new FeedTask().execute("http://jovemnerd.com.br/categoria/jovem-nerd-news/feed/");
                //new FeedTask().execute(" http://jovemnerd.com.br/feed/");
            } else {
                Toast.makeText(MainActivity.this, "Sem conexão com a internet, impossível atualizar", Toast.LENGTH_SHORT).show();
            }

        } else {
            feedCache = (Feed) savedInstanceState.getSerializable("feed");
            mAdapter = new FeedMessagesAdapter(MainActivity.this, feedCache.getMessages());
            mAdapter.setOnItemClickListener(new FeedMessagesAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    FeedMessage feedMessage = mAdapter.getItem(position);

                    Intent intentFeedMessage = new Intent(MainActivity.this, ViewFeedItemActivity.class);
                    intentFeedMessage.putExtra("feedMessage", feedMessage);
                    startActivity(intentFeedMessage);
                }
            });
            mRecyclerView.setAdapter(mAdapter);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("feed", feedCache);
        Log.d("STATE-SAVE", "onSaveInstanceState()");
    }

    class FeedTask extends AsyncTask<String, String, Feed> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Feed doInBackground(String... params) {
            FeedReader fReader = new FeedReader(params[0]);
            fReader.read();
            Feed feed = fReader.getFeed();
            feedCache = feed;
            return feed;
        }

        @Override
        protected void onPostExecute(final Feed result) {
            super.onPostExecute(result);

            if (result != null) {
                mAdapter = new FeedMessagesAdapter(MainActivity.this, result.getMessages());
                mAdapter.setOnItemClickListener(new FeedMessagesAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        FeedMessage feedMessage = mAdapter.getItem(position);

                        Intent intentFeedMessage = new Intent(MainActivity.this, ViewFeedItemActivity.class);
                        intentFeedMessage.putExtra("feedMessage", feedMessage);
                        startActivity(intentFeedMessage);
                    }
                });
                mRecyclerView.setAdapter(mAdapter);

                mSwipeLayout.setRefreshing(false);

            }
        }

    }

    public boolean isOnline() {
        ConnectivityManager connectivity = (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_atualizar) {
            mSwipeLayout.setRefreshing(true);
            new FeedTask().execute("http://jovemnerd.com.br/categoria/jovem-nerd-news/feed/");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}