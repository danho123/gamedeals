package hodanny.app1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    //views
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    public SwipeRefreshLayout mSwipeRefreshLayout;

    boolean loading = true;
    final int listViewBufferSize = 10;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    LinearLayoutManager mLayoutManager;

    List<GameDeal> gameDeals;
    String mAfter = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshItems();
        InitViews();

    }

    private void InitViews()
    {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
//                refreshItems();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new GameDealAdapter(gameDeals);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(gameDeals.get(position).getUrl()));
                        startActivity(i);
                    }
                })
        );

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        loading = false;
                        sendRedditRequest("gamedeals", "hot", 10, mAfter);
                    }
                }
            }
        });


    }

    private void refreshItems() {
        gameDeals = new ArrayList<>();
        mAfter = "";
        sendRedditRequest("gamedeals", "hot", 10, mAfter);

        if(mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void sendRedditRequest(String subreddit, String filter, int limit, String after)
    {
        String url = String.format("https://www.reddit.com/r/%s/%s.json?limit=%d&after=%s", subreddit, filter, limit, after);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray array = (JSONArray) ((JSONObject) object.get("data")).get("children");

                            mAfter = ((JSONObject) object.get("data")).get("after").toString();

                            JSONObject data;

                            for (int i = 0; i < array.length(); i++) {
                                data = (JSONObject) array.get(i);
                                data = ((JSONObject) data.get("data"));
                                gameDeals.add(new GameDeal(data));
                            }
                            mAdapter.notifyDataSetChanged();

                            loading = true;
                        } catch (Exception e) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        RedditVolleyClient.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
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

        return super.onOptionsItemSelected(item);
    }
}

