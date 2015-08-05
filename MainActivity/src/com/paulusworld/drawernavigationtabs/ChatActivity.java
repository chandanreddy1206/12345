package com.paulusworld.drawernavigationtabs;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.R.attr;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ChatActivity extends Fragment {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	public static final String TAG = ChatActivity.class.getSimpleName();

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	public static ChatActivity newInstance() {
		return new ChatActivity();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_chat, container, false);
		mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

		mViewPager = (ViewPager) v.findViewById(R.id.chat_pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		
		return v;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = new VoiceChatFragment();
				break;
			case 1:
				fragment = new VideoChatFragment();
				break;
			default:
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 2 total pages(Voice & Video).
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.chat_voice).toUpperCase(l);
			case 1:
				return getString(R.string.chat_video).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A fragment representing Voice Chat. Facilitates text and voice chat.
	 */
	public static class VoiceChatFragment extends Fragment {

		private ArrayAdapter<String> listAdapter;

		public VoiceChatFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_tabbed_chat_voice, container, false);
			ListView listView = (ListView) rootView.findViewById(R.id.chat_voice_list_view);
			List<String> strings = new ArrayList<String>();
			for (int i = 0; i < 100; i++) {
				strings.add(i + "");
			}
			listAdapter = new ArrayAdapter<String>(getActivity(), R.layout.drawer_list_item, strings);
			listView.setAdapter(listAdapter);
			listView.setOnScrollListener(new AbsListView.OnScrollListener() {
				private int mLastFirstVisibleItem;

				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
					TypedValue tv = new TypedValue();
					if (mLastFirstVisibleItem < firstVisibleItem) {
						getActivity().getActionBar().hide();
						
					}
					if (mLastFirstVisibleItem > firstVisibleItem) {

						if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
						{
						    System.out.println(TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics()));
						}
						getActivity().getActionBar().show();
					}
					mLastFirstVisibleItem = firstVisibleItem;
				}
			});
			return rootView;
		}
	}

	/**
	 * A fragment representing Video Chat. But displays
	 * "Premium package. Not available for Indian users".
	 */
	public static class VideoChatFragment extends Fragment {

		public VideoChatFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_tabbed_chat_video, container, false);
			TextView dummyTextView = (TextView) rootView.findViewById(R.id.chat_video_section_label);
			dummyTextView.setText("Premium package. Not available for Indian users");
			return rootView;
		}
	}

}
