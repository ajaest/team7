package so.chalmers.eda397.so.data.entity;

import java.util.ArrayList;
import java.util.List;

import se.chalmers.eda397.team7.so.R;
import se.chalmers.eda397.team7.so.data.entity.Post;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PostListAdapter extends ArrayAdapter<Post>{

	private List<Post> postList;
	private Context context;
	private int layout;
	
	public PostListAdapter(Context context, List<Post> postList, int layout) {
		super( context, R.layout.question_item, postList);
		this.postList = postList;
		this.context = context;
		this.layout = layout;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		//if (row==null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			row=inflater.inflate(this.layout, parent, false);
			TextView questionTitle =(TextView)row.findViewById(R.id.questionTitle);
			TextView nAnswers = (TextView) row.findViewById(R.id.answersNumber);
			questionTitle.setText(this.postList.get(position).getTitle());
			nAnswers.setText(Integer.toString(this.postList.get(position).getAnswer_count()) + " answers ");
		//}
		return(row);
	}
	
}
