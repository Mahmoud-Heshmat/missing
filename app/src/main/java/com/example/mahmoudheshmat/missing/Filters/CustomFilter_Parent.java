package com.example.mahmoudheshmat.missing.Filters;

import android.widget.Filter;

import com.example.mahmoudheshmat.missing.Adapters.parentAdapter;
import com.example.mahmoudheshmat.missing.Adapters.unknownAdapter;
import com.example.mahmoudheshmat.missing.Models.childItem;

import java.util.ArrayList;
import java.util.List;


public class CustomFilter_Parent  extends Filter {

    parentAdapter parentAdapter;
    unknownAdapter UnAdapter;
    List<childItem> filterList;

    public CustomFilter_Parent(List<childItem> filterList, parentAdapter parentAdapter)
    {
        this.parentAdapter=parentAdapter;
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
            List<childItem> filteredPlayers=new ArrayList<>();
            for (int i=0;i<filterList.size();i++)
            {
                //CHECK
                if(filterList.get(i).getUser_id().toUpperCase().contains(constraint))
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

        parentAdapter.items= (ArrayList<childItem>) results.values;
        parentAdapter.notifyDataSetChanged();
    }
}

