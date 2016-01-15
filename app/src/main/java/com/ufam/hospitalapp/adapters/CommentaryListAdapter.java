package com.ufam.hospitalapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
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
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ufam.hospitalapp.R;
import com.ufam.hospitalapp.interfaces.RecyclerViewOnClickListenerHack;
import com.ufam.hospitalapp.models.Comentario;
import com.ufam.hospitalapp.tools.DataUrl;

import java.util.List;
import java.util.Random;


/**
 * Created by viniciusthiengo on 4/5/15.
 */
public class CommentaryListAdapter extends RecyclerView.Adapter<CommentaryListAdapter.MyViewHolder> {
    private Context mContext;
    private List<Comentario> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
    private float scale;
    private int width, height, roundPixels;
    final String TAG = CommentaryListAdapter.this.getClass().getSimpleName();



    public CommentaryListAdapter(Context c, List<Comentario> l){
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

        v = mLayoutInflater.inflate(R.layout.commentary_item_list, viewGroup, false);

        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    public Random rand = new Random();

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {

        /*ControllerListener listener = new BaseControllerListener(){
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
        */

        int w = 0;
        if( myViewHolder.ivUser.getLayoutParams().width == FrameLayout.LayoutParams.MATCH_PARENT
            || myViewHolder.ivUser.getLayoutParams().width == FrameLayout.LayoutParams.WRAP_CONTENT){

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

        Uri uri = Uri.parse(DataUrl.getUrlCustom(mList.get(position).getUsuario().getFotoUsuario(), w));
        DraweeController dc = Fresco.newDraweeControllerBuilder()
                .setUri( uri )
                .setOldController( myViewHolder.ivUser.getController() )
                .build();

        RoundingParams rp = RoundingParams.fromCornersRadii(roundPixels, roundPixels, 0, 0);
        myViewHolder.ivUser.setController(dc);
        myViewHolder.ivUser.getHierarchy().setRoundingParams(rp);

        myViewHolder.tvNameUser.setText(mList.get(position).getUsuario().getNome());
        myViewHolder.tvText.setText(mList.get(position).getTexto());
        myViewHolder.tvDateTime.setText(mList.get(position).getDataRegistro());

        /*final int pos = position;
        final Comentario commentary = mList.get(pos);

        final Rating rating = commentary.getRating();
        final String value;

        Log.w(TAG,""+position);
        Log.w(TAG,mList.get(position).getUser().getName());

        if(rating!=null){
            value  = rating.getValue() + ",0";
            myViewHolder.tvRvRating.setText(value);
            Log.w(TAG, value);
        }else{
            myViewHolder.tvRvRating.setVisibility(View.INVISIBLE);
        }*/
        myViewHolder.tvRvRating.setVisibility(View.INVISIBLE);


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){
        mRecyclerViewOnClickListenerHack = r;
    }


    public void addListItem(Comentario c, int position){
        mList.add(c);
        notifyItemInserted(position);
    }


    public void removeListItem(int position){
        mList.remove(position);
        notifyItemRemoved(position);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        public TextView tvNameUser;
        public TextView tvText;
        public TextView tvDateTime;
        public TextView tvRvRating;
        public SimpleDraweeView ivUser;


        public MyViewHolder(View itemView) {
            super(itemView);

            ivUser = (SimpleDraweeView) itemView.findViewById(R.id.iv_user);
            tvNameUser = (TextView) itemView.findViewById(R.id.tv_name_user);
            tvText = (TextView) itemView.findViewById(R.id.tv_text);
            tvDateTime = (TextView) itemView.findViewById(R.id.tv_date_time);
            tvRvRating = (TextView) itemView.findViewById(R.id.tv_rating_comment);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mRecyclerViewOnClickListenerHack != null){
                mRecyclerViewOnClickListenerHack.onClickListener(v, getPosition());
            }
        }


        @Override
        public boolean onLongClick(View v) {
            if(mRecyclerViewOnClickListenerHack != null){
                mRecyclerViewOnClickListenerHack.onLongPressClickListener(v, getPosition());
                return true;
            }
            return false;
        }
    }
}
