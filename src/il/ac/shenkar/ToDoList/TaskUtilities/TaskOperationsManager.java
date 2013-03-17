/*
 * TaskOperationsManager.java
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

package il.ac.shenkar.ToDoList.TaskUtilities;

import il.ac.shenkar.ToDoList.R;
import il.ac.shenkar.ToDoList.ITaskInterfaces.ITaskOperations;
import il.ac.shenkar.ToDoList.TaskAlarmManager.MyAlarmManager;
import il.ac.shenkar.ToDoList.TaskDAO.TaskDataBaseSQL;
import il.ac.shenkar.ToDoList.TaskDAO.TaskList;
import il.ac.shenkar.ToDoList.TaskDAO.TaskObject;
import il.ac.shenkar.ToDoList.TaskList.TaskListActivity;
import il.ac.shenkar.ToDoList.TaskLocationManager.TaskGPSManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class TaskOperationsManager implements ITaskOperations
{
	TaskDataBaseSQL myDataBase;
	private Context context;
	private TaskGPSManager myTaskGPSManager;
	private MyAlarmManager myAlarmManager;
	
	public TaskOperationsManager(Context context)
	{
		this.context = context;
		myDataBase = new TaskDataBaseSQL(context);
		myTaskGPSManager = new TaskGPSManager(context);
		myAlarmManager = new MyAlarmManager(context);
	}


	public void changeTaskImportanceState(TaskObject chosenTask, int state)
	{
		chosenTask.setTaskImportantIndicator(state);
		myDataBase.open();
		myDataBase.updateTask(chosenTask);		
		myDataBase.close();
	}


	public void changeTaskDoneState(TaskObject chosenTask, int state)
	{
		chosenTask.setTaskDoneIndicator(state);
		chosenTask.setTaskImportantIndicator(MyConstants.NOTIMPORTANT);
		myDataBase.open();
		myDataBase.updateTask(chosenTask);
		myDataBase.close();	
	}

     
	public void soundOfDeletion()
	{
  	    MediaPlayer mediaplayer;
  	    //delete from database and tasklist 
  	    mediaplayer = MediaPlayer.create(context, R.raw.recycle2); 
  	    mediaplayer.start();		
	}
	
	
	public void removeTask(int position)
	{
		TaskObject chosenTask =  TaskList.getTasksList().get(position);   
		boolean notificationState = chosenTask.getTaskNotificationDate() != null;
		boolean proximityState = chosenTask.getIfTaskHasProximity() == MyConstants.HASPROXIMITY;

		//if has notification, first cancel the notification itself
		if(notificationState == true)
		{
			myAlarmManager.CancelAlarm(chosenTask);
			Log.i(getClass().getSimpleName(), "Removed alarm task with id " + chosenTask.getTaskId());																						
		}
	        
		if(proximityState == true)
		{
			myTaskGPSManager.cancelProximityAlert(chosenTask);
		}
		
 	  myDataBase.open();
      myDataBase.deleteTask(chosenTask.getTaskId());
      myDataBase.close();
      TaskList.getTasksList().remove(position);
      soundOfDeletion();
	}
	
	
	public void shareTask(TaskObject chosenTask)
	{
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        
         sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,chosenTask.getTaskTitle() + "\n" + chosenTask.getTaskDescription()+ "\nShared By " + context.getString(R.string.appName));
        context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
	}
	
	
	public void shareList()
	{
		StringBuilder tasksToShare = new StringBuilder();
		if(TaskList.getTasksList().isEmpty())
		{
			toastNotification("Your Task List Is Empty");
			return;
		}
		
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		
		for (int i = 0; i < TaskList.getTasksList().size(); i++) 
		{
		    if(TaskList.getTasksList().get(i).getTaskDoneIndicator() != MyConstants.ISDONE)
		    {
		    	String title = TaskList.getTasksList().get(i).getTaskTitle();
		    	String description = TaskList.getTasksList().get(i).getTaskDescription();
		    	
		    	tasksToShare.append(i+1 + ")" + title + "\n" + description + "\n");
		    }
		}
		
	       sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, tasksToShare.toString()+ "\nShared By " + context.getString(R.string.appName));
	       context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
	}
	
	
	public void deleteDoneTasks()
	{
		if(TaskList.getTasksList().isEmpty())
		{
			toastNotification("Your Task List Is Empty");
			return;
		}
	
		 myDataBase.open();
		
		for (int i = 0; i < TaskList.getTasksList().size(); i++) 
		{
		    if(TaskList.getTasksList().get(i).getTaskDoneIndicator() == MyConstants.ISDONE)
		    {
		    	cancalNotificationOnDeleting(i);
		       	myDataBase.deleteTask(TaskList.getTasksList().get(i).getTaskId());  
		     	TaskList.getTasksList().remove(i);    
		    }
		}

		myDataBase.close();
		soundOfDeletion();
		return;
	}
	
	
	public void clearList()
	{
		if(TaskList.getTasksList().isEmpty())
		{
			toastNotification("Your Task List Is Empty");
			return;
		}
		
		for (int i = 0; i < TaskList.getTasksList().size(); i++) 
		{
			cancalNotificationOnDeleting(i);
			TaskList.getTasksList().remove(i);	
		}
		
		toastNotification("Task List Is Clear!");
		TaskList.getTasksList().clear();
		myDataBase.open();
		myDataBase.deleteDatabse();
		myDataBase.close();
		soundOfDeletion();
	}
	
	
	public void cancalNotificationOnDeleting(int index)
	{
		 if(TaskList.getTasksList().get(index).getTaskNotificationDate() != null)
			 myAlarmManager.CancelAlarm(TaskList.getTasksList().get(index)); 		
		 
		 if(TaskList.getTasksList().get(index).getIfTaskHasProximity() == MyConstants.HASPROXIMITY)
			 myTaskGPSManager.cancelProximityAlert(TaskList.getTasksList().get(index));			 
	}
	
	public void toastNotification(String msg)
	{
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();		
	}
		
	

	public static void showUndo(final View viewUndoFeature)
	{
		viewUndoFeature.setVisibility(View.VISIBLE);
		viewUndoFeature.setAlpha(1);
		viewUndoFeature.animate().alpha(0.4f).setDuration(5000)
				.withEndAction(new Runnable()
				{
					public void run() 
					{
						viewUndoFeature.setVisibility(View.GONE);
					}
				});

	}
		

	
}
