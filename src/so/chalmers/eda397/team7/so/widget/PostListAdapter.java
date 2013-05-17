package so.chalmers.eda397.team7.so.widget;

import java.io.IOException;
import java.util.List;

import se.chalmers.eda397.team7.so.R;
import se.chalmers.eda397.team7.so.data.SQLiteSODatabaseHelper;
import se.chalmers.eda397.team7.so.data.entity.Question;
import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import se.chalmers.eda397.team7.so.datalayer.PostDataLayer;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PostListAdapter extends ArrayAdapter<Question>{

	private List<Question> postList;
	private Context context;
	private int layout;
	private boolean isHeat;
	
	public PostListAdapter(Context context, List<Question> postList, int layout, boolean isHeat) {
		super( context, R.layout.question_item, postList);
		this.postList = postList;
		this.context = context;
		this.layout = layout;
		this.isHeat = isHeat;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		PostDataLayer postDataLayer = null;
		try {
			SQLiteSODatabaseHelper test = new SQLiteSODatabaseHelper(context);
			SQLiteDatabase db = test.getWritableDatabase();
			DataLayerFactory factory = new DataLayerFactory(db);
			postDataLayer = factory.createPostDataLayer();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//if (row==null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			row=inflater.inflate(this.layout, parent, false);
			if(isHeat)
				row.setBackgroundColor(postDataLayer.colorQuestion(this.postList.get(position), this.postList));
			TextView questionTitle =(TextView)row.findViewById(R.id.questionTitle);
			TextView nAnswers = (TextView) row.findViewById(R.id.answersNumber);
			questionTitle.setText(this.postList.get(position).getTitle());
			nAnswers.setText(Integer.toString(this.postList.get(position).getAnswer_count()) + " answers ");
		//}
		return(row);
	}


	
	
}
