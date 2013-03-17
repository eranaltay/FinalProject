package il.ac.shenkar.ToDoList.ShowTask;

import il.ac.shenkar.ToDoList.R;
import il.ac.shenkar.ToDoList.TaskCreation.CreateTaskActivity;
import il.ac.shenkar.ToDoList.TaskDAO.TaskList;
import il.ac.shenkar.ToDoList.TaskDAO.TaskObject;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class ShowTaskActivity extends SherlockActivity 
{
	private   		 ActionBar				actionBar;
	private 		 ListView 				taskShower;
	private 		 TaskObject 			taskToShow;
	    
		@Override
	    public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
	        setContentView(R.layout.showtask_layout);
		    getWindow().setSoftInputMode(
		    	      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		    actionBar = getSupportActionBar();
		    actionBar.setDisplayShowHomeEnabled(true);
		    actionBar.setDisplayShowTitleEnabled(false);
		    taskToShow =  getIntent().getParcelableExtra("tasktoshow");
		    
		    
			ShowTaskItems[] taskItems = 
			   	{ 
			        new ShowTaskItems("Task Title", taskToShow.getTaskTitle()),
			        new ShowTaskItems("Task Description", taskToShow.getTaskDescription()),
			        new ShowTaskItems("@Location", taskToShow.getTaskLocation())
			    };
				
			    
			    ShowTaskActivityAdapter showTaskAdapter = new ShowTaskActivityAdapter (getApplicationContext(),
			            R.layout.showtaskitems, taskItems);
			    
			    taskShower = (ListView)findViewById (R.id.showtask_list);
			    taskShower.setAdapter(showTaskAdapter);              		    
		}	
		
		
		
		  @Override
			public boolean onCreateOptionsMenu(Menu menu)
			 {
				MenuInflater inflater = getSupportMenuInflater();
				inflater.inflate(R.menu.simplemenu, menu);
				return super.onCreateOptionsMenu(menu);
			}

			@Override
			public boolean onOptionsItemSelected(MenuItem item)
			{
				switch(item.getItemId())
				{
					case R.id.backMI:
						finish();
					break;		
				}	
				
				return super.onOptionsItemSelected(item);
			}
		
		
}
