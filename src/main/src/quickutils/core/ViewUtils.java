package quickutils.core;

import android.app.Activity;
import android.view.View;


public class ViewUtils {

	ViewUtils() {}

	/**
	 * Simpler version of {@link View#findViewById(int)} which infers the target type.
	 */
	@SuppressWarnings({ "unchecked" }) public static <T extends View> T findById(View view, int id) {
		return (T) view.findViewById(id);
	}

	/**
	 * Simpler version of {@link Activity#findViewById(int)} which infers the target type.
	 */
	@SuppressWarnings({ "unchecked" }) public static <T extends View> T findById(Activity activity, int id) {
		return (T) activity.findViewById(id);
	}

	/**
	 * Change visibility of the view
	 * 
	 * @param view
	 *            visibility of which will be changed
	 * @param state
	 *            if true View.VISIBLE, otherwise View.INVISIBLE
	 * @return same view
	 */
	public static View visible(View view, boolean state) {
		view.setVisibility(state ? View.VISIBLE : View.INVISIBLE);
		return view;
	}

	/**
	 * Change visibility of the view
	 * 
	 * @param view
	 *            visibility of which will be changed
	 * @param state
	 *            if true View.GONE, otherwise View.VISIBLE
	 * @return same view
	 */
	public static View gone(View view, boolean state) {
		view.setVisibility(state ? View.GONE : View.VISIBLE);
		return view;
	}
}