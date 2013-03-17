/*
 * TaskAlarmService.java
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

import il.ac.shenkar.ToDoList.TaskDAO.TaskDataBaseSQL;
import il.ac.shenkar.ToDoList.TaskDAO.TaskObject;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class TaskAlarmService extends IntentService
{
	TaskDataBaseSQL myDataBase; 
	public TaskAlarmService() 
	{
		super("TaskAlarm");
	}
	

	@Override
	protected void onHandleIntent(Intent intent)
	{
		 myDataBase = new TaskDataBaseSQL(this);
		 
		 TaskObject ob =intent.getParcelableExtra("taskToRemoveIndicator");
					 
		 //TaskObject ob = (TaskObject) intent.getSerializableExtra("taskToRemoveIndicator");
		 ob.setTaskNotificationDate(null);
		 myDataBase.open();
		 myDataBase.updateTask(ob);
		 myDataBase.close();
	     Log.i(getClass().getSimpleName(), "Alarm Service For Task: " + ob.getTaskId());

	}
}
