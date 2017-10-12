package com.soto.videoprecios;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by José on 07/10/2017.
 */

public class VideogameAdapter extends ArrayAdapter<Videogame>{

    private int layoutResource;

    public VideogameAdapter(Context context, int resource, List<Videogame> videogameList) {
        super(context, resource, videogameList);

        this.layoutResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(layoutResource, null);
        }

        Videogame videogame = getItem(position);

        if (videogame != null) {
            ImageView imageView = (ImageView) view.findViewById(R.id.list_Image);
            TextView nameView = (TextView) view.findViewById(R.id.list_Name);
            TextView priceView = (TextView) view.findViewById(R.id.list_Price);
            TextView sellerView = (TextView) view.findViewById(R.id.list_Seller);

            if (imageView != null) {
                imageView.setImageBitmap(videogame.getImage());
            }
            if (nameView != null) {
                nameView.setText(videogame.getName());
            }
            if (priceView != null) {
                priceView.setText(videogame.getPrice());
            }
            if (sellerView != null) {
                sellerView.setText(videogame.getSeller());
            }
        }

        return view;
    }
}
