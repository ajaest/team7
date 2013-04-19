package se.chalmers.eda397.team7.so;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class QuestionListAdapter extends ArrayAdapter<Question>{

	private ArrayList<Question> questionList;
	private Context context;
	private int layout;
	
	public QuestionListAdapter(Context context, ArrayList<Question> questionList, int layout) {
		super( context, R.layout.question_item, questionList);
		this.questionList = questionList;
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
			questionTitle.setText(this.questionList.get(position).getTitle());
			nAnswers.setText(Integer.toString(this.questionList.get(position).getnAnswers()) + " answers ");
		//}
		return(row);
	}
	
}
