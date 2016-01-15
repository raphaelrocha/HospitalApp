package com.ufam.hospitalapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ufam.hospitalapp.R;
import com.ufam.hospitalapp.interfaces.RecyclerViewOnClickListenerHack;
import com.ufam.hospitalapp.models.Hospital;
import com.ufam.hospitalapp.tools.DataUrl;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;


/**
 * Created by viniciusthiengo on 4/5/15.
 */
public class PlaceListAdapter extends RecyclerView.Adapter<PlaceListAdapter.MyViewHolder> {
    private Context mContext;
    private List<Hospital> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
    private float scale;
    private int width, height, roundPixels;


    public PlaceListAdapter(Context c, List<Hospital> l){
        mContext = c;
        mList = l;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        scale = mContext.getResources().getDisplayMetrics().density;
        width = mContext.getResources().getDisplayMetrics().widthPixels - (int)(14 * scale + 0.5f);
        height = (width / 16) * 9;

        roundPixels = (int)(2 * scale + 0.5f);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;

        v = mLayoutInflater.inflate(R.layout.place_item_list, viewGroup, false);

        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    public Random rand = new Random();

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {

        Log.i("TESTE",""+mList.get(position).getNome());
        Log.i("TESTE","onBindViewHolder()");

        ControllerListener listener = new BaseControllerListener(){
            @Override
            public void onFinalImageSet(String id, Object imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                //Log.i("LOG", "onFinalImageSet");
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                super.onFailure(id, throwable);
                //Log.i("LOG", "onFailure");
            }

            @Override
            public void onIntermediateImageFailed(String id, Throwable throwable) {
                super.onIntermediateImageFailed(id, throwable);
                //Log.i("LOG", "onIntermediateImageFailed");
            }

            @Override
            public void onIntermediateImageSet(String id, Object imageInfo) {
                super.onIntermediateImageSet(id, imageInfo);
                //Log.i("LOG", "onIntermediateImageSet");
            }

            @Override
            public void onRelease(String id) {
                super.onRelease(id);
                //Log.i("LOG", "onRelease");
            }

            @Override
            public void onSubmit(String id, Object callerContext) {
                super.onSubmit(id, callerContext);
                //Log.i("LOG", "onSubmit");
            }
        };

        int w = 0;
        if( myViewHolder.ivPlace.getLayoutParams().width == FrameLayout.LayoutParams.MATCH_PARENT
            || myViewHolder.ivPlace.getLayoutParams().width == FrameLayout.LayoutParams.WRAP_CONTENT){

            Display display = ( (Activity) mContext ).getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize( size );

            try{
                w = size.x;
            }
            catch( Exception e ){
                w = display.getWidth();
            }
        }

        Log.i("URL",mList.get(position).getFotoHospital());
        Uri uri = Uri.parse(DataUrl.getUrlCustom(mList.get(position).getFotoHospital(), w));
        DraweeController dc = Fresco.newDraweeControllerBuilder()
                .setUri( uri )
                .setOldController( myViewHolder.ivPlace.getController() )
                .build();

        RoundingParams rp = RoundingParams.fromCornersRadii(roundPixels, roundPixels, 0, 0);
        myViewHolder.ivPlace.setController(dc);
        myViewHolder.ivPlace.getHierarchy().setRoundingParams(rp);

        myViewHolder.tvPlaceCity.setText(mList.get(position).getBairro().getCidade().getNome() + "/" + mList.get(position).getBairro().getCidade().getEstado().getNome());
        myViewHolder.tvPlaceName.setText(mList.get(position).getNome());

        if(mList.get(position).getDistancia()!=null){
            Double distance = Double.parseDouble(mList.get(position).getDistancia());

            String value = new DecimalFormat("#.##").format(distance);

            value = value+" Km";
            myViewHolder.tvPlaceDistance.setText(value);
        }
        myViewHolder.tvPlaceDistance.setVisibility(View.INVISIBLE);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){
        mRecyclerViewOnClickListenerHack = r;
    }


    public void addListItem(Hospital p, int position){
        mList.add(p);
        notifyItemInserted(position);
    }


    public void removeListItem(int position){
        mList.remove(position);
        notifyItemRemoved(position);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public SimpleDraweeView ivPlace;
        public TextView tvPlaceName;
        public TextView tvPlaceCity;
        public TextView tvPlaceDistance;

        public MyViewHolder(View itemView) {
            super(itemView);

            ivPlace = (SimpleDraweeView) itemView.findViewById(R.id.iv_place);
            tvPlaceName = (TextView) itemView.findViewById(R.id.tv_place_name);
            tvPlaceCity = (TextView) itemView.findViewById(R.id.tv_place_city);
            tvPlaceDistance = (TextView) itemView.findViewById(R.id.tv_place_distance);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mRecyclerViewOnClickListenerHack != null){
                mRecyclerViewOnClickListenerHack.onClickListener(v, getPosition());
            }
        }
    }
}
