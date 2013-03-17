package il.ac.shenkar.ToDoList.ShowTask;


import il.ac.shenkar.ToDoList.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class ShowTaskActivityAdapter extends ArrayAdapter<ShowTaskItems> 
{

		Context context; 
	    int layoutResourceId;    
	    ShowTaskItems data[] = null;
		
		public ShowTaskActivityAdapter(Context context, int layoutResourceId, ShowTaskItems[] data)
		{
			super(context, layoutResourceId, data);
		    this.layoutResourceId = layoutResourceId;
		    this.context = context;
		    this.data = data;
		}
		
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        TaskItemsHolder holder = null;
	        
	        if(row == null)
	        {
	        	LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
	            row = inflater.inflate(layoutResourceId, parent, false);
	            
	            holder = new TaskItemsHolder();
	            holder.txtItem = (TextView)row.findViewById(R.id.taskheader);
	            holder.bottomtext = (TextView)row.findViewById(R.id.task);
	            
	            row.setTag(holder);
	        }
	        else
	        {
	            holder = (TaskItemsHolder)row.getTag();
	        }
	        
	        ShowTaskItems taskItem = data[position];
	        holder.txtItem.setText(taskItem.item);
	        holder.bottomtext.setText(taskItem.subItem);
	        
	        return row;
	    }
	    
	    static class TaskItemsHolder
	    {
	        TextView txtItem;
	        TextView bottomtext;
	    }
	}

