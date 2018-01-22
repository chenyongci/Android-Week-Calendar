package com.android.calendarlibrary.manager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.android.calendarlibrary.CollapseCalendarView;


/**
 * 重新设置尺寸管理器
 * @author MaJian
 *
 */
public class ResizeManager {

	public static final String TAG = "ResizeManager";

	/** View to resize */
	@NonNull
    private CollapseCalendarView mCalendarView;

	/** Distance in px until drag has started */
	private final int mTouchSlop;

	private final int mMinFlingVelocity;

	private final int mMaxFlingVelocity;

	/** Y position on {@link MotionEvent#ACTION_DOWN} */
	private float mDownY;

	/** Y position on {@link MotionEvent#ACTION_DOWN} */
	private float mDownX;

	/** Y position when resizing started */
	private float mDragStartY;

	/** If calendar is currently resizing. */
	private State mState = State.IDLE;

	private VelocityTracker mVelocityTracker;
	private final Scroller mScroller;
	private int type = -1;
	public static final int LEFT = 0;
	public static final int RIGHT = 1;

	@Nullable
    private ProgressManager mProgressManager;

	public ResizeManager(@NonNull CollapseCalendarView calendarView) {
		mCalendarView = calendarView;
		mScroller = new Scroller(calendarView.getContext());
		ViewConfiguration viewConfig = ViewConfiguration.get(mCalendarView.getContext());
		mTouchSlop = viewConfig.getScaledTouchSlop();
		mMinFlingVelocity = viewConfig.getScaledMinimumFlingVelocity();
		mMaxFlingVelocity = viewConfig.getScaledMaximumFlingVelocity();
	}

	public boolean onInterceptTouchEvent(@NonNull MotionEvent ev) {
		final int action = MotionEventCompat.getActionMasked(ev);
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			type = -1;
			return onDownEvent(ev);
		case MotionEvent.ACTION_MOVE:
			mVelocityTracker.addMovement(ev);
			return checkForResizing(ev);
		case MotionEvent.ACTION_UP:
			finishMotionEvent();
			if (type == LEFT) {
				mCalendarView.prev();
			} else if (type == RIGHT) {
				mCalendarView.next();
			}
			return false;
		}
		return false;
	}

	public boolean onTouchEvent(@NonNull MotionEvent event) {
		final int action = MotionEventCompat.getActionMasked(event);
		if (action == MotionEvent.ACTION_MOVE) {
			mVelocityTracker.addMovement(event);
		}
		if (mState == State.DRAGGING) {
			switch (action) {
			case MotionEvent.ACTION_MOVE:
				int deltaY = calculateDistanceForDrag(event);
				mProgressManager.applyDelta(deltaY);
				break;
			case MotionEvent.ACTION_UP:
				finishMotionEvent();
				if (type == LEFT) {
					mCalendarView.prev();
				} else if (type == RIGHT) {
					mCalendarView.next();
				}
				break;
			}

		} else if (action == MotionEvent.ACTION_MOVE) {
			checkForResizing(event);
		} else if (action == MotionEvent.ACTION_UP) {
			if (type == LEFT) {
				mCalendarView.prev();
			} else if (type == RIGHT) {
				mCalendarView.next();
			}
		}

		return true;
	}

	/**
	 * Triggered
	 * @param event Down event
	 */
	private boolean onDownEvent(@NonNull MotionEvent event) {
		if (MotionEventCompat.getActionMasked(event) != MotionEvent.ACTION_DOWN) {
			throw new IllegalStateException("Has to be down event!");
		}
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		} else {
			mVelocityTracker.clear();
		}
		mDownY = event.getY();
		mDownX = event.getX();

		if (!mScroller.isFinished()) {
			mScroller.forceFinished(true);
			if (mScroller.getFinalY() == 0) {
				mDragStartY = mDownY + mScroller.getStartY() - mScroller.getCurrY();
			} else {
				mDragStartY = mDownY - mScroller.getCurrY();
			}
			mState = State.DRAGGING;
			return true;
		} else {
			return false;
		}

	}

	public void recycle() {
		if (mVelocityTracker != null) {
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}
	}

	public boolean checkForResizing(MotionEvent ev) { // FIXME this method should only return true / false. Make another method for starting animation
		if (mState == State.DRAGGING) {
			return true;
		}

		final float yDIff = calculateDistance(ev);
		if (Math.abs(calculateXDistanse(ev)) > Math.abs(calculateDistance(ev))) {
			if (calculateXDistanse(ev) > 100) {
				type = LEFT;
			} else if (calculateXDistanse(ev) < -100) {
				type = RIGHT;
			}
		} else {
			CalendarManager manager = mCalendarView.getManager();
			CalendarManager.State state = manager.getState();
			if (Math.abs(yDIff) > mTouchSlop) { // FIXME this should happen only if dragging int right direction
				mState = State.DRAGGING;
				mDragStartY = ev.getY();
				if (mProgressManager == null) {
					int weekOfMonth = manager.getWeekOfMonth();
					if (state == CalendarManager.State.WEEK) { // always animate in month view
						manager.toggleView();
						mCalendarView.populateLayout();
					}
					mProgressManager = new ProgressManagerImpl(mCalendarView, weekOfMonth, state == CalendarManager.State.MONTH);
				}
				return true;
			}
		}
		return false;
	}

	private void finishMotionEvent() {
		if (mProgressManager != null) {
			if (mProgressManager.isInitialized()) {
				startScolling();
			}
		}
	}

	private void startScolling() {

		mVelocityTracker.computeCurrentVelocity(1000, mMaxFlingVelocity);
		int velocity = (int) mVelocityTracker.getYVelocity();

		if (!mScroller.isFinished()) {
			mScroller.forceFinished(true);
		}

		int progress = mProgressManager.getCurrentHeight();
		int end;
		if (Math.abs(velocity) > mMinFlingVelocity) {

			if (velocity > 0) {
				end = mProgressManager.getEndSize() - progress;
			} else {
				end = -progress;
			}

		} else {

			int endSize = mProgressManager.getEndSize();
			if (endSize / 2 <= progress) {
				end = endSize - progress;
			} else {
				end = -progress;
			}

		}

		mScroller.startScroll(0, progress, 0, end);
		mCalendarView.postInvalidate();

		mState = State.SETTLING;

	}

	private float calculateDistance(MotionEvent event) {
		return (event.getY() - mDownY);
	}
	/**
	 * 计算X方向移动距离
	 * @param event
	 * @return
	 */
	private int calculateXDistanse(MotionEvent event){
		return (int) (event.getX() - mDownX);
	}

	private int calculateDistanceForDrag(MotionEvent event) {
		return (int) (event.getY() - mDragStartY);
	}

	public void onDraw() {
		if (!mScroller.isFinished()) {
			mScroller.computeScrollOffset();
			float position = mScroller.getCurrY() * 1f / mProgressManager.getEndSize();
			mProgressManager.apply(position);
			mCalendarView.postInvalidate();
		} else if (mState == State.SETTLING) {
			mState = State.IDLE;
			float position = mScroller.getCurrY() * 1f / mProgressManager.getEndSize();
			mProgressManager.finish(position > 0);
			mProgressManager = null;
		}

	}

	private enum State {
		IDLE,
		DRAGGING,
		SETTLING
	}
}
