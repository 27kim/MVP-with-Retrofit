/**
 * @file CastAdapter.java
 * @brief This is adapter class responsible for showing movie casts.
 * @author Shrikant
 * @date 15/04/2018
 */

package com.mvp_with_retrofit.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.mvp_with_retrofit.R;
import com.mvp_with_retrofit.model.Cast;
import com.mvp_with_retrofit.network.MovieApiClient;

import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.MyViewHolder> {

    private Context mContext;
    private List<Cast> castList;

    public CastAdapter(Context mContext, List<Cast> castList) {
        this.mContext = mContext;
        this.castList = castList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cast_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        Cast cast = castList.get(position);

        holder.tvCharacter.setText(cast.getCharacter());
        holder.tvName.setText(cast.getName());

        // loading cast profile pic using Glide library
        Glide.with(mContext)
                .load(MovieApiClient.IMAGE_BASE_URL + cast.getProfilePath())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.pbLoadProfile.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.pbLoadProfile.setVisibility(View.GONE);
                        return false;
                    }
                })
                .apply(new RequestOptions().placeholder(R.drawable.ic_place_holder).error(R.drawable.ic_place_holder))
                .into(holder.ivProfilePic);
    }

    @Override
    public int getItemCount() {
        return castList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvCharacter;

        public TextView tvName;

        public ImageView ivProfilePic;

        public ProgressBar pbLoadProfile;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvCharacter = itemView.findViewById(R.id.tv_character);
            tvName = itemView.findViewById(R.id.tv_name);
            ivProfilePic = itemView.findViewById(R.id.iv_profile_pic);
            pbLoadProfile = itemView.findViewById(R.id.pb_load_profile);
        }
    }
}
