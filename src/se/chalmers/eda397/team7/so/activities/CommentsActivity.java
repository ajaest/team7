package se.chalmers.eda397.team7.so.activities;

import java.io.IOException;
import java.util.ArrayList;

import se.chalmers.eda397.team7.so.R;
import se.chalmers.eda397.team7.so.data.SQLiteSODatabaseHelper;
import se.chalmers.eda397.team7.so.data.entity.Comment;
import se.chalmers.eda397.team7.so.datalayer.CommentDataLayer;
import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import se.chalmers.eda397.team7.so.datalayer.PostDataLayer;
import so.chalmers.eda397.team7.so.widget.CommentListAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class CommentsActivity extends Activity {

	private ListView commentsListView;
	private Bundle bundle;
	private Integer idPost;
	private SQLiteDatabase db;
	private CommentDataLayer commentDataLayer;
	private ArrayList<Comment> commentList;
	private Button commentButton;
	private	Integer userID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comments);
		
		commentsListView = (ListView) findViewById(R.id.listViewComments);
		commentButton = (Button) findViewById(R.id.buttonCommentPost);
		
		bundle = getIntent().getExtras();
		idPost = bundle.getInt("idPost");
		userID = bundle.getInt("UserID");
		
		try {
			SQLiteSODatabaseHelper test = new SQLiteSODatabaseHelper(this.getApplicationContext());
			db = test.getWritableDatabase();
			DataLayerFactory factory = new DataLayerFactory(db);
			commentDataLayer= factory.createCommentDataLayer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		commentList = (ArrayList<Comment>)commentDataLayer.getCommentsByPostId(idPost);
		commentsListView.setAdapter(new CommentListAdapter(this, commentList, R.layout.comment_item, db));
		
		commentButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent =  new Intent(CommentsActivity.this, PopupAnswerOrCommentActivity.class);
				intent.putExtra("idPost", idPost);
				intent.putExtra("UserID", userID);
				intent.putExtra("isComment", true);
				startActivity(intent);

			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setDisplayShowTitleEnabled(true);
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
