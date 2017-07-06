package com.example.mahmoudheshmat.missing.Filters;


import android.widget.Filter;

import com.example.mahmoudheshmat.missing.Adapters.chat_adapter;
import com.example.mahmoudheshmat.missing.Models.chat_items;

import java.util.ArrayList;
import java.util.List;


public class CustomFilter_chat extends Filter {

    chat_adapter adapter;
    List<chat_items> filterList;


    public CustomFilter_chat(List<chat_items> filterList, chat_adapter adapter)
    {
        this.adapter=adapter;
        this.filterList=filterList;
    }

    //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            constraint=constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            List<chat_items> filteredPlayers=new ArrayList<>();
            for (int i=0;i<filterList.size();i++)
            {
                //CHECK
                if(filterList.get(i).getUser_name().toUpperCase().contains(constraint))
                {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredPlayers.add(filterList.get(i));
                }
            }
            results.count=filteredPlayers.size();
            results.values=filteredPlayers;
        }else {
            results.count=filterList.size();
            results.values=filterList;
        }
        return results;
    }
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.items= (ArrayList<chat_items>) results.values;
        adapter.notifyDataSetChanged();
    }
}

