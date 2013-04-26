package so.chalmers.eda397.so.data.entity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import se.chalmers.eda397.team7.so.R;
import se.chalmers.eda397.team7.so.data.entity.Answer;
import se.chalmers.eda397.team7.so.data.entity.EntityUtils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AnswerListAdapter extends ArrayAdapter<Answer>{

	private List<Answer> answerList;
	private Context context;
	private int layout;
	
	public AnswerListAdapter(Context context, List<Answer> answerList, int layout) {
		super( context, R.layout.question_item, answerList);
		this.answerList = answerList;
		this.context = context;
		this.layout = layout;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		//if (row==null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			row=inflater.inflate(this.layout, parent, false);
			TextView answerTextView =(TextView)row.findViewById(R.id.textViewAnswer);
			TextView dateTextView = (TextView) row.findViewById(R.id.textViewDateAnswer);
			answerTextView.setText(EntityUtils.extractText(this.answerList.get(position).getBody()));
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
			dateTextView.setText(df.format(this.answerList.get(position).getCreation_date()));
		//}
		return(row);
	}
	
}