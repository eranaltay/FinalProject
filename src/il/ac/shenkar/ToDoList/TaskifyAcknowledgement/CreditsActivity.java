/*
 * CreditsActivity.java
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

package il.ac.shenkar.ToDoList.TaskifyAcknowledgement;

import il.ac.shenkar.ToDoList.R;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class CreditsActivity extends SherlockActivity implements OnItemClickListener
{
    private   		 ActionBar				actionBar;
	private 		 ListView 				creditsList;
    
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.creditslayout);
	    getWindow().setSoftInputMode(
	    	      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	    actionBar = getSupportActionBar();
	    actionBar.setDisplayShowHomeEnabled(true);
	    actionBar.setDisplayShowTitleEnabled(false);
	    
	    
		CreditsItems[] creditsItems = 
	   	{ 
	        new CreditsItems("SlidingMenu", getApplicationContext().getString(R.string.SlidingmenuURL)),
	        new CreditsItems("ActionBarSherlock", getApplicationContext().getString(R.string.actionbarsherlock)),
	        new CreditsItems("Support", getApplicationContext().getString(R.string.mailapp))
	    }; 
	    
	    
	    CreditsAdapter creditsAdapter = new CreditsAdapter (getApplicationContext(),
	            R.layout.creditsitems, creditsItems);
	    
	    creditsList = (ListView)findViewById (R.id.creditsmenu);
	    creditsList.setAdapter(creditsAdapter);     
	    creditsList.setOnItemClickListener(this);
	    
	   
	    
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

	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) 
	{
		Uri uri = null;
		switch(position)
		{
			case 0:
				uri = Uri.parse(getApplicationContext().getString(R.string.SlidingmenuURL));
				startWEBBrowser(uri);
			break;
		
			case 1:
				uri = Uri.parse(getApplicationContext().getString(R.string.actionbarsherlock));
				startWEBBrowser(uri);
			break;		
			
			case 2:
				startEMAILAgent();
			break;		
			
		}

	}
	
	
	public void startWEBBrowser(Uri uri)
	{
		 Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		 startActivity(intent);
	}

	
	public void startEMAILAgent()
	{
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/html");
		intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getApplicationContext().getString(R.string.mailapp)});
		intent.putExtra(Intent.EXTRA_SUBJECT, "Taskify Support");
		startActivity(intent);	
	}
	
	
	
}
