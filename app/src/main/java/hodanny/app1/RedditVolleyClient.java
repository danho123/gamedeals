package hodanny.app1;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by thho on 6/12/2015.
 */
public class RedditVolleyClient {
    private static RedditVolleyClient mInstance;
    private RequestQueue mRequestQueue;
    private static Context mContext;

    private RedditVolleyClient(Context context)
    {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized RedditVolleyClient getInstance(Context context)
    {
        if(mInstance == null)
        {
            mInstance = new RedditVolleyClient(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if(mRequestQueue == null)
        {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public void addToRequestQueue(Request req)
    {
        getRequestQueue().add(req);
    }
}
