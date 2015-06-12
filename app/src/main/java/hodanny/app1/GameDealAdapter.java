package hodanny.app1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


/**
 * Created by thho on 6/9/2015.
 */
public class GameDealAdapter extends RecyclerView.Adapter<GameDealAdapter.GameViewHolder> {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    List<GameDeal> mDataset;
    public static class GameViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView mTextView;
        public TextView mGameDealUrl;
        public ImageView mGameDealImage;
        public LinearLayout mLowerCardHalf;

        public Context mContext;

        public GameViewHolder(View v) {
            super(v);
            mTextView = (TextView)v.findViewById(R.id.gamedeal_title);
            mGameDealUrl = (TextView) v.findViewById(R.id.gamedeal_url);
            mGameDealImage = (ImageView) v.findViewById(R.id.gamedeal_image);
            mLowerCardHalf = (LinearLayout) v.findViewById(R.id.card_bottom);
            mContext = v.getContext();
        }

        @Override
        public void onClick(View v) {
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public GameDealAdapter(List<GameDeal> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gamedeal_row, parent, false);

        // set the view's size, margins, paddings and layout parameters
        GameViewHolder vh = new GameViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(GameViewHolder holder, int position) {

        holder.mGameDealImage.setImageDrawable(holder.mContext.getResources().getDrawable(R.drawable.lemon));
        GameDeal deal = mDataset.get(position);

        String description = deal.getUrl();
        try {
            description = new URL(deal.getUrl()).getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        holder.mTextView.setText(deal.getTitle());
        holder.mGameDealUrl.setText(description);
        if(!deal.getThumbnail().equals("self") && !deal.getThumbnail().equals("default") &&  !deal.getThumbnail().equals("nsfw") ) {
            new ImageLoadTask(deal.getThumbnail(), holder.mGameDealImage).execute();
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public GameDeal getItem(int position){
        return mDataset.get(position);
    }
}