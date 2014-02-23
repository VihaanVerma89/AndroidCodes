package com.epaisa.demo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.epaisa.demo.R.color;

public class LoginActivity extends FragmentActivity
{
	public static final String tag = LoginActivity.class.getSimpleName();
	private RelativeLayout mRelativeLayout;
	private TextView mNameTextView;
	private EditText mNameEditText;

	private TextView mPasswordTextView;
	private EditText mPasswordEditText;

	private Button mLoginButton;
	private LoginActivity mActivity;
	
	private TextView mLanguagesOptionsTextView;
	private RadioGroup mLaguageRadioGroup;
	private RadioButton mHindiLanguageRadiobutton; 
	private RadioButton mEnglishLanguageRadiobutton; 

	private TextView mThemeOptionsTextView;
	private RadioGroup mThemesRadioGroup;
	private RadioButton mWhiteThemeRadioButton;
	private RadioButton mBlueThemeRadioButton;
	
	private String mHindi = "Hindi";
	private String mEnglish = "English";

	private int mSelectedColor = R.color.epaisa_white_bg;
	private String mSelectedLanguage = mEnglish;
	
	
	private FragmentTransactionManager mFragmentTransactionManager = FragmentTransactionManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
       
        storeUIReferences();
        setupListeners();
    }
    
    private void storeUIReferences()
    {
         
        mRelativeLayout = (RelativeLayout) findViewById(R.id.content);
        
        mNameTextView = (TextView)findViewById(R.id.nameTextView);
        mNameEditText = (EditText) findViewById(R.id.nameEditText);

        mPasswordTextView = (TextView) findViewById(R.id.passwordTextView);
        mPasswordEditText = (EditText) findViewById(R.id.passwordEditText);

        mLoginButton = (Button)findViewById(R.id.loginButton);
        
        mActivity = this;
        
        mThemeOptionsTextView = (TextView) findViewById(R.id.themeOptionsTextView);
        mThemesRadioGroup = (RadioGroup) findViewById(R.id.themesRadioGroup);
        mWhiteThemeRadioButton = (RadioButton) findViewById(R.id.whiteThemeRadioButton);
        mBlueThemeRadioButton = (RadioButton) findViewById(R.id.blueThemeRadioButton);
        

        mLanguagesOptionsTextView = (TextView) findViewById(R.id.languageOptionsTextView);
        mLaguageRadioGroup = (RadioGroup) findViewById(R.id.languageRadioGroup);
        mHindiLanguageRadiobutton = (RadioButton) findViewById(R.id.hindiLanguageRadioButton);
        mEnglishLanguageRadiobutton = (RadioButton) findViewById(R.id.englishLanguageRadioButton);
    }
    
    private void setupListeners()
    {
    	mPasswordEditText.setOnEditorActionListener(onEditorActionListener);
        mLaguageRadioGroup.setOnCheckedChangeListener(mLanguageRadioGroupOnCheckedChangeListener);
        mThemesRadioGroup.setOnCheckedChangeListener(mThemesRadioGroupCheckedChangeListener);
        mLoginButton.setOnClickListener(mLoginButtonClickListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    
    private OnClickListener mLoginButtonClickListener = new OnClickListener()
    {
		@Override
		public void onClick(View view) 
		{
			String name = mNameEditText.getText().toString();
			String password = mPasswordEditText.getText().toString();

			if(name.equals(Config.mUserName) && password.equals(Config.mPassword))
			{
				Bundle bundle = new Bundle();
				bundle.putInt("selectedColor", mSelectedColor);
				bundle.putString("selectedLanguage", mSelectedLanguage);
	
				mFragmentTransactionManager.showMapStarterScreen(mActivity, bundle);
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Wrong User Name or Password", Toast.LENGTH_LONG).show();
			}
		
		}
	};
	
	private OnCheckedChangeListener mLanguageRadioGroupOnCheckedChangeListener = new OnCheckedChangeListener() 
	{
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) 
		{
			if(mHindiLanguageRadiobutton.isChecked())
			{
				mNameTextView.setText(R.string.name_textView_text_hindi);
				mPasswordTextView.setText(R.string.password_textView_text_hindi);
				
				mThemeOptionsTextView.setText(R.string.theme_textView_text_hindi);
				mWhiteThemeRadioButton.setText(R.string.white_theme_radio_box_hindi);
				mBlueThemeRadioButton.setText(R.string.blue_theme_radio_box_hindi);

				mLanguagesOptionsTextView.setText(R.string.language_textView_text_hindi);
				mHindiLanguageRadiobutton.setText(R.string.hindi_language_radio_button_text_hindi);
				mEnglishLanguageRadiobutton.setText(R.string.english_language_radio_button_text_hindi);;

				mLoginButton.setText(R.string.login_button_text_hindi);

				setTitle(R.string.app_name_hindi);

				mSelectedLanguage = mHindi;
			}
			else if(mEnglishLanguageRadiobutton.isChecked())
			{
				mNameTextView.setText(R.string.name_textView_text);
				mPasswordTextView.setText(R.string.password_textView_text);
				mLoginButton.setText(R.string.login_button_text);

				mThemeOptionsTextView.setText(R.string.theme_textView_text);
				mWhiteThemeRadioButton.setText(R.string.white_theme_radio_box);
				mBlueThemeRadioButton.setText(R.string.blue_theme_radio_box);
				
				mLanguagesOptionsTextView.setText(R.string.language_textView_text);
				mHindiLanguageRadiobutton.setText(R.string.hindi_language_radio_button_text);
				mEnglishLanguageRadiobutton.setText(R.string.english_language_radio_button_text);;
				setTitle(R.string.app_name);

				mSelectedLanguage = mEnglish;
			}
		}
	};
	
	private OnCheckedChangeListener mThemesRadioGroupCheckedChangeListener = new OnCheckedChangeListener()
	{
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) 
		{
			if(mWhiteThemeRadioButton.isChecked())
			{
				mRelativeLayout.setBackgroundResource(color.epaisa_white_bg);;
				mSelectedColor = R.color.epaisa_white_bg;
			}
			else if(mBlueThemeRadioButton.isChecked())
			{
				mRelativeLayout.setBackgroundResource(color.epaisa_blue_bg);
				mSelectedColor = R.color.epaisa_blue_bg;
			}
		}
	};
	
	private OnEditorActionListener onEditorActionListener = new OnEditorActionListener() {
		
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) 
		{
//            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
            if (actionId == EditorInfo.IME_ACTION_DONE) 
            {
                Log.i(tag,"Enter pressed");
                mLoginButton.performClick();
            }    
            return false;
		}
	};
}
