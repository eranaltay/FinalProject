/*
 * TaskDataBaseSQL.java
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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 
 * @author EranAltay
 * DATABASE, SQLlite
 */

public class TaskDataBaseSQL
{
	//TaskList App KEYS, like task name, task id..
    public static final String KEY_ROWID = "_id";
    public static final String KEY_TITLETASK = "taskname";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_LOCATIONOFTASK = "location";
    public static final String KEY_NOTIFY_DATE= "notifydate";
    public static final String KEY_REPEAT_INTERVAL = "interval";
    public static final String KEY_DONE_TASK = "isDone";
    public static final String KEY_IMPORTANT_TASK = "important";
    public static final String KEY_PROXIMITY_TASK = "proximity";
    
    //DataBase keys
    private static final String DATABASE_NAME = "inventory";
    private static final String DATABASE_TABLE = "tasks";
    private static final int 	DATABASE_VERSION = 1;
    private static final String TAG = "itemDataBaseSQL";
 
    private static final String DATABASE_CREATE =
        "create table " + DATABASE_TABLE + " ( " + KEY_ROWID + " integer primary key autoincrement, " 
    + KEY_TITLETASK +" text not null," + KEY_DESCRIPTION +" text," + KEY_LOCATIONOFTASK +" text," +
        		KEY_PROXIMITY_TASK +" integer," + KEY_NOTIFY_DATE +" text," + KEY_REPEAT_INTERVAL + " integer,"
    +  KEY_DONE_TASK +" integer," + KEY_IMPORTANT_TASK + " integer"  + ")";
   	    
    private static DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	private Context context;
	    
	    public TaskDataBaseSQL(Context context) 
	    {
	        this.context = context;
	        DatabaseHelper.getInstance(context);
	    }
	   
	 
	    private static class DatabaseHelper extends SQLiteOpenHelper 
	    {
	    	  public static DatabaseHelper getInstance(Context ctx) 
	    	  {
		    	     if (DBHelper == null) 
		    	     {
	    	        	DBHelper = new DatabaseHelper(ctx);
	    	         }
		    	     
	    	        return DBHelper;
	    	    }
	    		    	
	    	
	        DatabaseHelper(Context context) 
	        {
	            super(context, DATABASE_NAME, null, DATABASE_VERSION);
	        }
	 
	        @Override
	        public void onCreate(SQLiteDatabase db) 
	        {
	            db.execSQL(DATABASE_CREATE);
	        }
	 
	        @Override
	        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
	                              int newVersion) 
	        {
	            Log.w(TAG, "Upgrading database from version " + oldVersion 
	                  + " to "
	                  + newVersion + ", which will destroy all old data");
	            db.execSQL("DROP TABLE IF EXISTS tasks");
	            onCreate(db);
	        }
	        
	  
	    }  
	   
	    //opens the database
	    public TaskDataBaseSQL open() throws SQLException 
	    {
	    	db = DBHelper.getWritableDatabase();
	        return this;
	    }
	 
	    //closes the database    
	    public void close() 
	    {
	    	DBHelper.close();
	    }
	 
	    //insert a title into the database
	    public long insertTask(TaskObject newTask) 
	    {
	        ContentValues initialValues = new ContentValues();
	        initialValues.put(KEY_TITLETASK, newTask.getTaskTitle());
	        initialValues.put(KEY_DESCRIPTION, newTask.getTaskDescription());
	        initialValues.put(KEY_LOCATIONOFTASK, newTask.getTaskLocation());
	        initialValues.put(KEY_PROXIMITY_TASK, newTask.getIfTaskHasProximity());
	        initialValues.put(KEY_NOTIFY_DATE, newTask.getTaskNotificationDate());
	        initialValues.put(KEY_REPEAT_INTERVAL, newTask.getTaskInterval());
	        initialValues.put(KEY_DONE_TASK, newTask.getTaskDoneIndicator());
	        initialValues.put(KEY_IMPORTANT_TASK, newTask.getTaskImportantIndicator());
	        return db.insert(DATABASE_TABLE, null, initialValues);
	       
	    }
	 
	    //deletes a particular task
	    public boolean deleteTask(long rowId) 
	    {
	        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	    }
	    
	    
	   public void deleteDatabse()
	   {
	            SQLiteDatabase db = DBHelper.getWritableDatabase();
	            db.delete(DATABASE_TABLE, null, new String[] {});
	            Log.d("Database stuff", "Database table succesfully deleted");
	            DBHelper.onUpgrade(db, 1, 2);
	        }
	 
	    //retrieves all the tasks
	    public Cursor getAllTasks() 
	    {
	        return db.query(DATABASE_TABLE, new String[]
	        		{
	           		KEY_ROWID, 
	        		KEY_TITLETASK,
	        		KEY_DESCRIPTION,
	        		KEY_LOCATIONOFTASK,
	        		KEY_PROXIMITY_TASK,
	        		KEY_NOTIFY_DATE,
	        		KEY_REPEAT_INTERVAL,
	        		KEY_DONE_TASK,
	        		KEY_IMPORTANT_TASK
		        	    }, 
	        	       null,
	                   null, 
	                   null, 
	                   null, 
	                   null);
	    }
	 
	  
	    
		public void recoverTask(Cursor c)
	    {
			TaskObject obj = new TaskObject
    			(Long.parseLong(c.getString(0)),
    			c.getString(1),
    			c.getString(2), 
	    		c.getString(3),
	    		c.getInt(4),
	    		c.getString(5), 
	    		c.getInt(6), 
	    		c.getInt(7),
	    		c.getInt(8));
		   	TaskList.addToList(obj);  
	    } 
		
		
		public TaskObject getTaskByID(int rowid)
		{ 
			 Cursor c = getTask(rowid);
				TaskObject obj = new TaskObject
		    			(Long.parseLong(c.getString(0)),
		    			c.getString(1),
		    			c.getString(2), 
			    		c.getString(3),
			    		c.getInt(4),
			    		c.getString(5), 
			    		c.getInt(6), 
			    		c.getInt(7),
			    		c.getInt(8));
				
				return obj;		
		}

		public void addTasksToArray()
		{
			TaskList.getTasksList().clear();
			// recover all tasks from the data base
			this.open();
			Cursor c = this.getAllTasks();
			if (c.moveToFirst())
			{
				do 
				{
					recoverTask(c);
				} while (c.moveToNext());
			}
			
			this.close();			
		}
	    
	    
	    
	    //retrieves a particular task
	    public Cursor getTask(long rowId) throws SQLException 
	    {
	        Cursor mCursor =
	                db.query(true, DATABASE_TABLE, new String[] {
	                		KEY_ROWID,
	                		KEY_TITLETASK,
	    	        		KEY_DESCRIPTION,
	    	        		KEY_LOCATIONOFTASK,
	    	        		KEY_PROXIMITY_TASK,
	    	        		KEY_NOTIFY_DATE,
	    	        		KEY_REPEAT_INTERVAL,
	    	        		KEY_DONE_TASK,
	    	        		KEY_IMPORTANT_TASK
	                   		}, 
	                		KEY_ROWID + "=" + rowId, 
	                		null,
	                	    null, 
	                        null, 
	                        null,
	                		null);
	        
	        if (mCursor != null)
	        {
	            mCursor.moveToFirst();
	        }
	        return mCursor;
	    }
	 
	    //updates a task
	    public boolean updateTask(TaskObject task)
	    		 
	    {
	        ContentValues args = new ContentValues();
	        args.put(KEY_TITLETASK, task.getTaskTitle());
	        args.put(KEY_DESCRIPTION, task.getTaskDescription());
	        args.put(KEY_LOCATIONOFTASK, task.getTaskLocation());
	        args.put(KEY_PROXIMITY_TASK, task.getIfTaskHasProximity());
	        args.put(KEY_NOTIFY_DATE, task.getTaskNotificationDate());
	        args.put(KEY_REPEAT_INTERVAL,task.getTaskInterval());
	        args.put(KEY_DONE_TASK, task.getTaskDoneIndicator());
	        args.put(KEY_IMPORTANT_TASK, task.getTaskImportantIndicator());
	        return db.update(DATABASE_TABLE, args, 
	                         KEY_ROWID + "=" + task.getTaskId(), null) > 0;
	    }

			
}

