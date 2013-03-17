/*
 * MenuAdapter.java
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


package il.ac.shenkar.ToDoList.TaskList;

import il.ac.shenkar.ToDoList.R;
import android.app.Activity;
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
public class MenuAdapter extends ArrayAdapter<SlidingMenuItems> 
{
	Context context; 
    int layoutResourceId;    
    SlidingMenuItems data[] = null;
	
	public MenuAdapter(Context context, int layoutResourceId, SlidingMenuItems[] data)
	{
		super(context, layoutResourceId, data);
	    this.layoutResourceId = layoutResourceId;
	    this.context = context;
	    this.data = data;
	}
	
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MenuItemsHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new MenuItemsHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtItem = (TextView)row.findViewById(R.id.txtTitle);
            
            row.setTag(holder);
        }
        else
        {
            holder = (MenuItemsHolder)row.getTag();
        }
        
        SlidingMenuItems menuItem = data[position];
        holder.txtItem.setText(menuItem.item);
        holder.imgIcon.setImageResource(menuItem.icon);
        
        return row;
    }
    
    static class MenuItemsHolder
    {
        ImageView imgIcon;
        TextView txtItem;
    }
}


