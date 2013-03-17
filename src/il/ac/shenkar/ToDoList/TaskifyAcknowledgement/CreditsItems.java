/*
 * CreditsItems.java
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


/**
 * 
 * The CreditsItems Class
 * Populate the view with name
 * @author EranAltay
 *
 */
public class CreditsItems 
{
		public String item;
		public String subItem;
	   
	    
	    public CreditsItems()
	    {
	        super();
	    }
	    
	    public CreditsItems(String name, String subItem)
	    {
	        super();
	        this.item = name;
	        this.subItem = subItem;
	    }
	    
	    @Override
	    public String toString() 
	    {
	        return this.item + "\n"+ this.subItem;
	       
	    }

}
