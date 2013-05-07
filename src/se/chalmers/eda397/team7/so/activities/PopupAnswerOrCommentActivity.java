package se.chalmers.eda397.team7.so.activities;

import se.chalmers.eda397.team7.so.R;
import se.chalmers.eda397.team7.so.R.layout;
import se.chalmers.eda397.team7.so.R.menu;
import android.os.Build;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class PopupAnswerOrCommentActivity extends Activity {

	private ImageButton buttonAnswerQuestion;
	private Bundle bundle;
	Integer idPost;
	boolean isComment;
	private EditText editTextBody;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_popup_answer_comment);
		
		bundle = getIntent().getExtras();
		idPost = bundle.getInt("idPost");
		isComment = bundle.getBoolean("isComment");
		if(isComment)
			this.setTitle("Add a comment");
		else
			this.setTitle("Answer to question ");
		buttonAnswerQuestion = (ImageButton) findViewById(R.id.imageButtonAnswerQuestion);
		editTextBody = (EditText) findViewById(R.id.editTextBodyAnswerQuestion);
		buttonAnswerQuestion.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();

			}
		});
	}

	

}
