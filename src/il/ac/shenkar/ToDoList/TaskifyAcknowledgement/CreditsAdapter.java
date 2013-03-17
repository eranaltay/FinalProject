/*
 * CreditsAdapter.java
 *
 * Copyright 2013 Eran Altay
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 1.13 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 */


package il.ac.shenkar.ToDoList.TaskifyAcknowledgement;

import il.ac.shenkar.ToDoList.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adapter for the Sliding menu 
 * 
 * 
 * @author EranAltay
 *
 */
public class CreditsAdapter extends ArrayAdapter<CreditsItems> 
{
	Context context; 
    int layoutResourceId;    
    CreditsItems data[] = null;
	
	public CreditsAdapter(Context context, int layoutResourceId, CreditsItems[] data)
	{
		super(context, layoutResourceId, data);
	    this.layoutResourceId = layoutResourceId;
	    this.context = context;
	    this.data = data;
	}
	
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CreditsItemsHolder holder = null;
        
        if(row == null)
        {
        	LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new CreditsItemsHolder();
            holder.txtItem = (TextView)row.findViewById(R.id.creditstxt);
            holder.bottomtext = (TextView)row.findViewById(R.id.bottomtext);
            
            row.setTag(holder);
        }
        else
        {
            holder = (CreditsItemsHolder)row.getTag();
        }
        
        CreditsItems creditItem = data[position];
        holder.txtItem.setText(creditItem.item);
        holder.bottomtext.setText(creditItem.subItem);
        
        return row;
    }
    
    static class CreditsItemsHolder
    {
        ImageView imgIcon;
        TextView txtItem;
        TextView bottomtext;
    }
}


