package com.syt.balram.syt;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.syt.balram.syt.BE.SellerFragmentBE;
import com.syt.balram.syt.BL.SellerFragmentBL;
import com.syt.constant.Constant;
import com.syt.dialog.TransparentProgressDialog;
import com.syt.util.Configuration;

public class SellerFragment extends Fragment {

    ListView lvLeads;
    SellerFragmentAdapter objSellerFragmentAdapter;
    SellerFragmentBE objSellerFragmentBE;
    SellerFragmentBL objSellerFragmentBL;

    ProgressDialog progressDialog;
    TransparentProgressDialog pd;
    AlertDialog alertDialog;


    public SellerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_seller, container, false);
        lvLeads= (ListView) view.findViewById(R.id.seller_leads);

        progressDialog=new ProgressDialog(getActivity(),R.style.MyDialogTheme);
        pd=new TransparentProgressDialog(getActivity(),R.drawable.logo_single);

        alertDialog = new AlertDialog.Builder(getActivity(),R.style.MyDialogTheme).create();

        if(Configuration.isInternetConnection(getActivity())) {
                    try {

                        new LongRunningGetIO().execute();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

            }
        else{

            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                    getActivity());

// Setting Dialog Title
            alertDialog2.setTitle(Constant.ERR_INTERNET_CONNECTION_NOT_FOUND);

// Setting Dialog Message
            alertDialog2.setMessage(Constant.ERR_INTERNET_CONNECTION_NOT_FOUND_MSG);

// Setting Icon to Dialog

            alertDialog2.setIcon(R.drawable.no_internet);
// Setting Positive "Yes" Btn
            alertDialog2.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog
                            startActivity(new Intent(Settings.ACTION_SETTINGS));
                        }
                    });
// Setting Negative "NO" Btn
            alertDialog2.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog
                            Toast.makeText(getActivity(),
                                    "You clicked on NO", Toast.LENGTH_SHORT)
                                    .show();
                            dialog.cancel();

                        }
                    });

// Showing Alert Dialog
            alertDialog2.show();


        }






        return  view;
    }




    public class SellerFragmentAdapter extends BaseAdapter {

        Context mContext;
        TextView txtName,txtSummary,txtReadMore;




        public SellerFragmentAdapter(Context context)
        {
            mContext=context;
            objSellerFragmentBE=new SellerFragmentBE();
            objSellerFragmentBL=new SellerFragmentBL();
            try {

                String gg=getSellerJson();
            }
            catch (Exception e)
            {

            }



        }
        @Override
        public int getCount() {
            return Constant.sellerNameArray.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater infalInflater = (LayoutInflater) mContext
                    .getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

            View gridView=null;
            //TextView tv,tv1;
            if (convertView != null){

                gridView=convertView;

            }else{
                gridView = new View(mContext);
                gridView= infalInflater.inflate(R.layout.buyer_leads_rawlist, null);

            }
            txtName = (TextView) gridView .findViewById(R.id.buy_leads_name);
            txtSummary= (TextView) gridView.findViewById(R.id.buy_leads_summary);
            txtReadMore= (TextView) gridView.findViewById(R.id.buy_leads_read);

            txtName.setText(Constant.sellerNameArray[position]);
            String summary="Looking for "+Constant.sellerSubCategoryArray[position]+" at"+" "+Constant.sellerZipArray[position]+" location";
            String summaryNew="Signed up under "+Constant.sellerSubCategoryArray[position]+" category in "+Constant.sellerZipArray[position]+" location";
            txtSummary.setText(summaryNew);

            gridView.setLayoutParams(new ListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
            return gridView;
        }
    }


    private class LongRunningGetIO extends AsyncTask<String, String, String> {

        // ProgressDialog progress;

        @Override
        protected void onPreExecute() {

            pd.show();

           /* progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
*/
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            objSellerFragmentAdapter=new SellerFragmentAdapter(getActivity());

           return "";
        }

        @Override
        protected void onPostExecute(String result) {

           // progressDialog.dismiss();

            pd.dismiss();

            lvLeads.setAdapter(objSellerFragmentAdapter);

            lvLeads.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent=new Intent(getActivity(),SellerProfile.class);
                    intent.putExtra("ID",Constant.sellerIdArray[position]);
                    startActivity(intent);
                }
            });


        }
    }


    private String getSellerJson()
    {
        String result=objSellerFragmentBL.getsellerData(objSellerFragmentBE);

        return result;

    }


}


