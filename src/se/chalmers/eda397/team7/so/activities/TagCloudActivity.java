
package se.chalmers.eda397.team7.so.activities;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import se.chalmers.eda397.team7.so.R;
import se.chalmers.eda397.team7.so.data.SQLiteSODatabaseHelper;
import se.chalmers.eda397.team7.so.data.entity.Question;
import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import se.chalmers.eda397.team7.so.datalayer.PostDataLayer;
import se.chalmers.eda397.team7.so.datalayer.PostDataLayer.OrderCriteria;
import se.chalmers.eda397.team7.so.datalayer.TagDataLayer;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff.Mode;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class TagCloudActivity extends FragmentActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments representing
     * each object in a collection. We use a {@link android.support.v4.app.FragmentStatePagerAdapter}
     * derivative, which will destroy and re-create fragments as needed, saving and restoring their
     * state in the process. This is important to conserve memory and is a best practice when
     * allowing navigation between objects in a potentially large collection.
     */
   static  DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;

    /**
     * The {@link android.support.v4.view.ViewPager} that will display the object collection.
     */
    
    static ViewPager mViewPager;
    
    private  static List<String> tagList;
    private   SQLiteDatabase db;
    private int buttonPressed =-1;

    private Bundle bundle;
    private static Integer userId;
    private static TagDataLayer  tagDataLayer;
    private static boolean myTags;
    private static boolean multiTag;
    private static boolean heat=false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_cloud);

		bundle = getIntent().getExtras();
        userId = bundle.getInt("UserID");
        myTags = bundle.getBoolean("See_my_tags");
        multiTag = bundle.getBoolean("isMultitag");
        inflateViewPager();
        
    }
    
    
    @Override
    public void onRestart(){
    	inflateViewPager();
    	super.onRestart();
    }
   
    
    
    public void inflateViewPager(){
    	String tags = null;
    	try {
			SQLiteSODatabaseHelper test = new SQLiteSODatabaseHelper(this.getApplicationContext());
			db = test.getWritableDatabase();
			DataLayerFactory factory = new DataLayerFactory(db);
			tagDataLayer = factory.createTagDataLayer();
		} catch (IOException e) {
			e.printStackTrace();
		}
        if(multiTag){
        	tags = bundle.getString("query");
        	TagCloudActivity.tagList = new ArrayList<String>();
        	TagCloudActivity.tagList.add(tags);
        }
        else if(myTags)
        	TagCloudActivity.tagList = getAllTagsOfUser(userId);
        else{
        	TagCloudActivity.tagList = tagDataLayer.getAllTags();
        	 // Set up action bar.
            final ActionBar actionBar = getActionBar();

            // Specify that the Home button should show an "Up" caret, indicating that touching the
            // button will take the user one step up in the application's hierarchy.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        	
        // Create an adapter that when requested, will return a fragment representing an object in
        // the collection.
        // 
        // ViewPager and its adapters use support library fragments, so we must use
        // getSupportFragmentManager.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mDemoCollectionPagerAdapter = new DemoCollectionPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager, attaching the adapter.
       
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
    }
    
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; 
		if(!multiTag)
			getMenuInflater().inflate(R.menu.tag_cloud, menu); 
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setDisplayShowTitleEnabled(true);
			
		}
		return true;
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
     * Returns the list of tags in our system of a specific user 
     */
      private List<String> getAllTagsOfUser(Integer userId){
    	  List<String> openTags = new ArrayList<String>();
    	  DataLayerFactory factory = new DataLayerFactory(db);
    	  PostDataLayer postDataLayer = factory.createPostDataLayer();
    	  List<Question> userQuestions = postDataLayer.getQuestionsOfUser(userId, OrderCriteria.CREATION_DATE);
    	  for (Question question : userQuestions) 
    		  for (String tag : question.getTags()) {
				if (!openTags.contains(tag)) {
					openTags.add(tag);
				}
			}
  			
    	  return openTags;
  		}



	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            	onBackPressed();
                return true;
            case R.id.menu_heat_cloud:
            	heat = !heat;
            	mDemoCollectionPagerAdapter.notifyDataSetChanged();
            	return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
   
    

    /**
     * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a fragment
     * representing an object in the collection.
     */
    public static class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {

        public DemoCollectionPagerAdapter(FragmentManager fm) {
        	super(fm);
        	
        }
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new DemoObjectFragment();
            Bundle args = new Bundle();
            
            args.putString("center_tag", tagList.get(i)); 
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
        private String centerTag;
        

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tag_cloud, container, false);
            Bundle args = getArguments();

            centerTag = args.getString("center_tag");
                       
            centerButton = (Button) rootView.findViewById(R.id.buttonCenter);
            topLeftButton = (Button) rootView.findViewById(R.id.buttonTopLeft);
            topRightButton = (Button) rootView.findViewById(R.id.buttonTopRight);
            bottomLeftButton = (Button) rootView.findViewById(R.id.buttonBottomLeft);
            bottomRightButton = (Button) rootView.findViewById(R.id.buttonBottomRight);
          
            
            if (multiTag ) {
				inflateMultiCloudLayout();
			}
            else{
            	centerButton.setText(centerTag);
            	List<String> closeTagsList = tagDataLayer.getCloseTags(centerTag);    
                      
            	 
                if (closeTagsList.size()>0) {
    				topLeftButton.setText(closeTagsList.get(0));
                }
                else 
    				topLeftButton.setVisibility(View.GONE);
                
                if(closeTagsList.size()>1){
    				topRightButton.setText(closeTagsList.get(1));
                }else 
    				topRightButton.setVisibility(View.GONE);
                
                if (closeTagsList.size()>2) {
    				bottomLeftButton.setText(closeTagsList.get(2));
                }else 
    				bottomLeftButton.setVisibility(View.GONE);
                
                if (closeTagsList.size()>3){
                	bottomRightButton.setText(closeTagsList.get(3));
                }else 
    				bottomRightButton.setVisibility(View.GONE);
                
                if (heat && !myTags) {
                	
					heatButtons();
				}
	            
	            
	            setButtonListener(centerButton);
	            if (tagList.contains(topLeftButton.getText().toString())) 
	            	setButtonListener2(topLeftButton);
	            else
	            	setButtonListener(topLeftButton);
	 
	            
	            if (tagList.contains(topRightButton.getText().toString())) 
	            	setButtonListener2(topRightButton);
	            else
	            	setButtonListener(topRightButton);
	
	            
	            if (tagList.contains(bottomLeftButton.getText().toString())) 
	            	setButtonListener2(bottomLeftButton);
	            else
	            	setButtonListener(bottomLeftButton);
	 
	            
	            if (tagList.contains(bottomRightButton.getText().toString())) 
	            	setButtonListener2(bottomRightButton);
	            else
	            	setButtonListener(bottomRightButton);
            }
            
           
            return rootView;
        }
        
        public void heatButtons(){
        	Integer curTagColor;
        	List<String> closeTagsList = tagDataLayer.getCloseTags(centerTag);    
            
       	 
            if (closeTagsList.size()>0) {
    			curTagColor = tagDataLayer.getTagGraphRelativeColor(centerTag, closeTagsList.get(0));
    			
    			topLeftButton.getBackground().setColorFilter(curTagColor, Mode.LIGHTEN);
            }
            
            if(closeTagsList.size()>1){
    			curTagColor = tagDataLayer.getTagGraphRelativeColor(centerTag, closeTagsList.get(1));
    			
    			topRightButton.getBackground().setColorFilter(curTagColor, Mode.LIGHTEN);
            }

            if (closeTagsList.size()>2) {	
    			curTagColor = tagDataLayer.getTagGraphRelativeColor(centerTag, closeTagsList.get(2));
    			
    			bottomLeftButton.getBackground().setColorFilter(curTagColor, Mode.LIGHTEN);
            }
            
            if (closeTagsList.size()>3){            	
            	curTagColor = tagDataLayer.getTagGraphRelativeColor(centerTag, closeTagsList.get(3));
            	
            	bottomRightButton.getBackground().setColorFilter(curTagColor, Mode.LIGHTEN);
            }
        }
        

        private String implode (String [] stringArray){
        	StringBuilder builder = new StringBuilder();
        	for (int i = 0; i < stringArray.length; i++) {
				builder.append(stringArray[i]+" ");
			}
        
        	return builder.toString();
        }
        
        //Listener for the corner buttons in the multitag cloud
        private void setButtonListener3(Button button, final int pos){
        	String tag = button.getText().toString();      	
        	String tags[]= centerTag.split(" ");
			tags[pos] = tag;
			final String tagString = implode(tags);
        	button.setOnClickListener(new OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				tagList.remove(0);
    				tagList.add(tagString);
    				mDemoCollectionPagerAdapter.notifyDataSetChanged();
    			}
    		});
        }
        
        //Listener for the buttons in the corners
        private void setButtonListener2(Button button){
        	final String tag = button.getText().toString();
        	// button on click listers
    		button.setOnClickListener(new OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				int pos = tagList.indexOf(tag);
    				mViewPager.setCurrentItem(pos);
    				

    			}
    		});
        }
        
        //Listener for the center Button
        private void setButtonListener(Button button){
        	final Context ctx = this.getActivity();
        	final String tag = button.getText().toString();
        	// button on click listers
    		button.setOnClickListener(new OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				Intent intent =  new Intent(ctx, Questions_Tab_Activity.class);
    				intent.putExtra("tagPressed", tag);
    				intent.putExtra("UserID", userId);
    				startActivity(intent);
    				
    			}
    		});
        }
        
        
        private void inflateMultiCloudLayout(){
        	centerButton.setText(centerTag);
        	setButtonListener(centerButton);
        	String tags[]= centerTag.split(" ");
        	topLeftButton.setText(getClosestTag(tags[0]));
        	setButtonListener3(topLeftButton, 0);
        	if (tags.length>1){ 
				topRightButton.setText(getClosestTag(tags[1]));
				setButtonListener3(topRightButton, 1);
        	}
        	else
        		topRightButton.setVisibility(View.GONE);
        	if (tags.length>2){
        		bottomLeftButton.setText(getClosestTag(tags[2]));
        		setButtonListener3(bottomLeftButton, 2);
        	}
        	else 
				bottomLeftButton.setVisibility(View.GONE);
			if (tags.length>3){
				bottomRightButton.setText(getClosestTag(tags[3]));
				setButtonListener3(bottomRightButton, 3);
			}
			else 
				bottomRightButton.setVisibility(View.GONE);
        }
        
        private String getClosestTag(String tag){
        	return (tagDataLayer.getCloseTags(tag).get(0));
        }
    }
    
    
}
