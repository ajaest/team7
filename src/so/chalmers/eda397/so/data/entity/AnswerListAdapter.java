package so.chalmers.eda397.so.data.entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


import se.chalmers.eda397.team7.so.R;
import se.chalmers.eda397.team7.so.data.entity.EntityUtils;
import se.chalmers.eda397.team7.so.data.entity.Post;
import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import se.chalmers.eda397.team7.so.datalayer.PostDataLayer;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AnswerListAdapter extends ArrayAdapter<Post>{

	private ArrayList<Post> answerList;
	private Context context;
	private int layout;
	private SQLiteDatabase db;
	
	public AnswerListAdapter(Context context, ArrayList<Post> answerList, int layout ) {
		super( context, R.layout.question_item, answerList);
		this.answerList = answerList;
		this.context = context;
		this.layout = layout;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		//if (row==null){
			DataLayerFactory factory = new DataLayerFactory(db);
			PostDataLayer postDataLayer= factory.createPostDataLayer();
			
			LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			row=inflater.inflate(this.layout, parent, false);
			TextView answerTextView =(TextView)row.findViewById(R.id.textViewAnswer);
			TextView dateTextView = (TextView) row.findViewById(R.id.textViewDateAnswer);
			TextView numberComments = (TextView) row.findViewById(R.id.textViewNumberCommentsAnswer);
			answerTextView.setText(EntityUtils.extractText(this.answerList.get(position).getBody()));
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
			dateTextView.setText(df.format(this.answerList.get(position).getCreation_date()));
			numberComments.setText(Integer.toString(this.answerList.get(position).getComment_count())+ " comments");
		//}
		return(row);
	}
	
}