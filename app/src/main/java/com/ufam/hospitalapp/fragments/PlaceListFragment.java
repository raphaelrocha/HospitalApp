package com.ufam.hospitalapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.ufam.hospitalapp.BaseActivity;
import com.ufam.hospitalapp.PlaceActivity;
import com.ufam.hospitalapp.PlaceListActivity;
import com.ufam.hospitalapp.R;
import com.ufam.hospitalapp.adapters.PlaceListAdapter;
import com.ufam.hospitalapp.conn.ServerInfo;
import com.ufam.hospitalapp.conn.VolleyConnection;
import com.ufam.hospitalapp.interfaces.CustomVolleyCallbackInterface;
import com.ufam.hospitalapp.interfaces.RecyclerViewOnClickListenerHack;
import com.ufam.hospitalapp.models.Category;
import com.ufam.hospitalapp.models.FactoryDataTemp;
import com.ufam.hospitalapp.models.Hospital;
import com.ufam.hospitalapp.tools.GPSTracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PlaceListFragment extends Fragment implements RecyclerViewOnClickListenerHack, View.OnClickListener, CustomVolleyCallbackInterface {
    private final String TAG = PlaceListFragment.this.getClass().getSimpleName();
    protected RecyclerView mRecyclerView;
    protected List<Hospital> mList;
    protected android.support.design.widget.FloatingActionButton fab;
    protected VolleyConnection mVolleyConnection;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView ivRefresh;
    //private Category mCategory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("PLACELISTFRAG","onCreate()");

        mVolleyConnection = new VolleyConnection(this);

        ((BaseActivity)getActivity()).forceStartVolleyQueue();

        //mCategory = getArguments().getParcelable("category");

        callServer();
    }

    protected void callServer(){
//        Log.i("PLACELISTFRAG","callServer()"+ mCategory.getId());
        GPSTracker gpsTracker = ((BaseActivity)getActivity()).getGpsTracker();
        Double lat = gpsTracker.getLatitude();
        Double lng = gpsTracker.getLongitude();

        if(lat==0 ||  lng==0){
            lat = -3.088281;//temp ufam
            lng = -59.964379;//temp ufam
        }


        Integer radius = ((BaseActivity)getActivity()).getDistanceRadius();

        Log.i(TAG,"callServer()");
        HashMap<String, String> params = new  HashMap<String, String> ();
        params.put("user_latitude", lat.toString());
        params.put("user_longitude", lng.toString());
        params.put("radius", radius.toString());

        mVolleyConnection.callServerApiByJsonObjectRequest(ServerInfo.LIST_HOSPITAIS, Request.Method.POST,false,params,"LIST_HOSPITAIS");

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_place, container, false);

        Log.i(TAG,"onCreateView()");

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), mRecyclerView, this));

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(dy > 0){
                    //Log.i(TAG, "scroll up!");
                    ((PlaceListActivity)getContext()).hideFab();;
                }
                else{
                    //Log.i(TAG, "scroll down!");
                    ((PlaceListActivity)getContext()).showFab();;
                }



            }
        });

        //llm.setReverseLayout(true);
        mRecyclerView.setLayoutManager(llm);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_category);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.bringToFront();
                //Toast.makeText(getActivity(), "Atualizando... ", Toast.LENGTH_SHORT).show();

                //((MainActivity) getActivity()).startAsyncTask();
                callServer();
                //mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        ivRefresh = (ImageView) view.findViewById(R.id.iv_refresh_category);
        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callServer();
            }
        });

        showRefresh();

        return view;
    }

    public void showRefresh(){
        ivRefresh.setVisibility(View.VISIBLE);
    }

    public void hidenRefresh(){
        ivRefresh.setVisibility(View.INVISIBLE);
    }

    public void stopSwipeRefresh(){
        mSwipeRefreshLayout.setRefreshing(false);
    }


    public void setList(ArrayList<Hospital> c){
        if(c.size()==0){
            showRefresh();
        }
        stopSwipeRefresh();
        Log.i(TAG,"setList()");
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

        setList(hospitals);
    }

    //public void setCategory(Category c){
    //    this.mCategory = c;
    //}

    @Override
    public void onClickListener(View view, int position) {
        Log.i(TAG,"onClickListener()");

        Intent intent = new Intent(getActivity(), PlaceActivity.class);
        intent.putExtra("place", mList.get(position));
        getActivity().startActivity(intent);


    }
    @Override
    public void onLongPressClickListener(View view, int position) {
        Log.i(TAG,"onLongPressClickListener()");
        //Toast.makeText(getActivity(), "onLongPressClickListener(): " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deliveryResponse(JSONArray response, String flag) {
        /*Log.i("PLACELISTFRAG","deliveryResponse(Array)");
        Log.i("PLACELISTA_FRAG", response.toString());
        setCardView(response);
        try {
            String id = response.getJSONObject(0).getString("id");
            if(!id.equals("not_found")){
                setCardView(response,null);
            }else{
                ((BaseActivity)getActivity()).showLongSnack("Nenhum lugar encontrado.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

    }

    @Override
    public void deliveryResponse(JSONObject response, String flag) {
        hidenRefresh();
        //hidenRefresh();
        stopSwipeRefresh();
        setCardView(response);
        Log.i(TAG,"deliveryResponse(Object)");

    }

    @Override
    public void deliveryError(VolleyError error, String flag) {
        Log.i(TAG,"deliveryError()");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.i(TAG,"onStop()");
        mVolleyConnection.cancelRequest();
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

        Log.i(TAG,"onClick()");
        String aux = "";


        Toast.makeText(getActivity(), aux, Toast.LENGTH_SHORT).show();
    }


    /*@Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i("PLACELISTFRAG","onSaveInstanceState()");
        super.onSaveInstanceState(outState);
        //outState.putParcelableArrayList("mList", (ArrayList<Car>) mList);
    }*/

    /*@Override
    public void onResume() {
        super.onResume();
        Log.i("PLACELISTFRAG","onResume()");
        mCategory = getArguments().getParcelable("category");
    }*/

    @Override
    public void onResume() {
        super.onResume();
        callServer();
    }
}
