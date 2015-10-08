package com.syt.balram.syt;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.syt.constant.Constant;

/**
 * Created by Balram on 4/3/2015.
 */
public class ImageAdapter extends BaseAdapter{

    private Context mContext;

    // Constructor
    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override


    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(Constant.CategoryImageWidth,Constant.CategoryImageHeight));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(0,0,0,0);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // Keep all Images in array
    public Integer[] mThumbIds = {
            R.drawable.categories_one, R.drawable.categories_two,
            R.drawable.categories_three, R.drawable.categories_four,
            R.drawable.categories_five, R.drawable.categories_six,
            R.drawable.categories_eight, R.drawable.categories_seven,
            R.drawable.categories_nine

    };
}
