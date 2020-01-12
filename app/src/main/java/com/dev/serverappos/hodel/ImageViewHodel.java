package com.dev.serverappos.hodel;


import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.dev.serverappos.R;

public class ImageViewHodel extends RecyclerView.ViewHolder {
    public ImageView img;




    public ImageViewHodel(View itemView) {
        super(itemView);
        img=itemView.findViewById(R.id.my_image_view);

    }
}
