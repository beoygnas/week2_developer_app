package com.example.week2_developer_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;


public class AdapterChatmsg extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable{

    Context context;
    private ArrayList<Chatmsg> chatmsgs_filtered = new ArrayList<Chatmsg>();
    private ArrayList<Chatmsg> chatmsgs = new ArrayList<Chatmsg>();
    private ArrayList<Chatmsg> chatmsgs_list = new ArrayList<Chatmsg>();




    public AdapterChatmsg(ArrayList<Chatmsg> myData){
        this.context = context;
        this.chatmsgs_filtered = myData;
        this.chatmsgs = myData;
        this.chatmsgs_list.addAll(myData);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(viewType == 1) {
            view = inflater.inflate(R.layout.item_chatmsgmy, parent, false);
            return new ViewHoldermy(view);
        }
        else {
            view =  inflater.inflate(R.layout.item_chatmsgoppo, parent, false);
            return new ViewHolderoppo(view);
        }
    }

    @Override
    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();

                if(charString.isEmpty()) {
                    chatmsgs_filtered = chatmsgs_list;
                } else {
                    ArrayList<Chatmsg> filteringList = new ArrayList<>();
                    for(Chatmsg chatmsg : chatmsgs_list) {
                        if(chatmsg.getMsg().toLowerCase().contains(charString.toLowerCase())){
                            filteringList.add(chatmsg);
                        }
                    }
                    chatmsgs_filtered = filteringList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = chatmsgs_filtered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                chatmsgs.clear();
                chatmsgs.addAll((ArrayList<Chatmsg>)results.values);
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ViewHoldermy) {
            ((ViewHoldermy) holder).msg_id.setText(Integer.toString(chatmsgs.get(position).getMsg_id()));
            ((ViewHoldermy) holder).msg.setText(chatmsgs.get(position).getMsg());
            ((ViewHoldermy) holder).name.setText(chatmsgs.get(position).getSender_name());
            ((ViewHoldermy) holder).chat_id.setText(Integer.toString(chatmsgs.get(position).getChat_id()));
            ((ViewHoldermy) holder).regdata.setText(chatmsgs.get(position).getRegdata());
            if(chatmsgs.get(position).getConnected() == 1)
                ((ViewHoldermy) holder).imagebtn.setImageResource(R.drawable.icon_greendot);

        }
        else {
            ((ViewHolderoppo) holder).msg_id.setText(Integer.toString(chatmsgs.get(position).getMsg_id()));
            ((ViewHolderoppo) holder).msg.setText(chatmsgs.get(position).getMsg());
            ((ViewHolderoppo) holder).name.setText(chatmsgs.get(position).getSender_name());
            ((ViewHolderoppo) holder).chat_id.setText(Integer.toString(chatmsgs.get(position).getChat_id()));
            ((ViewHolderoppo) holder).regdata.setText(parseRegData(chatmsgs.get(position).getRegdata()));
            if(chatmsgs.get(position).getConnected() == 1)
                ((ViewHolderoppo) holder).imagebtn.setImageResource(R.drawable.icon_greendot);
        }
    }

    public String parseRegData(String regdata){
        String year = regdata.substring(0, 4);
        String month = regdata.substring(4, 6);
        String day = regdata.substring(6, 8);
        String hour = regdata.substring(8, 10);
        String minute = regdata.substring(10, 12);
        return month+"-"+day+" "+hour+":"+minute;
    }

    @Override
    public int getItemCount() {
        return chatmsgs.size();
    }
    @Override
    public int getItemViewType(int position){
        return chatmsgs.get(position).getviewType();
    }

    static class ViewHoldermy extends RecyclerView.ViewHolder {

        TextView msg_id;
        TextView msg;
        TextView chat_id;
        TextView name;
        ImageButton imagebtn;
        TextView regdata;

        ViewHoldermy(@NonNull View itemView) {
            super(itemView);
            chat_id = (TextView) itemView.findViewById(R.id.chat_id);
            msg = (TextView) itemView.findViewById(R.id.msg);
            msg_id = (TextView) itemView.findViewById(R.id.msg_id);
            imagebtn = (ImageButton) itemView.findViewById(R.id.imagebtn);
            name = (TextView) itemView.findViewById(R.id.name);
            regdata = (TextView) itemView.findViewById(R.id.regdata);
        }
    }

    static class ViewHolderoppo extends RecyclerView.ViewHolder {

        TextView msg_id;
        TextView msg;
        TextView chat_id;
        TextView name;
        ImageButton imagebtn;
        TextView regdata;

        ViewHolderoppo(@NonNull View itemView) {
            super(itemView);
            chat_id = (TextView) itemView.findViewById(R.id.chat_id);
            msg = (TextView) itemView.findViewById(R.id.msg);
            msg_id = (TextView) itemView.findViewById(R.id.msg_id);
            imagebtn = (ImageButton) itemView.findViewById(R.id.imagebtn);
            name = (TextView) itemView.findViewById(R.id.name);
            regdata = (TextView) itemView.findViewById(R.id.regdata);
        }
    }
}
