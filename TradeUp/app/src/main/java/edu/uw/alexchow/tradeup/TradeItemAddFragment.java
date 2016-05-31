package edu.uw.alexchow.tradeup;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.uw.alexchow.tradeup.dummy.DummyContent;

/**
 * Created by alexchow on 5/24/16.
 */
public class TradeItemAddFragment extends Fragment {

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private TradeItem mItem;
    private String TAG = "trade item add fragment";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TradeItemAddFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG,"lol");
        super.onCreate(savedInstanceState);

//        if (getArguments().containsKey(ARG_ITEM_ID)) {
//            // Load the dummy content specified by the fragment
//            // arguments. In a real-world scenario, use a Loader
//            // to load content from a content provider.
//            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
//
//            Activity activity = this.getActivity();
//            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
//            if (appBarLayout != null) {
//                appBarLayout.setTitle(mItem.title);
//            }
//        }
        Log.v(TAG,"");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tradeitem_add, container, false);

        // Show the dummy content as text in a TextView.
//        if (mItem != null) {
//            ((EditText) rootView.findViewById(R.id.trade_item_description)).setText(mItem.description);
//            ((EditText) rootView.findViewById(R.id.tradeitem_id)).setText(mItem.id);
//            ((EditText) rootView.findViewById(R.id.tradeitem_name)).setText(mItem.name);
//            ((EditText) rootView.findViewById(R.id.tradeitem_posterName)).setText(mItem.posterName);
//            ((EditText) rootView.findViewById(R.id.tradeitem_status)).setText(mItem.status);
//            ((EditText) rootView.findViewById(R.id.tradeitem_timeStamp)).setText(mItem.timeStamp);
//
//
//        }

        return rootView;
    }


}
