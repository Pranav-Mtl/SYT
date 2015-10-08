package com.syt.balram.syt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import com.syt.balram.syt.BE.SellerQuestionBE;
import com.syt.dialog.TransparentProgressDialog;


public class SellerSubCategory extends AppCompatActivity {
    Context context;
    String SubChild;
    SellerQuestionBE objSellerQuestionBE;
    TextView tvTitle;
    SubCategoryAdapter objSubCategoryAdapter;
    TransparentProgressDialog pd;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_sub_category);

        context=getApplicationContext();

        tvTitle= (TextView) findViewById(R.id.seller_subcategoryTitlenew);
        back= (ImageButton) findViewById(R.id.seller_subcategory2_back);

        SubChild=getIntent().getExtras().get("SubChild").toString();
        pd=new TransparentProgressDialog(SellerSubCategory.this,R.drawable.logo_single);

        Intent intent1=getIntent();
        objSellerQuestionBE=(SellerQuestionBE)intent1.getSerializableExtra("SellerQuestionBE");

        pd.show();

        final SubCategoryAdapter objSubCategoryAdapter = new SubCategoryAdapter(context,SubChild);
        ListView listview = (ListView) findViewById(R.id.seller_subcategory_listview);

        tvTitle.setText(SubChild);
        listview.setAdapter(objSubCategoryAdapter);

        pd.dismiss();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                objSubCategoryAdapter.setSelection(position);
                String subChild=SubCategoryAdapter.txtItemList[position];

                if(subChild.trim().equalsIgnoreCase("Others"))
                {
                    //Toast.makeText(SellerSubCategory.this, "Others CLicked", Toast.LENGTH_LONG).show();
                    //showDialog(getApplicationContext());
                    initiatePopupWindow(subChild);
                }
                else {
                    objSellerQuestionBE.setSubCategory(SubCategoryAdapter.txtItemList[position]);
                    Intent intent = new Intent(SellerSubCategory.this, SellerQuestionThree.class);
                    intent.putExtra("SellerQuestionBE", objSellerQuestionBE);
                    startActivity(intent);
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_seller_sub_category, menu);
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
            LayoutInflater inflater = (LayoutInflater) SellerSubCategory.this
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
                        objSellerQuestionBE.setSubCategory(subcategory);
                        Intent intent = new Intent(SellerSubCategory.this, SellerQuestionThree.class);
                        intent.putExtra("SellerQuestionBE", objSellerQuestionBE);
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
