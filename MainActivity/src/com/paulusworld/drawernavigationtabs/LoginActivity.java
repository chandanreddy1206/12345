package com.paulusworld.drawernavigationtabs;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People.LoadPeopleResult;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.Person.Image;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class LoginActivity extends Activity implements ConnectionCallbacks, OnConnectionFailedListener, ResultCallback<LoadPeopleResult> {
	/* Request code used to invoke sign in user interactions. */
	private static final int RC_SIGN_IN = 0;

	/* Client used to interact with Google APIs. */
	private GoogleApiClient mGoogleApiClient;

	/*
	 * A flag indicating that a PendingIntent is in progress and prevents us
	 * from starting further intents.
	 */
	private boolean mIntentInProgress;

	private static final int SOME_REQUEST_CODE = 10;

	private static final String TAG = LoginActivity.class.getSimpleName();

	private NetworkImageView profilePicNetworkImageView;
    private ImageLoader profilePicImageLoader;
    private EditText displayName;
    private EditText age;
    private RadioGroup gender;
    private RadioButton male;
    private RadioButton female;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		setTitle(R.string.registration_title);
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_LOGIN)
				.addScope(Plus.SCOPE_PLUS_PROFILE)
				.build();
		profilePicNetworkImageView = (NetworkImageView) findViewById(R.id.registration_profile_pic);
		displayName = (EditText) findViewById(R.id.registration_display_name);
		age = (EditText)findViewById(R.id.registration_age);
		gender = (RadioGroup)findViewById(R.id.registration_radio_group_gender);
		male = (RadioButton)findViewById(R.id.registration_radio_male);
		female = (RadioButton)findViewById(R.id.registration_radio_female);
		
		// setContentView(R.layout.activity_main);
		/*
		 * Intent intent = AccountPicker.newChooseAccountIntent(null, null, new
		 * String[]{"com.google"}, false, null, null, null, null);
		 * startActivityForResult(intent, SOME_REQUEST_CODE);
		 */

	}

	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}

	protected void onStop() {
		super.onStop();

		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		if (requestCode == RC_SIGN_IN) {
			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		} else if (requestCode == SOME_REQUEST_CODE && resultCode == RESULT_OK) {
			String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
			System.out.println(accountName);
		}
	}

	public void onConnectionFailed(ConnectionResult result) {
		if (!mIntentInProgress && result.hasResolution()) {
			try {
				mIntentInProgress = true;
				startIntentSenderForResult(result.getResolution().getIntentSender(), RC_SIGN_IN, null, 0, 0, 0);
			} catch (SendIntentException e) {
				// The intent was canceled before it was sent. Return to the
				// default
				// state and attempt to connect to get an updated
				// ConnectionResult.
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}

	public void onConnected(Bundle connectionHint) {
		// We've resolved any connection errors. mGoogleApiClient can be used to
		// access Google APIs on behalf of the user.
		System.out.println("connected");
		Plus.PeopleApi.loadVisible(mGoogleApiClient, null).setResultCallback(this);
		Person currentPerson = 	Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
		if (currentPerson != null) {
			displayPerson(currentPerson);
		 }
		else
		{
			System.out.println("current person null");
		}
	}
	private void displayPerson(Person person)
	{
		String personName = person.getDisplayName();
	    Image personPhoto = person.getImage();
	    String personGooglePlusProfile = person.getUrl();
	    System.out.println(person.getAgeRange());
	    System.out.println(person);
	    Toast.makeText(this, personName, Toast.LENGTH_SHORT).show();
		displayName.setText(personName);
	    profilePicImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getImageLoader();
        //Image URL - This can point to any image file supported by Android
        profilePicImageLoader.get(personPhoto.getUrl().split("\\?")[0], ImageLoader.getImageListener(profilePicNetworkImageView,
        		android.R.drawable.ic_menu_add, android.R.drawable.ic_dialog_alert));
        profilePicNetworkImageView.setImageUrl(personPhoto.getUrl().split("\\?")[0], profilePicImageLoader);
        age.setText(person.getAgeRange().getMin()+"");
        int genderInt=person.getGender();
        if(genderInt == 0)
        	gender.check(male.getId());
        else if(genderInt == 1)
        	gender.check(female.getId());
	}
	public void onConnectionSuspended(int cause) {
		  mGoogleApiClient.connect();
		}

	@Override
	public void onResult(LoadPeopleResult peopleData) {/*
		// TODO Auto-generated method stub
		System.out.println("ppl result "+peopleData);
		if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
		    PersonBuffer personBuffer = peopleData.getPersonBuffer();
		    try {
		      int count = personBuffer.getCount();
		      for (int i = 0; i < count; i++) {
		        Log.d(TAG, "Display name: " + personBuffer.get(i).getDisplayName());
		      }
		    } finally {
		      personBuffer.close();
		    }
		  } else {
		    Log.e(TAG, "Error requesting people data: " + peopleData.getStatus());
		  }
		Person currentPerson = 	Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
		if (currentPerson != null) {
		    String personName = currentPerson.getDisplayName();
		    Image personPhoto = currentPerson.getImage();
		    String personGooglePlusProfile = currentPerson.getUrl();
		    System.out.println(currentPerson);
		 }
	*/}
}
