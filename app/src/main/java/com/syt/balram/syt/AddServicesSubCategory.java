package com.syt.balram.syt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.syt.balram.syt.BE.AddServicesBE;
import com.syt.dialog.TransparentProgressDialog;


public class AddServicesSubCategory extends ActionBarActivity {
    Context context;
    String SubChild;
    AddServicesBE objAddServicesBE;
    TextView tvTitle;
    SubCategoryAdapter objSubCategoryAdapter;
    TransparentProgressDialog pd;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_services_sub_category);

        context=getApplicationContext();

        SubChild=getIntent().getExtras().get("SubChild").toString();
        pd=new TransparentProgressDialog(AddServicesSubCategory.this,R.drawable.logo_single);

        Intent intent1=getIntent();
        objAddServicesBE=(AddServicesBE)intent1.getSerializableExtra("AddServicesBE");

        SubCategoryAdapter objSubCategoryAdapter=new SubCategoryAdapter(context,SubChild);
        ListView listview= (ListView) findViewById(R.id.subcategory_listview);
        ImageButton back= (ImageButton) findViewById(R.id.seller_addsubcategory2_back);
        TextView tv= (TextView) findViewById(R.id.subcategoryTitle);



        tv.setText(SubChild);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               finish();

            }
        });

        listview.setAdapter(objSubCategoryAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

               String subChild= SubCategoryAdapter.txtItemList[position];

                if(subChild.trim().equalsIgnoreCase("Others"))
                {
                    //Toast.makeText(SellerSubCategory.this, "Others CLicked", Toast.LENGTH_LONG).show();
                    //showDialog(getApplicationContext());
                    initiatePopupWindow(subChild);
                }
                else {
                    objAddServicesBE.setSubCategory((SubCategoryAdapter.txtItemList[position]));
                    Intent intent = new Intent(AddServicesSubCategory.this, AddServicesThree.class);
                    intent.putExtra("AddServicesBE", objAddServicesBE);
                    startActivity(intent);
                }
            }
        });



        // find and initialize your TextView(s), EditText(s) and Button(s); setup their behavior

        // display your popup window



}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_services_sub_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private PopupWindow pwindo;

    private void initiatePopupWindow(String title) {
        final EditText edittext;
        TextView tvTitle;
        Button btnClosePopup,btnsave;
        try {
// We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) AddServicesSubCategory.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.seller_other_layout,
                    (ViewGroup) findViewById(R.id.popup_element));
            pwindo = new PopupWindow(layout, 500, 550, true);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

            edittext= (EditText) layout.findViewById(R.id.seller_other_category);
            btnClosePopup = (Button) layout.findViewById(R.id.seller_other_cancel);
            btnsave= (Button) layout.findViewById(R.id.seller_other_button);
            tvTitle= (TextView) layout.findViewById(R.id.seller_other_title);

            tvTitle.setText(title);

            btnClosePopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Toast.makeText(SellerQuestionExpandable.this,edittext.getText().toString(),Toast.LENGTH_LONG).show();
                    pwindo.dismiss();
                }
            });

            btnsave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String subcategory=edittext.getText().toString();
                    if(subcategory.trim().length()>0) {
                        objAddServicesBE.setSubCategory(subcategory);
                        Intent intent=new Intent(AddServicesSubCategory.this, AddServicesThree .class);
                        intent.putExtra("AddServicesBE",objAddServicesBE);
                        startActivity(intent);
                    }
                    pwindo.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
