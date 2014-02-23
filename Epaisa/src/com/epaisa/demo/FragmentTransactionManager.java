package com.epaisa.demo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentTransactionManager 
{
	private static FragmentManager mFragmentManager;
	private FragmentTransaction mFragmentTransaction;

	private static FragmentTransactionManager mFragmentTransactionManager = new FragmentTransactionManager();
	
	private FragmentTransactionManager()
	{
		
	}

	public static FragmentTransactionManager getInstance()
	{
		return mFragmentTransactionManager;
	}

	public void showMapStarterScreen(LoginActivity activity, Bundle bundle)
	{
		mFragmentManager = activity.getSupportFragmentManager();
		mFragmentTransaction = mFragmentManager.beginTransaction();

		mFragmentTransaction.setCustomAnimations(R.anim.slide_in_right, android.R.anim.slide_out_right, R.anim.slide_in_right, android.R.anim.slide_out_right);
		
		Fragment fragment = new MapStarterFragment();
		fragment.setArguments(bundle);
		mFragmentTransaction.replace(R.id.content, fragment);
		mFragmentTransaction.addToBackStack(MapStarterFragment.tag);
		mFragmentTransaction.commit();
	}
}
