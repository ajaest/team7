package se.chalmers.eda397.team7.so;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class UserListAdapter extends ArrayAdapter<User>{

	private ArrayList<User> userList;
	private Context context;
	private int layout;
	
	public UserListAdapter(Context context, ArrayList<User> userList, int layout) {
		super( context, R.layout.user_item, userList);
		this.userList = userList;
		this.context = context;
		this.layout = layout;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		//if (row==null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			row=inflater.inflate(this.layout, parent, false);
			TextView name =(TextView)row.findViewById(R.id.textViewUserNameList);
			TextView votesUp = (TextView) row.findViewById(R.id.textViewVotesUp);
			TextView votesDown = (TextView) row.findViewById(R.id.textViewVotesDown);
			TextView reputation = (TextView) row.findViewById(R.id.textViewReputationUserList);

			name.setText(this.userList.get(position).getName());
			votesUp.setText(Integer.toString(this.userList.get(position).getVotesUp()));
			votesDown.setText(Integer.toString(this.userList.get(position).getVotesDown()));
			reputation.setText(Integer.toString(this.userList.get(position).getReputation()));

		//}
		return(row);
	}
	
}
