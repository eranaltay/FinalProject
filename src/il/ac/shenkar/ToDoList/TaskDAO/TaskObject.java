/*
 * TaskObject.java
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

package il.ac.shenkar.ToDoList.TaskDAO;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * 
 * @author EranAltay
 *
 */
public class TaskObject implements Parcelable
{

	private long taskID;
	private String taskTitle;
	private String taskDescription = null;
	private String taskLocation = null;
	private int proximity;
	private String notificationDate = null;
	private int intervalOfAlarm;
	private int doneTask;
	private int importantTask;

	
	public TaskObject() { ; };
	

	public TaskObject(long id ,String taskTitle, String taskDescription, String taskLocation, 
			int proximity, String notificationDate, int interval, int doneTask, int importantTask)
	{
		setTaskId(id);
		setTaskTitle(taskTitle);
		setTaskDescription(taskDescription);
		setTaskLocation(taskLocation);
		setTaskProximity(proximity);
		setTaskNotificationDate(notificationDate);
		setTaskInterval(interval);
		setTaskDoneIndicator(doneTask);
		setTaskImportantIndicator(importantTask);
		
	}

	public long getTaskId()
	{
		return taskID;
	}


	public void setTaskId(long id)
	{
		this.taskID = id;
	}

	public String getTaskTitle()
	{

		return taskTitle;
		
	}

	
	public void setTaskTitle(String titleTask)
	{
		this.taskTitle = titleTask;
		
	}
		
	
	public String getTaskDescription() 
	{
		
		
		return taskDescription;
		
	}

	
	public void setTaskDescription(String taskDescription)
	{
		this.taskDescription = taskDescription;
	}


	public String getTaskLocation() 
	{
		return taskLocation;
	}

	public void setTaskLocation(String taskLocation) 
	{
		this.taskLocation = taskLocation;
	}
	


	public String getTaskNotificationDate() 
	{
		return notificationDate;
	}


	public void setTaskNotificationDate(String notificationDate) 
	{
		this.notificationDate = notificationDate;
	}


	public int getTaskDoneIndicator() 
	{
		return doneTask;
	}


	public void setTaskDoneIndicator(int doneTask)
	{
		this.doneTask = doneTask;
	}


	public int getTaskImportantIndicator()
	{
		return importantTask;
	}


	public void setTaskImportantIndicator(int importantTask) 
	{
		this.importantTask = importantTask;
	}


	public int getTaskInterval()
	{
		return intervalOfAlarm;
	}


	public void setTaskInterval(int interval)
	{
		this.intervalOfAlarm = interval;
	}


	public int getIfTaskHasProximity()
	{
		return proximity;
	}


	public void setTaskProximity(int proximity) 
	{
		this.proximity = proximity;
	}

	
	// Parcelable section
	public TaskObject(Parcel in) 
	{
		readFromParcel(in);
	}
 
	private void readFromParcel(Parcel in)
	{
		 
		taskID = in.readLong();
		taskTitle = in.readString();
		taskDescription = in.readString();
		taskLocation = in.readString();
		proximity = in.readInt();
		notificationDate = in.readString();
		intervalOfAlarm = in.readInt();
		doneTask = in.readInt();
		importantTask = in.readInt();
		
	}
	
	public void writeToParcel(Parcel dest, int flags) 
	{
		dest.writeLong(taskID);
		dest.writeString(taskTitle);
		dest.writeString(taskDescription);
		dest.writeString(taskLocation);
		dest.writeInt(proximity);
		dest.writeString(notificationDate);
		dest.writeInt(intervalOfAlarm);
		dest.writeInt(doneTask);
		dest.writeInt(importantTask);
	}

	
	
	 public static final Parcelable.Creator CREATOR =
		    	new Parcelable.Creator() {
		            public TaskObject createFromParcel(Parcel in) {
		                return new TaskObject(in);
		            }
		 
		            public TaskObject[] newArray(int size) {
		                return new TaskObject[size];
		            }
		        };
	
	

		    	public int describeContents() 
		    	{
		    		// TODO Auto-generated method stub
		    		return 0;
		    	}

	
}
