package com.syt.balram.syt;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.syt.balram.syt.BE.FollowerListBE;
import com.syt.constant.Constant;

import java.util.List;

/**
 * Created by Balram on 5/26/2015.
 */
public class FollowerListAdapter  extends ArrayAdapter<FollowerListBE>{

    private final List<FollowerListBE> list;
    private final Activity context;
    boolean checkAll_flag = false;
    boolean checkItem_flag = false;
    String emaiid;
    FollowerList objFollowerList;

    public FollowerListAdapter(Activity context, List<FollowerListBE> list,FollowerList followerList) {
        super(context, R.layout.follower_list_raw, list);
        this.context = context;
        this.list = list;
        objFollowerList=followerList;
    }

    static class ViewHolder {
        protected TextView text;
        protected CheckBox checkbox;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            convertView = inflator.inflate(R.layout.follower_list_raw, null,false);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView.findViewById(R.id.code);
            viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.checkBox1);
            viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    int getPosition = (Integer) buttonView.getTag();  // Here we get the position that we have set for the checkbox using setTag.
                    list.get(getPosition).setSelected(buttonView.isChecked()); // Set the value of checkbox to maintain its state.
                }
            });
            convertView.setTag(viewHolder);
            convertView.setTag(R.id.code, viewHolder.text);
            convertView.setTag(R.id.checkBox1, viewHolder.checkbox);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.checkbox.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v ;
                //Country country = (Country) cb.getTag();
                if(cb.isChecked())
                {
                    //System.out.println("Position"+position);
                   // Toast.makeText(context,Constant.followIdArray[position]+Constant.followLastNameArray[position],Toast.LENGTH_LONG).show();
                    objFollowerList.responseText.append(Constant.followIdArray[position] + "/");
                }
                        /*Toast.makeText(getApplicationContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_SHORT).show();*/
                //country.setSelected(cb.isChecked());
            }
        });
        viewHolder.checkbox.setTag(position); // This line is important.

        viewHolder.text.setText(list.get(position).getName()+" "+Constant.followLastNameArray[position]);
        viewHolder.checkbox.setChecked(list.get(position).isSelected());


        convertView.setLayoutParams(new ListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, 100));

        return convertView;
    }
}
