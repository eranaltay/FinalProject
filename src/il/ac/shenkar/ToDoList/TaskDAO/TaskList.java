/*
 * TaskList.java
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/**
 * @author EranAltay
 * Singleton class of the list
 */
public class TaskList 
{
	private   static TaskList instance = new TaskList();
	private   static ArrayList<TaskObject> arrayOfTask;
	
	static 
	{
		arrayOfTask =  new ArrayList<TaskObject>();
	}

	public  static synchronized TaskList getInstance ()
	{
        return instance;
    }
	

	public static ArrayList<TaskObject> getTasksList()
	{
		return arrayOfTask;
	}
		
	
	public static TaskObject getTaskByID(long id)
	{
		Iterator<TaskObject> it = arrayOfTask.iterator();
		while(it.hasNext())
		{
			if(it.next().getTaskId() == id)
			{
				return it.next();
			}
		}	
		return null;
	}
	
	/**
	 * 
	 * add task to head of the list
	 */
	public static void addToList(TaskObject newTask)
	{ 
		arrayOfTask.add(0,newTask);
	}

	/**
	 * Get REALTIME of the system
	 */
	public static String getCurrentSystemTime()
	{
		DateFormat  dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Calendar cal = Calendar.getInstance();				
		return dateFormat.format(cal.getTime());
	}
	

	@Override
    protected Object clone() throws CloneNotSupportedException
    {
        throw new CloneNotSupportedException("Clone is not allowed.");
    }
	
	 
}
