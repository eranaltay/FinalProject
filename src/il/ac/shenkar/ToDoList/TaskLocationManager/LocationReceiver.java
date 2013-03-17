/*
 * LocationReceiver.java
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


package il.ac.shenkar.ToDoList.TaskLocationManager;

import il.ac.shenkar.ToDoList.R;
import il.ac.shenkar.ToDoList.ShowTask.ShowTaskActivity;
import il.ac.shenkar.ToDoList.TaskDAO.TaskObject;
import il.ac.shenkar.ToDoList.TaskList.TaskListActivity;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class LocationReceiver extends BroadcastReceiver
{

	private  TaskObject taskToNotify;
	private NotificationManager notificationManager;
	private Builder builder;
	private Notification notification;
	private Intent taskListIntent;
	private PendingIntent taskListPendingIntent;
	private Intent showTaskIntent;
	private PendingIntent showTaskPendingIntent;

	
	/**
	 * 
	 * 
	 */
		@Override 
    public void onReceive(Context context, Intent intent)
	{
		 try 
		 {

			 taskToNotify = intent.getParcelableExtra("taskToSetProximity");
			 		
			 int id = (int) taskToNotify.getTaskId();	 
		
			 notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

			  
			  //Configure what activty will launch after tapping the notification
			  taskListIntent = new Intent(context, TaskListActivity.class);
			  taskListPendingIntent = PendingIntent.getActivity(context, id, taskListIntent, 0);
 
			  showTaskIntent = new Intent(context,ShowTaskActivity.class);
			  showTaskIntent.putExtra("tasktoshow", taskToNotify);
			  showTaskPendingIntent = PendingIntent.getActivity(context, id, showTaskIntent, 0);
			  
			  
			  buildNotification(context);
			  //sending the notification and id of the task
			  notificationManager.notify(id, notification);
	
			  Log.i(getClass().getSimpleName(), "notification was sent from the system for task id " + id);
			  Intent sender = new Intent(context, TaskLocationService.class);
			  sender.putExtra("taskToRemoveIndicator", taskToNotify);
			  context.startService(sender);
			  
		    } 
		 
		 catch (Exception e) 
		    {
			     Toast.makeText(context, "There was an error somewhere, but we still received an alarm", Toast.LENGTH_SHORT).show();
			     Log.e(getClass().getSimpleName(), "Broadcast receiver error");
			     e.printStackTrace();
		    }
	}

		public void buildNotification(Context context)
		{
			 builder = new Notification.Builder(context)
			    .setContentIntent(showTaskPendingIntent)
			    .setTicker(context.getString(R.string.appName))
			    .setContentTitle(taskToNotify.getTaskTitle())
			    .setSmallIcon(R.drawable.proximityindicator)
			    .setDefaults(-1)
			    .addAction(R.drawable.notificationicon, "Launch " 
			    		+ context.getString(R.string.appName), taskListPendingIntent)
			    .setAutoCancel(true);
			 
			 notification = new Notification.InboxStyle(builder)
			  	.addLine("Description: ")
			    .addLine(taskToNotify.getTaskDescription())
			    .setSummaryText("@" + taskToNotify.getTaskLocation()).build();
		}
}
