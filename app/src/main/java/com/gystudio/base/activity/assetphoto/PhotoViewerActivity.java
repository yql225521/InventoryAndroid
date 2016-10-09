package com.gystudio.base.activity.assetphoto;

import java.util.ArrayList;
import java.util.List;

import com.gystudio.base.R;
import com.gystudio.utils.ToolLog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PhotoViewerActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

	private static final String STATE_POSITION = "STATE_POSITION";
	protected final String TAG = this.getClass().getSimpleName(); 
	/**
	 * 查看器图片默认选中位置
	 */
	public final static String PICTURE_VIEWER_DEFAULT_POSTION = "defaultPostion";
	/**
	 * 查看器数据源
	 */
	public final static String PICTURE_VIEWER_DATASOURCE = "pictureViewerDatasource";
	/**
	 * 图片数据源
	 */
	private ArrayList<PictureBean> dataSource = new ArrayList<PictureBean>();

	/**
	 * 可滑动的图片查看器
	 */
	private ViewPager mImageViewPager;
	/**
	 * 图片VP适配器
	 */
	private ImagePagerAdapter mImageVpAdapter;
	/**
	 * 指示器
	 */
	private TextView indicator;
	/**
	 * 当前默认选中位置
	 */
	private int mDefaultIndex = 0;

	private ViewGroup mContextView = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			mDefaultIndex = savedInstanceState.getInt(STATE_POSITION);
		}
		mContextView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.common_picture_viewer, null);
		setContentView(mContextView);
		setContentView(R.layout.common_picture_viewer);

		// 初始化参数
		Bundle bundle = getIntent().getExtras();
		if (bundle == null) {
			bundle = new Bundle();
		}
		initParms(bundle);
		// 初始化控件
		initView(mContextView);
	}

	private void initView(View view) {
		mImageVpAdapter = new ImagePagerAdapter(getSupportFragmentManager(), dataSource);

		// 图片Viewpager、指示文本
		mImageViewPager = (ViewPager) findViewById(R.id.pager);
		mImageViewPager.setAdapter(mImageVpAdapter);
		mImageViewPager.setOnPageChangeListener(this);

		// 默认初始化
		indicator = (TextView) findViewById(R.id.indicator);
		CharSequence text = getString(R.string.viewpager_indicator, mDefaultIndex + 1, mImageVpAdapter.getCount());
		indicator.setText(text);
		mImageViewPager.setCurrentItem(mDefaultIndex);

	}

	public void initParms(Bundle parms) {
		try {
			mDefaultIndex = parms.getInt(PICTURE_VIEWER_DEFAULT_POSTION, mDefaultIndex);
			ArrayList<PictureBean> imageData = (ArrayList<PictureBean>) parms
					.getSerializable(PICTURE_VIEWER_DATASOURCE);
			if (null != imageData && imageData.size() > 0) {
				dataSource.addAll(imageData);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ToolLog.e(TAG, "获取参数发生异常，原因：" + e.getMessage());
		}
	}

	public void hiddeTitleBar() {
		// 标题栏容器
		// View mTitleBarContainer =
		// findViewById(BaseView.gainResId(mApplication, BaseView.ID,
		// "ll_title"));
		// if (null == mTitleBarContainer) {
		// return;
		// }
		// mTitleBarContainer.setVisibility(View.GONE);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, mImageViewPager.getCurrentItem());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 图片适配器
	 *
	 */
	class ImagePagerAdapter extends FragmentStatePagerAdapter {

		public List<PictureBean> fileList;

		public ImagePagerAdapter(FragmentManager fm, List<PictureBean> fileList) {
			super(fm);
			this.fileList = fileList;
		}

		@Override
		public int getCount() {
			return fileList == null ? 0 : fileList.size();
		}

		@Override
		public Fragment getItem(int position) {
			return PictureItemFragment.newInstance(fileList.get(position));
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) { 

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		CharSequence text = getString(R.string.viewpager_indicator, position + 1,
				mImageViewPager.getAdapter().getCount());
		indicator.setText(text);
	}

}
