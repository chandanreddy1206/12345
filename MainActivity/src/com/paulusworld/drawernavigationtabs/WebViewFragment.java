package com.paulusworld.drawernavigationtabs;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.Fragment.SavedState;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class WebViewFragment extends Fragment {

	public final static String TAG = WebViewFragment.class.getSimpleName();
	
	public WebViewFragment() {
		// TODO Auto-generated constructor stub
	}

	public static WebViewFragment newInstance() {
		return new WebViewFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_webview, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		WebView webView = (WebView) view.findViewById(R.id.webView);
		webView.loadUrl("http://www.paulusworld.com");
	}
}
