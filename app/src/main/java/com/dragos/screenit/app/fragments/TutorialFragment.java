package com.dragos.screenit.app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.dragos.screenit.app.R;
import com.dragos.screenit.app.utils.SharedPreferencesUtils;

/**
 * Created by Dragos Raducanu (raducanu.dragos@gmail.com) on 4/13/14.
 */
public class TutorialFragment extends Fragment {


    private View mRootView;
    private int mPage;
    private String mBackgroundPath;

    public TutorialFragment() {

    }

    public TutorialFragment(int page) {
        this.mPage = page;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("page", mPage);
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int resId = R.layout.tutorial_fragment1;
        if(savedInstanceState != null) {
            mPage = savedInstanceState.getInt("page");
        }
        switch (mPage) {
            case 0:
                resId = R.layout.tutorial_fragment1;
                break;
            case 1:
                resId = R.layout.tutorial_fragment2;
                break;
            case 2:
                resId = R.layout.tutorial_fragment3;
                break;
            default:
                resId = R.layout.tutorial_fragment1;
                break;
        }
        mRootView = inflater.inflate(resId, container, false);


        if (mPage == 2) {

            Button endTutorialBtn = (Button) mRootView.findViewById(R.id.endTutorialBtn);
            final CheckBox dontShowAgain = (CheckBox) mRootView.findViewById(R.id.dontShowTutorialAgain);
            dontShowAgain.setChecked(!SharedPreferencesUtils.isFirstLaunch(getActivity()));
            endTutorialBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                    SharedPreferencesUtils.setFirstTimeLaunch(getActivity(), !dontShowAgain.isChecked());

                }
            });
        }
        return mRootView;
    }



}
