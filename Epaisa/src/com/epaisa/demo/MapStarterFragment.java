package com.epaisa.demo;

import java.util.Locale;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MapStarterFragment extends Fragment 
{
	public static final String tag = MapStarterFragment.class.getSimpleName();

	private RelativeLayout mRelativelayout;
	private Button mMapStarterButton;
	private LoginActivity mActivity;
	
	private String mAddress= "Chhatrapati+Shivaji+International+Airport";
	
	private String mSelectedLanguage;
	private int mSelectedColor;
	
	
	
	@Override
	public void onAttach(Activity activity) 
	{
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mActivity  = (LoginActivity)activity;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) 
	{ // TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Bundle bundle = this.getArguments();
		
		mSelectedLanguage = bundle.getString("selectedLanguage");
		mSelectedColor = bundle.getInt("selectedColor");

	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_map_starter, null);
		mRelativelayout = (RelativeLayout) view.findViewById(R.id.mapStarterContent);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) 
	{
		mMapStarterButton = (Button)view.findViewById(R.id.mapStarterButton);
		
		if(mSelectedLanguage == "Hindi")
		{
			mMapStarterButton.setText(R.string.map_starter_button_text_hindi);
		}
		
		mRelativelayout.setBackgroundResource(mSelectedColor);

		mMapStarterButton.setOnClickListener(mMapStarterButtonOnClickListener);
	}
	
	private OnClickListener mMapStarterButtonOnClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View arg0) 
		{
			String uri = String.format(Locale.ENGLISH, "geo:0,0?q=" + mAddress );
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

			try
			{
				mActivity.startActivity(intent);
				Toast.makeText(getActivity(), "Map loaded", Toast.LENGTH_SHORT).show();
			}
			catch(ActivityNotFoundException e)
			{
				String message = "Google Maps not found. Please install maps from play store.";
				Log.e(tag, message);
				Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
			}
		}
	};
}
