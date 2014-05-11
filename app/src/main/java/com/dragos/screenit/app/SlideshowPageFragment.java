package com.dragos.screenit.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Dragos Raducanu (raducanu.dragos@gmail.com) on 4/13/14.
 */
public class SlideshowPageFragment extends Fragment {


    private View mRootView;



    public SlideshowPageFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.slideshowpage_fragment, container, false);

        String imagePath = getArguments().getString("imgPath");

        ImageView image = (ImageView) mRootView.findViewById(R.id.image);

        Picasso.with(getActivity())
                .load("file:///" + imagePath)
                .fit()
                .centerInside()
                .into(image);

        return mRootView;
    }
}
