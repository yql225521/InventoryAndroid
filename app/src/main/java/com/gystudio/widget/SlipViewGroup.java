package com.gystudio.widget;


import java.util.ArrayList;

import com.gystudio.base.R;
import com.gystudio.base.activity.Main;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Scroller;


public class SlipViewGroup extends ViewGroup {
	private static class WorkspaceOvershootInterpolator implements Interpolator {
		private static final float DEFAULT_TENSION = 1.3f;
		private float mTension;

		public WorkspaceOvershootInterpolator() {
			mTension = DEFAULT_TENSION;
		}

		public void setDistance(int distance) {
			mTension = distance > 0 ? DEFAULT_TENSION / distance
					: DEFAULT_TENSION;
		}

		public void disableSettle() {
			mTension = 0.f;
		}

		public float getInterpolation(float t) {
			// _o(t) = t * t * ((tension + 1) * t + tension)
			// o(t) = _o(t - 1) + 1
			t -= 1.0f;
			return t * t * ((mTension + 1) * t + mTension) + 1.0f;
		}
	}

	public static class SavedState extends BaseSavedState {
		int currentScreen = -1;

		SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			currentScreen = in.readInt();
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeInt(currentScreen);
		}

		public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}

	private static final int INVALID_SCREEN = -1;
	/**
	 * The velocity at which a fling gesture will cause us to snap to the next
	 * screen
	 */
	private static final int SNAP_VELOCITY = 600;
	private WallpaperManager mWallpaperManager;
	private int mDefaultScreen;
	private boolean mFirstLayout = true;
	private int mCurrentScreen;
	private int mNextScreen = INVALID_SCREEN;
	private Scroller mScroller;
	private VelocityTracker mVelocityTracker;
	private float mLastMotionX;
	private float mLastMotionY;
	private final static int TOUCH_STATE_REST = 0;
	private final static int TOUCH_STATE_SCROLLING = 1;
	private int mTouchState = TOUCH_STATE_REST;
	private OnLongClickListener mLongClickListener;
	private boolean mAllowLongPress = true;
	private int mTouchSlop;
	private int mMaximumVelocity;
	private static final int INVALID_POINTER = -1;
	private int mActivePointerId = INVALID_POINTER;
	private Drawable mPreviousIndicator;
	private Drawable mNextIndicator;
	private static final float NANOTIME_DIV = 1000000000.0f;
	private static final float SMOOTHING_SPEED = 0.75f;
	private static final float SMOOTHING_CONSTANT = (float) (0.016 / Math
			.log(SMOOTHING_SPEED));
	private float mSmoothingTime;
	private float mTouchX;
	private WorkspaceOvershootInterpolator mScrollInterpolator;
	private static final float BASELINE_FLING_VELOCITY = 2500.f;
	private static final float FLING_VELOCITY_INFLUENCE = 0.4f;
	private int currentScreenIndex = 0;
	private int oldScreenIndex = 0;

	public SlipViewGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mWallpaperManager = WallpaperManager.getInstance(context);
		// TypedArray a = context.obtainStyledAttributes(attrs,
		// R.styleable.Workspace, defStyle, 0);
		// mDefaultScreen = a.getInt(R.styleable.Workspace_defaultScreen, 1);
		// a.recycle();
		setHapticFeedbackEnabled(false);
		initWorkspace();
	}

	public SlipViewGroup(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlipViewGroup(Context context) {
		this(context, null);
	}

	private void initWorkspace() {
		Context context = getContext();
		mScrollInterpolator = new WorkspaceOvershootInterpolator();
		mScroller = new Scroller(context, mScrollInterpolator);
		mCurrentScreen = mDefaultScreen;
		final ViewConfiguration configuration = ViewConfiguration
				.get(getContext());
		mTouchSlop = configuration.getScaledTouchSlop();
		mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
		setIndicators(new BitmapDrawable(), new BitmapDrawable());
	}

	public void setIndicators(Drawable previous, Drawable next) {
		mPreviousIndicator = previous;
		mNextIndicator = next;
		previous.setLevel(mCurrentScreen);
		next.setLevel(mCurrentScreen);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		int childLeft = 0;
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() != View.GONE) {
				final int childWidth = child.getMeasuredWidth();
				child.layout(childLeft, 0, childLeft + childWidth,
						child.getMeasuredHeight());
				childLeft += childWidth;
			}
		}
	}

	void enableChildrenCache(int fromScreen, int toScreen) {
		if (fromScreen > toScreen) {
			final int temp = fromScreen;
			fromScreen = toScreen;
			toScreen = temp;
		}
		final int count = getChildCount();
		fromScreen = Math.max(fromScreen, 0);
		toScreen = Math.min(toScreen, count - 1);
		for (int i = fromScreen; i <= toScreen; i++) {
			final ViewGroup layout = (ViewGroup) getChildAt(i);
			// layout.setChildrenDrawnWithCacheEnabled(true);
			// layout.setChildrenDrawingCacheEnabled(true);
		}
	}

	void clearChildrenCache() {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final ViewGroup layout = (ViewGroup) getChildAt(i);
			// layout.setChildrenDrawnWithCacheEnabled(false);
		}
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			mTouchX = mScroller.getCurrX();
			mSmoothingTime = System.nanoTime() / NANOTIME_DIV;
			super.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			// updateWallpaperOffset();
			postInvalidate();
		} else if (mNextScreen != INVALID_SCREEN) {
			mCurrentScreen = Math.max(0,
					Math.min(mNextScreen, getChildCount() - 1));
			mPreviousIndicator.setLevel(mCurrentScreen);
			mNextIndicator.setLevel(mCurrentScreen);
			// Launcher.setScreen(mCurrentScreen);
			mNextScreen = INVALID_SCREEN;
			clearChildrenCache();
		} else if (mTouchState == TOUCH_STATE_SCROLLING) {
			final float now = System.nanoTime() / NANOTIME_DIV;
			final float e = (float) Math.exp((now - mSmoothingTime)
					/ SMOOTHING_CONSTANT);
			final float dx = mTouchX - getScrollX();
			super.scrollTo(getScrollX() + (int) (dx * e), getScrollY());
			mSmoothingTime = now;
			// Keep generating points as long as we're more than 1px away from
			// the target
			if (dx > 1.f || dx < -1.f) {
				// updateWallpaperOffset();
				postInvalidate();
			}
		}
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		boolean restore = false;
		int restoreCount = 0;
		// ViewGroup.dispatchDraw() supports many features we don't need:
		// clip to padding, layout animation, animation listener, disappearing
		// children, etc. The following implementation attempts to fast-track
		// the drawing dispatch by drawing only what we know needs to be drawn.
		boolean fastDraw = mTouchState != TOUCH_STATE_SCROLLING
				&& mNextScreen == INVALID_SCREEN;
		// If we are not scrolling or flinging, draw only the current screen
		if (fastDraw) {
			drawChild(canvas, getChildAt(mCurrentScreen), getDrawingTime());
		} else {
			final long drawingTime = getDrawingTime();
			final float scrollPos = (float) getScrollX() / getWidth();
			final int leftScreen = (int) scrollPos;
			final int rightScreen = leftScreen + 1;
			if (leftScreen >= 0) {
				drawChild(canvas, getChildAt(leftScreen), drawingTime);
			}
			if (scrollPos != leftScreen && rightScreen < getChildCount()) {
				drawChild(canvas, getChildAt(rightScreen), drawingTime);
			}
		}
		if (restore) {
			canvas.restoreToCount(restoreCount);
		}
	}

	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		computeScroll();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int width = MeasureSpec.getSize(widthMeasureSpec);
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		if (widthMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException(
					"Workspace can only be used in EXACTLY mode.");
		}
		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		if (heightMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException(
					"Workspace can only be used in EXACTLY mode.");
		}
		// The children are given the same width and height as the workspace
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}
		if (mFirstLayout) {
			setHorizontalScrollBarEnabled(false);
			scrollTo(mCurrentScreen * width, 0);
			setHorizontalScrollBarEnabled(true);
			// updateWallpaperOffset(width * (getChildCount() - 1));
			mFirstLayout = false;
		}
	}

	@Override
	public boolean requestChildRectangleOnScreen(View child, Rect rectangle,
			boolean immediate) {
		int screen = indexOfChild(child);
		if (screen != mCurrentScreen || !mScroller.isFinished()) {
			snapToScreen(screen);
			return true;
		}
		return false;
	}

	@Override
	public boolean dispatchUnhandledMove(View focused, int direction) {
		if (direction == View.FOCUS_LEFT) {
			if (mCurrentScreen > 0) {
				snapToScreen(mCurrentScreen - 1);
				return true;
			}
		} else if (direction == View.FOCUS_RIGHT) {
			if (mCurrentScreen < getChildCount() - 1) {
				snapToScreen(mCurrentScreen + 1);
				return true;
			}
		}
		return super.dispatchUnhandledMove(focused, direction);
	}

	@Override
	public void addFocusables(ArrayList<View> views, int direction,
			int focusableMode) {
		getChildAt(mCurrentScreen).addFocusables(views, direction);
		if (direction == View.FOCUS_LEFT) {
			if (mCurrentScreen > 0) {
				getChildAt(mCurrentScreen - 1).addFocusables(views, direction);
			}
		} else if (direction == View.FOCUS_RIGHT) {
			if (mCurrentScreen < getChildCount() - 1) {
				getChildAt(mCurrentScreen + 1).addFocusables(views, direction);
			}
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		if ((action == MotionEvent.ACTION_MOVE)
				&& (mTouchState != TOUCH_STATE_REST)) {
			return true;
		}
		acquireVelocityTrackerAndAddMovement(ev);
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_MOVE: {
			/*
			 * mIsBeingDragged == false, otherwise the shortcut would have
			 * caught it. Check whether the user has moved far enough from his
			 * original down touch.
			 */
			/*
			 * Locally do absolute value. mLastMotionX is set to the y value of
			 * the down event.
			 */
			final int pointerIndex = ev.findPointerIndex(mActivePointerId);
			final float x = ev.getX(pointerIndex);
			final float y = ev.getY(pointerIndex);
			final int xDiff = (int) Math.abs(x - mLastMotionX);
			final int yDiff = (int) Math.abs(y - mLastMotionY);
			final int touchSlop = mTouchSlop;
			boolean xMoved = xDiff > touchSlop;
			boolean yMoved = yDiff > touchSlop;
			if (xMoved || yMoved) {
				if (xMoved) {
					// Scroll if the user moved far enough along the X axis
					mTouchState = TOUCH_STATE_SCROLLING;
					mLastMotionX = x;
					mTouchX = getScrollX();
					mSmoothingTime = System.nanoTime() / NANOTIME_DIV;
					enableChildrenCache(mCurrentScreen - 1, mCurrentScreen + 1);
				}
				// Either way, cancel any pending longpress
				if (mAllowLongPress) {
					mAllowLongPress = false;
					// Try canceling the long press. It could also have been
					// scheduled
					// by a distant descendant, so use the mAllowLongPress flag
					// to block
					// everything
					final View currentScreen = getChildAt(mCurrentScreen);
					currentScreen.cancelLongPress();
				}
			}
			break;
		}
		case MotionEvent.ACTION_DOWN: {
			final float x = ev.getX();
			final float y = ev.getY();
			// Remember location of down touch
			mLastMotionX = x;
			mLastMotionY = y;
			mActivePointerId = ev.getPointerId(0);
			mAllowLongPress = true;
			/*
			 * If being flinged and user touches the screen, initiate drag;
			 * otherwise don't. mScroller.isFinished should be false when being
			 * flinged.
			 */
			mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST
					: TOUCH_STATE_SCROLLING;
			break;
		}
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			if (mTouchState != TOUCH_STATE_SCROLLING) {
			}
			// Release the drag
			clearChildrenCache();
			mTouchState = TOUCH_STATE_REST;
			mActivePointerId = INVALID_POINTER;
			mAllowLongPress = false;
			releaseVelocityTracker();
			break;
		case MotionEvent.ACTION_POINTER_UP:
			onSecondaryPointerUp(ev);
			break;
		}
		/*
		 * The only time we want to intercept motion events is if we are in the
		 * drag mode.
		 */
		return mTouchState != TOUCH_STATE_REST;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		acquireVelocityTrackerAndAddMovement(ev);
		final int action = ev.getAction();
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			/*
			 * If being flinged and user touches, stop the fling. isFinished
			 * will be false if being flinged.
			 */
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			// Remember where the motion event started
			mLastMotionX = ev.getX();
			mActivePointerId = ev.getPointerId(0);
			mTouchState = TOUCH_STATE_SCROLLING;
			if (mTouchState == TOUCH_STATE_SCROLLING) {
				enableChildrenCache(mCurrentScreen - 1, mCurrentScreen + 1);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (mTouchState == TOUCH_STATE_SCROLLING) {
				// Scroll to follow the motion event
				final int pointerIndex = ev.findPointerIndex(mActivePointerId);
				final float x = ev.getX(pointerIndex);
				final float deltaX = mLastMotionX - x;
				mLastMotionX = x;
				if (deltaX < 0) {
					if (mTouchX > 0) {
						mTouchX += Math.max(-mTouchX, deltaX);
						mSmoothingTime = System.nanoTime() / NANOTIME_DIV;
						invalidate();
					}
				} else if (deltaX > 0) {
					final float availableToScroll = getChildAt(
							getChildCount() - 1).getRight()
							- mTouchX - getWidth();
					if (availableToScroll > 0) {
						mTouchX += Math.min(availableToScroll, deltaX);
						mSmoothingTime = System.nanoTime() / NANOTIME_DIV;
						invalidate();
					}
				} else {
					awakenScrollBars();
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mTouchState == TOUCH_STATE_SCROLLING) {
				final VelocityTracker velocityTracker = mVelocityTracker;
				velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
				// final int velocityX = (int)
				// velocityTracker.getXVelocity(mActivePointerId);
				final int velocityX = (int) velocityTracker.getXVelocity();
				final int screenWidth = getWidth();
				final int whichScreen = (getScrollX() + (screenWidth / 2))
						/ screenWidth;
				final float scrolledPos = (float) getScrollX() / screenWidth;
				if (velocityX > SNAP_VELOCITY && mCurrentScreen > 0) {
					// Fling hard enough to move left.
					// Don't fling across more than one screen at a time.
					final int bound = scrolledPos < whichScreen ? mCurrentScreen - 1
							: mCurrentScreen;
					snapToScreen(Math.min(whichScreen, bound), velocityX, true);
				} else if (velocityX < -SNAP_VELOCITY
						&& mCurrentScreen < getChildCount() - 1) {
					// Fling hard enough to move right
					// Don't fling across more than one screen at a time.
					final int bound = scrolledPos > whichScreen ? mCurrentScreen + 1
							: mCurrentScreen;
					snapToScreen(Math.max(whichScreen, bound), velocityX, true);
				} else {
					snapToScreen(whichScreen, 0, true);
				}
			}
			mTouchState = TOUCH_STATE_REST;
			mActivePointerId = INVALID_POINTER;
			releaseVelocityTracker();
			break;
		case MotionEvent.ACTION_CANCEL:
			if (mTouchState == TOUCH_STATE_SCROLLING) {
				final int screenWidth = getWidth();
				final int whichScreen = (getScrollX() + (screenWidth / 2))
						/ screenWidth;
				snapToScreen(whichScreen, 0, true);
			}
			mTouchState = TOUCH_STATE_REST;
			mActivePointerId = INVALID_POINTER;
			releaseVelocityTracker();
			break;
		case MotionEvent.ACTION_POINTER_UP:
			onSecondaryPointerUp(ev);
			break;
		}
		return true;
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		final SavedState state = new SavedState(super.onSaveInstanceState());
		state.currentScreen = mCurrentScreen;
		return state;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		SavedState savedState = (SavedState) state;
		super.onRestoreInstanceState(savedState.getSuperState());
		if (savedState.currentScreen != -1) {
			mCurrentScreen = savedState.currentScreen;
		}
	}

	void snapToScreen(int whichScreen) {
		snapToScreen(whichScreen, 0, false);
	}

	private void snapToScreen(int whichScreen, int velocity, boolean settle) {
		// if (!mScroller.isFinished()) return;
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		// clearVacantCache();
		enableChildrenCache(mCurrentScreen, whichScreen);
		mNextScreen = whichScreen;
		mPreviousIndicator.setLevel(mNextScreen);
		mNextIndicator.setLevel(mNextScreen);
		View focusedChild = getFocusedChild();
		if (focusedChild != null && whichScreen != mCurrentScreen
				&& focusedChild == getChildAt(mCurrentScreen)) {
			focusedChild.clearFocus();
		}
		final int screenDelta = Math.max(1,
				Math.abs(whichScreen - mCurrentScreen));
		final int newX = whichScreen * getWidth();
		final int delta = newX - getScrollX();
		int duration = (screenDelta + 1) * 100;
		if (!mScroller.isFinished()) {
			mScroller.abortAnimation();
		}
		if (settle) {
			mScrollInterpolator.setDistance(screenDelta);
		} else {
			mScrollInterpolator.disableSettle();
		}
		velocity = Math.abs(velocity);
		if (velocity > 0) {
			duration += (duration / (velocity / BASELINE_FLING_VELOCITY))
					* FLING_VELOCITY_INFLUENCE;
		} else {
			duration += 100;
		}
		awakenScrollBars(duration);
		mScroller.startScroll(getScrollX(), 0, delta, 0, duration);
		/* 改变ScreenPosition */
		if (oldScreenIndex != whichScreen) {
			if (oldScreenIndex < whichScreen) {
				currentScreenIndex++;
			} else if (oldScreenIndex > whichScreen) {
				currentScreenIndex--;
			}

			int count = Main.screenPositionList.size();
			for (int i = 0; i < count; i++) {
				if (i == currentScreenIndex) {
					Main.screenPositionList.get(currentScreenIndex)
							.setImageResource(
									R.drawable.screen_position_focused);
				} else {
					Main.screenPositionList.get(i).setImageResource(
							R.drawable.screen_position_unfocused);
				}
			}
		}
		invalidate();
		oldScreenIndex = whichScreen;
	}

	private void releaseVelocityTracker() {
		if (mVelocityTracker != null) {
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}
	}

	private void onSecondaryPointerUp(MotionEvent ev) {
		final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;
		final int pointerId = ev.getPointerId(pointerIndex);
		if (pointerId == mActivePointerId) {
			// This was our active pointer going up. Choose a new
			// active pointer and adjust accordingly.
			// TODO: Make this decision more intelligent.
			final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
			mLastMotionX = ev.getX(newPointerIndex);
			mLastMotionY = ev.getY(newPointerIndex);
			mActivePointerId = ev.getPointerId(newPointerIndex);
			if (mVelocityTracker != null) {
				mVelocityTracker.clear();
			}
		}
	}

	private void acquireVelocityTrackerAndAddMovement(MotionEvent ev) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(ev);
	}
}
