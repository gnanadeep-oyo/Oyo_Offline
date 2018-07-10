package com.journaldev.gpslocationtracking;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<ChatBubble> {

    private Activity activity;
    private List<ChatBubble> messages;

    public MessageAdapter(Activity context, int resource, List<ChatBubble> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.messages = objects;
    }



    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        ChatBubble message = messages.get(i);

        if (message.myMessage()) { // this message was sent by us so let's create a basic chat bubble on the right

            convertView = messageInflater.inflate(R.layout.left_chat_bubble, null);
            holder.msg = (TextView) convertView.findViewById(R.id.txt_msg);
            holder.htl=(TextView)convertView.findViewById(R.id.txt_msg1);
            String getmsg=message.getContent();

            if(getmsg.contains("OYO_ID")) {

                int index_id=getmsg.indexOf("OYO_ID");
                String[] temp=getmsg.substring(index_id).split("#");

                holder.msg.setText(temp[2]+" ★"+"\n"+temp[0]+"\nDistance: "+temp[1]+"\n"+temp[3]);

                holder.htl.setText(getmsg.substring(0,index_id-1));
            }
            else {
                holder.htl.setText("");
                if(!getmsg.contains("Unable"))
                holder.msg.setText("Dear user,confirmed your booking: "+getmsg);
                else
                 holder.msg.setText(getmsg);

            }



            convertView.setTag(holder);

        } else { // this message was sent by someone else so let's create an advanced chat bubble on the left
            convertView = messageInflater.inflate(R.layout.right_chat_bubble, null);
            holder.msg = (TextView) convertView.findViewById(R.id.txt_msg);

            holder.msg.setText(message.getContent());
            convertView.setTag(holder);


        }

        return convertView;
    }



    @Override
    public int getViewTypeCount() {
        // return the total number of view types. this value should never change
        // at runtime. Value 2 is returned because of left and right views.
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        // return a value between 0 and (getViewTypeCount - 1)
        return position % 2;
    }
}
  class ViewHolder {
         public TextView msg;
         public TextView htl;
  }

