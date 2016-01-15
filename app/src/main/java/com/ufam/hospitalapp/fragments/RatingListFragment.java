package com.ufam.hospitalapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.ufam.hospitalapp.BaseActivity;
import com.ufam.hospitalapp.PlaceListActivity;
import com.ufam.hospitalapp.R;
import com.ufam.hospitalapp.adapters.RatingListAdapter;
import com.ufam.hospitalapp.conn.ServerInfo;
import com.ufam.hospitalapp.conn.VolleyConnection;
import com.ufam.hospitalapp.interfaces.CustomVolleyCallbackInterface;
import com.ufam.hospitalapp.interfaces.RecyclerViewOnClickListenerHack;
import com.ufam.hospitalapp.models.AvaliaHospital;
import com.ufam.hospitalapp.models.Hospital;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RatingListFragment extends Fragment implements RecyclerViewOnClickListenerHack, View.OnClickListener, CustomVolleyCallbackInterface {

    protected RecyclerView mRecyclerView;
    private List<AvaliaHospital> mList;
    private VolleyConnection mVolleyConnection;
    private Hospital mPlace;
    private int mCONTA_SNACK_ALERT; //garante q seja exibido apenas um snackalert no erro.


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((BaseActivity)getActivity()).forceStartVolleyQueue();

        mVolleyConnection = new VolleyConnection(this);

        mCONTA_SNACK_ALERT=0;

        mPlace = getArguments().getParcelable("place");

        callServer();
    }

    public void callServer (){
        HashMap<String,String> params = new HashMap<String, String>();
        params.put("id_place",mPlace.getId());
       // mVolleyConnection.callServerApiByJsonArrayRequest(ServerInfo.GET_RATING_LIST, Request.Method.POST, params, "get-list-ratings");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        final View view = inflater.inflate(R.layout.fragment_rating_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list_rating);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), mRecyclerView, this));

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        //llm.setReverseLayout(true);
        mRecyclerView.setLayoutManager(llm);
        return view;
    }

    public void setList(ArrayList<AvaliaHospital> r){
        mList = r;
        RatingListAdapter adapter = new RatingListAdapter(getActivity(), mList);
        //adapter.setRecyclerViewOnClickListenerHack(this);
        mRecyclerView.setAdapter(adapter);
    }


    public void setCardView(JSONArray ja,String status){
        ArrayList<AvaliaHospital> ratings = new ArrayList<AvaliaHospital>();
        try {
            for(int i = 0, tam = ja.length(); i < tam; i++){
                AvaliaHospital rating = new AvaliaHospital();
                rating = ((BaseActivity)getActivity()).popAvaliaHospital(ja.getJSONObject(i));
                ratings.add(rating);
            }
        }catch (JSONException e){
            if(mCONTA_SNACK_ALERT==0){
                mCONTA_SNACK_ALERT++;
                Log.i("SNAK", "---- Lançou o snak ----");
                ((BaseActivity)getActivity()).showLongSnack(getResources().getString(R.string.list_empty));
            }
        }
        setList(ratings);
    }



    @Override
    public void onClickListener(View view, int position) {

        Intent intent = new Intent(getActivity(), PlaceListActivity.class);
        intent.putExtra("rating", mList.get(position));
        getActivity().startActivity(intent);


    }


    @Override
    public void deliveryResponse(JSONArray response, String TAG) {
        Log.i("CATEGORY_LIST_FRAG", "SUCESS: "+response.toString());
        setCardView(response,null);
    }

    @Override
    public void deliveryResponse(JSONObject response, String TAG) {

    }

    @Override
    public void deliveryError(VolleyError error, String TAG) {
        Log.i("CATEGORY_LIST_FRAG", "ERROR: "+error );
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onLongPressClickListener(View view, int position) {

    }


    private static class RecyclerViewTouchListener implements RecyclerView.OnItemTouchListener {
        private Context mContext;
        private GestureDetector mGestureDetector;
        private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

        public RecyclerViewTouchListener(Context c, final RecyclerView rv, RecyclerViewOnClickListenerHack rvoclh) {
            mContext = c;
            mRecyclerViewOnClickListenerHack = rvoclh;

            mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);

                    View cv = rv.findChildViewUnder(e.getX(), e.getY());

                    if (cv != null && mRecyclerViewOnClickListenerHack != null) {
                        mRecyclerViewOnClickListenerHack.onLongPressClickListener(cv,
                                rv.getChildPosition(cv));
                    }
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    View cv = rv.findChildViewUnder(e.getX(), e.getY());

                    if (cv != null && mRecyclerViewOnClickListenerHack != null) {
                        mRecyclerViewOnClickListenerHack.onClickListener(cv,
                                rv.getChildPosition(cv));
                    }

                    return (true);
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    @Override
    public void onStop(){
        super.onStop();
        mVolleyConnection.cancelRequest();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCONTA_SNACK_ALERT=0;
        callServer();
    }
}
