/*
 * TaskListBaseAdapter.java
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package il.ac.shenkar.ToDoList.TaskList;

import il.ac.shenkar.ToDoList.R;
import il.ac.shenkar.ToDoList.TaskDAO.TaskDataBaseSQL;
import il.ac.shenkar.ToDoList.TaskDAO.TaskList;
import il.ac.shenkar.ToDoList.TaskDAO.TaskObject;
import il.ac.shenkar.ToDoList.TaskUtilities.MyConstants;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author EranAltay
 *
 */
public class TaskListBaseAdapter extends BaseAdapter 
{
	private LayoutInflater layoutInflater;
	private ArrayList<TaskObject> taskitems;
	TaskDataBaseSQL myDataBase;

	
	public TaskListBaseAdapter(Context context, TaskDataBaseSQL db)
	{
		this.layoutInflater = LayoutInflater.from(context);
		this.taskitems = TaskList.getTasksList();
		myDataBase = db;
	}

	static class ViewHolder 
	{
		TextView taskItem_txt;
		ImageView importantIndicator;
		ImageView alarmIndicator;
		ImageView proximityIndicator;
		ImageView repeatIndicator;
	}

	public ArrayList<TaskObject> getlist()
	{
		return taskitems;
	}

	public int getCount()
	{
		return getlist().size();
	}

	public Object getItem(int position) 
	{
		return getlist().get(position);
	}
	
	

	public long getItemId(int position)
	{
		return position;
	}


	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		if (convertView == null) 
		{
			convertView = layoutInflater.inflate(R.layout.taskitems, null);
			holder = new ViewHolder();
			holder.taskItem_txt = (TextView) convertView.findViewById(R.id.itemoflist);
			holder.importantIndicator = (ImageView) convertView.findViewById(R.id.importanceleft);
			holder.alarmIndicator = (ImageView) convertView.findViewById(R.id.alarmimg);
			holder.proximityIndicator = (ImageView) convertView.findViewById(R.id.proximityimg);
			holder.repeatIndicator = (ImageView) convertView.findViewById(R.id.repeatindicator);
     		convertView.setTag(holder);	  		   		
		} 
		
		else 
		 	holder = (ViewHolder) convertView.getTag();	

			TaskObject tempTask = getlist().get(position);
	
			holder.taskItem_txt.setText(tempTask.getTaskTitle());
			
			int isDone	=  tempTask.getTaskDoneIndicator();

				
			if(isDone == MyConstants.ISDONE)
			{
				holder.taskItem_txt.setPaintFlags(holder.taskItem_txt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
				holder.taskItem_txt.setTextColor(Color.GRAY);
			    holder.importantIndicator.setImageResource(android.R.color.transparent);
			}
		
			else
			{
				holder.taskItem_txt.setPaintFlags(holder.taskItem_txt.getPaintFlags() 
					& (~ Paint.STRIKE_THRU_TEXT_FLAG));
				holder.taskItem_txt.setTextColor(Color.WHITE);
			}
			
			if(tempTask.getTaskImportantIndicator() == MyConstants.ISIMPORTANT)
			{
				holder.taskItem_txt.setPaintFlags(holder.taskItem_txt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
				holder.importantIndicator.setImageResource(R.drawable.important);
			}
			
			else
			{
				holder.taskItem_txt.setPaintFlags(holder.taskItem_txt.getPaintFlags() & ~Paint.UNDERLINE_TEXT_FLAG);
				holder.importantIndicator.setImageResource(android.R.color.transparent);
			}
			
			
			if(tempTask.getTaskNotificationDate() != null)
			{
				holder.alarmIndicator.setImageResource(R.drawable.time);
				if(tempTask.getTaskInterval() != 0)
				{
					holder.repeatIndicator.setImageResource(R.drawable.repeatsign);
				}
				
			}
			
			else 
			{
				holder.alarmIndicator.setImageResource(android.R.color.transparent);
				holder.repeatIndicator.setImageResource(android.R.color.transparent);
			}
			
			
			
			if(tempTask.getIfTaskHasProximity() == MyConstants.HASPROXIMITY)
				holder.proximityIndicator.setImageResource(R.drawable.proximityindicator);
	
			
			else
				holder.proximityIndicator.setImageResource(android.R.color.transparent);

		return convertView;
	}

}
