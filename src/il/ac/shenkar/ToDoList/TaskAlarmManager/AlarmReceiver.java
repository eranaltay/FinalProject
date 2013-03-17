/*
 * AlarmReceiver.java
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

package il.ac.shenkar.ToDoList.TaskAlarmManager;

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

/**
 * ReminderReciver, activate the following, when user get the alarm on his device
 * @author EranAltay
 *
 */
public class AlarmReceiver extends BroadcastReceiver 
{
	private NotificationManager notificationManager;
	private TaskObject taskToNotify;
	private Builder builder;
	private Notification notification;
	private Intent taskListIntent;
	private PendingIntent taskListPendingIntent;
	private Intent showTaskIntent;
	private PendingIntent showTaskPendingIntent;

	
	/**
	 * Configure the the title of the notification, icon, message string, and which activity
	 * to launch when tapping the received notification
	 */
	@Override 
    public void onReceive(Context context, Intent intent)
	{		
		taskToNotify = intent.getParcelableExtra("taskToSetAlarm");
		buildNotification(context);
	}
	
	public void buildNotification(Context context)
	{
		 try 
		 {
			//get the Serializable object from the createactivity							
			 int id = (int) taskToNotify.getTaskId();
			
			 //Configure which alarm will show up, in this case notification
		     notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			   
		      taskListIntent = new Intent(context, TaskListActivity.class);
		   	  taskListPendingIntent = PendingIntent.getActivity(context, id, taskListIntent, 0);

			  showTaskIntent = new Intent(context,ShowTaskActivity.class);
			  showTaskIntent.putExtra("tasktoshow", taskToNotify);
			  showTaskPendingIntent = PendingIntent.getActivity(context, id, showTaskIntent, 0);
			  
			  builder = new Notification.Builder(context)
				    .setContentIntent(showTaskPendingIntent)
				    .setTicker(context.getString(R.string.appName))
				    .setContentTitle(taskToNotify.getTaskTitle())
				    .setSmallIcon(R.drawable.notifybell)
				    .setDefaults(Notification.DEFAULT_ALL)
				    .addAction(R.drawable.notificationicon, "Launch " 
				    		+ context.getString(R.string.appName), taskListPendingIntent)
				     .setAutoCancel(true); 
				 notification = new Notification.InboxStyle(builder)
				   .build();
				  
			  //sending the notification and id of the task
			  notificationManager.notify(id, notification);
			
			  Log.i(getClass().getSimpleName(), "notification was sent from the system for task id " + id);
					  
			  //cancel the alarm after receiving it
			  if(taskToNotify.getTaskInterval() == 0)
			  {
				  Intent sender = new Intent(context, TaskAlarmService.class);
				  sender.putExtra("taskToRemoveIndicator", taskToNotify);
				  context.startService(sender);
			  }
				  
		    } 
		 
		 catch (Exception e) 
		    {
			     Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
			     Log.e(getClass().getSimpleName(), "Broadcast receiver error");
			     e.printStackTrace();
		    }

	}
	
}
