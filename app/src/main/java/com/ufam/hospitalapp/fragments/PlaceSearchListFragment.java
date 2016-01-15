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
import android.widget.Toast;

import com.ufam.hospitalapp.BaseActivity;
import com.ufam.hospitalapp.PlaceActivity;
import com.ufam.hospitalapp.R;
import com.ufam.hospitalapp.adapters.PlaceListAdapter;
import com.ufam.hospitalapp.interfaces.RecyclerViewOnClickListenerHack;
import com.ufam.hospitalapp.models.Category;
import com.ufam.hospitalapp.models.FactoryDataTemp;
import com.ufam.hospitalapp.models.Hospital;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PlaceSearchListFragment extends Fragment implements RecyclerViewOnClickListenerHack, View.OnClickListener {
    protected static final String TAG = "LOG";
    protected RecyclerView mRecyclerView;
    protected List<Hospital> mList;
    protected android.support.design.widget.FloatingActionButton fab;
    private Category mCategory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //mVolleyConnection = new VolleyConnection(this);

        //callServer();

    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search_place, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), mRecyclerView, this));

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        //llm.setReverseLayout(true);
        mRecyclerView.setLayoutManager(llm);

        return view;
    }


    public void setList(ArrayList<Hospital> c){
        mList = c;
        PlaceListAdapter adapter = new PlaceListAdapter(getActivity(), mList);
        adapter.setRecyclerViewOnClickListenerHack(this);
        mRecyclerView.setAdapter(adapter);
    }


    public void setCardView(JSONObject jo){
        Log.i(TAG,"setCardView()");
        ArrayList<Hospital> hospitals = new ArrayList<Hospital>();
        JSONArray ja = null;
        try {
            ja = jo.getJSONArray("hospitais");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            for(int i = 0, tam = ja.length(); i < tam; i++){
                Hospital h = new Hospital();
                h = ((BaseActivity)getActivity()).popHospital(ja.getJSONObject(i));
                hospitals.add(h);
            }
        }catch (JSONException e){}

        if(hospitals.size()==0){
            ((BaseActivity)getActivity()).showLongSnack("Nenhuma unidade de saúde encontrada.");
        }

        setList(hospitals);
    }

    public void setCategory(Category c){
        this.mCategory = c;
    }

    @Override
    public void onClickListener(View view, int position) {

        Intent intent = new Intent(getActivity(), PlaceActivity.class);
        intent.putExtra("place", mList.get(position));
        getActivity().startActivity(intent);

    }
    @Override
    public void onLongPressClickListener(View view, int position) {
        Toast.makeText(getActivity(), "onLongPressClickListener(): " + position, Toast.LENGTH_SHORT).show();
    }




    private static class RecyclerViewTouchListener implements RecyclerView.OnItemTouchListener {
        private Context mContext;
        private GestureDetector mGestureDetector;
        private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

        public RecyclerViewTouchListener(Context c, final RecyclerView rv, RecyclerViewOnClickListenerHack rvoclh){
            mContext = c;
            mRecyclerViewOnClickListenerHack = rvoclh;

            mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);

                    View cv = rv.findChildViewUnder(e.getX(), e.getY());

                    if(cv != null && mRecyclerViewOnClickListenerHack != null){
                        mRecyclerViewOnClickListenerHack.onLongPressClickListener(cv,
                                rv.getChildPosition(cv) );
                    }
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    View cv = rv.findChildViewUnder(e.getX(), e.getY());

                    if(cv != null && mRecyclerViewOnClickListenerHack != null){
                        mRecyclerViewOnClickListenerHack.onClickListener(cv,
                                rv.getChildPosition(cv) );
                    }

                    return(true);
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            mGestureDetector.onTouchEvent(e);
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {}

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean b) {}
    }


    @Override
    public void onClick(View v) {
        String aux = "";


        Toast.makeText(getActivity(), aux, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putParcelableArrayList("mList", (ArrayList<Car>) mList);
    }
}
