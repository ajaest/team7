package so.chalmers.eda397.team7.so.widget;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import se.chalmers.eda397.team7.so.R;
import se.chalmers.eda397.team7.so.data.entity.Comment;
import se.chalmers.eda397.team7.so.data.entity.User;
import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import se.chalmers.eda397.team7.so.datalayer.UserDataLayer;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CommentListAdapter extends ArrayAdapter<Comment>{

	private ArrayList<Comment> commentList;
	private Context context;
	private int layout;
	private SQLiteDatabase dbDatabase;
	
	public CommentListAdapter(Context context, ArrayList<Comment> commentList, int layout, SQLiteDatabase db) {
		super( context, R.layout.comment_item, commentList);
		this.commentList = commentList;
		this.context = context;
		this.layout = layout;
		this.dbDatabase = db;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		//if (row==null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			row=inflater.inflate(this.layout, parent, false);
			TextView commentBodyTextView =(TextView)row.findViewById(R.id.commentBody);
			TextView commentScoreTextView = (TextView) row.findViewById(R.id.commentScore);
			TextView commentDateTextView = (TextView) row.findViewById(R.id.commentDate);
			TextView commentUserTextView = (TextView) row.findViewById(R.id.commentUserOwner);
			commentBodyTextView.setText(this.commentList.get(position).getText());
			Log.d("test", "message : " + this.commentList.get(position).getText());
			commentScoreTextView.setText(Integer.toString(this.commentList.get(position).getScore()));
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
			commentDateTextView.setText(df.format(this.commentList.get(position).getCreation_date()));
			User user = getUserName(this.commentList.get(position).getUser_id());
			commentUserTextView.setText(user.getDisplay_name());
		//}
		return(row);
	}
	
	
	private User getUserName(Integer idUser){
		User user;
		DataLayerFactory factory = new DataLayerFactory(this.dbDatabase);
		UserDataLayer userDataLayer = factory.createUserDataLayer();
		user = userDataLayer.getUserById(idUser);
		return user;
	}
}
