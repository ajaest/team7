/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package se.chalmers.eda397.team7.so.activities;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import se.chalmers.eda397.team7.so.R;
import se.chalmers.eda397.team7.so.data.SQLiteSODatabaseHelper;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class TagCloudActivity extends FragmentActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments representing
     * each object in a collection. We use a {@link android.support.v4.app.FragmentStatePagerAdapter}
     * derivative, which will destroy and re-create fragments as needed, saving and restoring their
     * state in the process. This is important to conserve memory and is a best practice when
     * allowing navigation between objects in a potentially large collection.
     */
    DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;

    /**
     * The {@link android.support.v4.view.ViewPager} that will display the object collection.
     */
    
    static ViewPager mViewPager;
    
    private  static List<String> tagList;
    private  static SQLiteDatabase db;
    private int buttonPressed =-1;
	static int userID;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_cloud);
		Bundle bundle;
		
		bundle = getIntent().getExtras();

		userID = bundle.getInt("UserID");

        try {
			SQLiteSODatabaseHelper test = new SQLiteSODatabaseHelper(this.getApplicationContext());
			TagCloudActivity.db = test.getWritableDatabase();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        TagCloudActivity.tagList = getAllTags();
        // Create an adapter that when requested, will return a fragment representing an object in
        // the collection.
        // 
        // ViewPager and its adapters use support library fragments, so we must use
        // getSupportFragmentManager.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mDemoCollectionPagerAdapter = new DemoCollectionPagerAdapter(getSupportFragmentManager(),tagList);

        // Set up action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home button should show an "Up" caret, indicating that touching the
        // button will take the user one step up in the application's hierarchy.
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Set up the ViewPager, attaching the adapter.
       
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
    }
   
    
    
    public int getButtonPressed() {
		return buttonPressed;
	}

    public static ViewPager getViewPager(){
    	return mViewPager;
    }

	public void setButtonPressed(int buttonPressed) {
		this.buttonPressed = buttonPressed;
	}


	/*
     * Returns the list of tags in our system order by number of appearances 
     */
    private List<String> getAllTags(){
    	Cursor cur;
    	List<String> tagsList = new ArrayList<String>();
  
		cur = db.rawQuery("SELECT tag, count(*) FROM searchindex_tags GROUP BY tag ORDER BY 1 ", null);
		while(cur.moveToNext())
			if (!cur.getString(0).equals("UL")) { //Problems with this tag, what is it??
				tagsList.add(cur.getString(0));
			}
		
		cur.close();
    	return tagsList;
    }

    @SuppressWarnings("deprecation")
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This is called when the Home (Up) button is pressed in the action bar.
                // Create a simple intent that starts the hierarchical parent activity and
                // use NavUtils in the Support Package to ensure proper handling of Up.
                Intent upIntent = new Intent(this, HomeActivity.class);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is not part of the application's task, so create a new task
                    // with a synthesized back stack.
                    TaskStackBuilder.from(this)
                            // If there are ancestor activities, they should be added here.
                            .addNextIntent(upIntent)
                            .startActivities();
                    finish();
                } else {
                    // This activity is part of the application's task, so simply
                    // navigate up to the hierarchical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    

    /**
     * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a fragment
     * representing an object in the collection.
     */
    public static class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {

    	private List<String> tagList;
        public DemoCollectionPagerAdapter(FragmentManager fm, List<String> tagList) {
        	super(fm);
        	this.tagList = tagList;
        	
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new DemoObjectFragment();
            Bundle args = new Bundle();
            
            args.putString("center_tag", this.tagList.get(i)); 
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return tagList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return " " + tagList.get(position) + " ";
        } 
    }


    
    
    public static class DemoObjectFragment extends Fragment {

        private Button centerButton;
        private Button topLeftButton;
        private Button topRightButton;
        private Button bottomLeftButton;
        private Button bottomRightButton;
        
        
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tag_cloud, container, false);
            Bundle args = getArguments();
            String centerTag = args.getString("center_tag");
            List<String> closeTagsList = getCloseTags(centerTag);
         
            centerButton = (Button) rootView.findViewById(R.id.buttonCenter);
            topLeftButton = (Button) rootView.findViewById(R.id.buttonTopLeft);
            topRightButton = (Button) rootView.findViewById(R.id.buttonTopRight);
            bottomLeftButton = (Button) rootView.findViewById(R.id.buttonBottomLeft);
            bottomRightButton = (Button) rootView.findViewById(R.id.buttonBottomRight);

            centerButton.setText(centerTag);
            if (closeTagsList.size()>0) 
				topLeftButton.setText(closeTagsList.get(0));
            else 
				topLeftButton.setVisibility(View.GONE);
            if(closeTagsList.size()>1)
				topRightButton.setText(closeTagsList.get(1));
            else 
				topRightButton.setVisibility(View.GONE);
            if (closeTagsList.size()>2) 
				bottomLeftButton.setText(closeTagsList.get(2));
            else 
				bottomLeftButton.setVisibility(View.GONE);
            if (closeTagsList.size()>3)
            	bottomRightButton.setText(closeTagsList.get(3));
            else 
				bottomRightButton.setVisibility(View.GONE);

            setButtonListener(centerButton);
            setButtonListener2(topLeftButton);
            setButtonListener2(topRightButton);
            setButtonListener2(bottomLeftButton);
            setButtonListener2(bottomRightButton);
         
            return rootView;
        }
        
        private void setButtonListener2(Button button){
        	final String tag = button.getText().toString();
        	Log.d("testing","tag pressed:"+ tag);
        	// button on click listers
    		button.setOnClickListener(new OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				int pos = tagList.indexOf(tag);
    				mViewPager.setCurrentItem(pos);

    			}
    		});
        }
        
        private void setButtonListener(Button button){
        	final Context ctx = this.getActivity();
        	final String tag = button.getText().toString();
        	Log.d("testing","tag pressed:"+ tag);
        	// button on click listers
    		button.setOnClickListener(new OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				Intent intent =  new Intent(ctx, Questions_Tab_Activity.class);
    				intent.putExtra("tagPressed", tag);
    				intent.putExtra("UserID", userID);
    				startActivity(intent);

    			}
    		});
        }
        /*
         * Gets the 4 tags more related to the given tag.
         */
        private List<String> getCloseTags(String tag){
        	ArrayList<String> closeTags = new ArrayList<String>();
        	Cursor cursor;
        	cursor = db.rawQuery("SELECT sum(weight), tag1,tag2 from tag_graph " +
        			"where tag1<>tag2 and tag1=? " +
        			" group by tag1,tag2 order by 1 desc LIMIT 4",new String[]{tag});
        	while (  cursor.moveToNext()) {
				closeTags.add(cursor.getString(2));
			}
        	cursor.close();
        	return closeTags;
        }
    }
    
}
