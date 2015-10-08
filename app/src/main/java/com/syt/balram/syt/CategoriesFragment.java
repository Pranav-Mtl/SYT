package com.syt.balram.syt;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.syt.balram.syt.BE.ServiceProviderBE;
import com.syt.balram.syt.BL.ServiceProviderBL;
import com.syt.constant.Constant;
import com.syt.util.Configuration;


public class CategoriesFragment extends Fragment {


    TextView tvServiceProvider;
    ServiceProviderBE objServiceProviderBE;
    ServiceProviderBL objServiceProviderBL;
    ProgressDialog pd;
    public CategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_categories, container, false);
        WindowManager wm = (WindowManager) getActivity().getSystemService(getActivity().WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        if(width>1000)
        {
            Constant.CategoryImageWidth=460;
            Constant.CategoryImageHeight=360;
        }
        else if(width>=600 && height>=1024)
        {
            Constant.CategoryImageWidth=340;
            Constant.CategoryImageHeight=260;
        }
        else
        {
            Constant.CategoryImageWidth=220;
            Constant.CategoryImageHeight=160;
        }
        objServiceProviderBE=new ServiceProviderBE();
        objServiceProviderBL=new ServiceProviderBL();

        pd=new ProgressDialog(getActivity());

        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
        tvServiceProvider= (TextView) rootView.findViewById(R.id.tv_service_provider);

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        tvServiceProvider.startAnimation(anim);

        gridview.setAdapter(new ImageAdapter(getActivity()));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

               /* Toast.makeText(getActivity(), "you clicked on" + position,
                        Toast.LENGTH_SHORT).show();*/

                Configuration.setSharedPrefrenceValue(getActivity(), Constant.PREFS_NAME, Constant.SHARED_PREFERENCE_SearchTitle, "Category");

                if (position == 0) {
                    Intent intent = new Intent(getActivity(), CategoryTrainerTutor.class);
                    intent.putExtra("Title", "Trainers and Tutors");
                    startActivity(intent);
                } else if (position == 1) {
                    Intent intent = new Intent(getActivity(), CategoryTrainerTutor.class);
                    intent.putExtra("Title", "Business Consultants");
                    startActivity(intent);
                } else if (position == 2) {
                    Intent intent = new Intent(getActivity(), CategoryTrainerTutor.class);
                    intent.putExtra("Title", "IT and Marketing");
                    startActivity(intent);
                } else if (position == 3) {
                    Intent intent = new Intent(getActivity(), CategoryTrainerTutor.class);
                    intent.putExtra("Title", "Fashion and Lifestyle");
                    startActivity(intent);
                } else if (position == 4) {
                    Intent intent = new Intent(getActivity(), CategoryTrainerTutor.class);
                    intent.putExtra("Title", "Beauty and Wellness");
                    startActivity(intent);
                } else if (position == 5) {
                    Intent intent = new Intent(getActivity(), CategoryTrainerTutor.class);
                    intent.putExtra("Title", "Social Causes");
                    startActivity(intent);
                } else if (position == 6) {
                    Intent intent = new Intent(getActivity(), CategoryTrainerTutor.class);
                    intent.putExtra("Title", "Home and Utility");
                    startActivity(intent);
                } else if (position == 7) {
                    Intent intent = new Intent(getActivity(), CategoryTrainerTutor.class);
                    intent.putExtra("Title", "Events and Entertainment");
                    startActivity(intent);
                } else if (position == 8) {
                    Intent intent = new Intent(getActivity(), CategoryTrainerTutor.class);
                    intent.putExtra("Title", "Everything Else");
                    startActivity(intent);
                }
            }
        });

        tvServiceProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetServiceProvider().execute();
            }
        });


        return rootView;
    }

    private class GetServiceProvider extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            pd.show();
            pd.setMessage("Loading...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {

            String result=objServiceProviderBL.getSerViceProvider(objServiceProviderBE);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            pd.dismiss();
            try {
                Intent resultIntent = new Intent(getActivity(), ServiceProvider.class);
                resultIntent.putExtra("Message", objServiceProviderBE.getTitle());
                resultIntent.putExtra("Id", objServiceProviderBE.getId());
                startActivity(resultIntent);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}


