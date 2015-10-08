package com.syt.balram.syt;


import android.app.AlertDialog;
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

import com.syt.balram.syt.BL.BuyerFragmentBL;
import com.syt.constant.Constant;
import com.syt.dialog.TransparentProgressDialog;
import com.syt.util.Configuration;


public class BuyerFragment extends Fragment {

    ListView lvLeads;

    BuyerFragmentAdapter objBuyerFragmentAdapter;

   // ProgressDialog progressDialog;
    TransparentProgressDialog pd;
    AlertDialog alertDialog;



    public BuyerFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_buyer, container, false);
        lvLeads= (ListView) view.findViewById(R.id.buyer_leads);

        alertDialog = new AlertDialog.Builder(getActivity()).create();

       // progressDialog=new ProgressDialog(getActivity());
        pd=new TransparentProgressDialog(getActivity(),R.drawable.logo_single);

        try
        {
            pd.show();

            if(Configuration.isInternetConnection(getActivity())) {
                new LongRunningGetIO().execute();
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
        }
        catch (Exception e)
        {

        }





        return  view;
    }

    // TODO: Rename method, update argument and hook method into UI event


    public static class BuyerFragmentAdapter extends BaseAdapter {

        Context mContext;
        TextView txtName,txtSummary,txtReadMore;
        TextView txtPost;
        public static String[] txtItemList;
        public static String[] txtItemListSummary;

        BuyerFragmentBL objBuyerFragmentBL;


        public BuyerFragmentAdapter(Context context)
        {
            mContext=context;

            objBuyerFragmentBL=new BuyerFragmentBL();

            try {

                String gg=getBuyerJson();
            }
            catch (Exception e)
            {

            }





        }
        @Override
        public int getCount() {
            return Constant.buyerNameArray.length;
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
            txtPost= (TextView) gridView.findViewById(R.id.buy_leads_read);

            System.out.println("Name"+Constant.buyerNameArray[position]);
            System.out.println("Sunnary"+Constant.buyerCategoryArray[position]);


            txtName.setText(Constant.buyerNameArray[position]);
            txtSummary.setText("Posted a "+Constant.buyerSubCategoryArray[position]+" requirement at "+Constant.buyerZipArray[position]+" location.");

            txtPost.setText("View post");

            gridView.setLayoutParams(new ListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT,AbsListView.LayoutParams.WRAP_CONTENT));
            return gridView;
        }

        private String getBuyerJson()
        {
            String result=objBuyerFragmentBL.getsellerData();

            return result;

        }


    }


    private class LongRunningGetIO extends AsyncTask<String, String, String> {

        // ProgressDialog progress;

        @Override
        protected void onPreExecute() {




        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            objBuyerFragmentAdapter=new BuyerFragmentAdapter(getActivity());

            return "";
        }

        @Override
        protected void onPostExecute(String result) {

           pd.dismiss();

            lvLeads.setAdapter(objBuyerFragmentAdapter);

            lvLeads.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent=new Intent(getActivity(),BuyerProfile.class);
                    intent.putExtra("ID", Constant.buyerIdArray[position]);
                    intent.putExtra("MyPost","allPost");
                    startActivity(intent);
                }
            });


        }
    }



}
