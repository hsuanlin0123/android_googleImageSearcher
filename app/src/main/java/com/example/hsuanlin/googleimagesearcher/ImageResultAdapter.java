package com.example.hsuanlin.googleimagesearcher;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by hsuanlin on 2015/8/4.
 */
public class ImageResultAdapter extends ArrayAdapter<SearchPhoto>{
    public ImageResultAdapter(Context context, List<SearchPhoto> objects){
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchPhoto photo = getItem(position);
        if( convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_image,parent,false);
        }

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        tvTitle.setText(Html.fromHtml(photo.title));
        ivPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.tbUrl).into(ivPhoto);
        return convertView;
    }
}
