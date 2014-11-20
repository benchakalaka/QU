package quickutils.core;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;


/**
 * Represents different notifications such as
 * 
 * @author ihorkarpachev
 */
public class NotificationUtils {
	
	NotificationUtils() {
	}

	/**
	 * Show short toast on UI thread
	 * 
	 * @param context
	 *            application context or activity's context
	 * @param message
	 *            text representation of message to display
	 * @param isShort
	 *            define toast duration
	 */
	public static void showLongToast(final Context context, final String message) {
		// Define toast duration
		try {
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		}
		catch (Exception ex) {
			Activity activity = (Activity) context;
			activity.runOnUiThread(new Runnable() {

				@Override public void run() {
					Toast.makeText(context, message, Toast.LENGTH_LONG).show();
				}
			});
		}
	}

	/**
	 * Show short/long toast on UI thread
	 * 
	 * @param context
	 *            application context or activity's context
	 * @param resourseStringId
	 *            id of message to display
	 * @param isShort
	 *            define toast duration
	 */
	public static void showShortToast(final Context context, final int resourseStringId) {
		try {
			Toast.makeText(context, context.getResources().getString(resourseStringId), Toast.LENGTH_SHORT).show();
		}
		catch (Exception ex) {
			Activity activity = (Activity) context;
			activity.runOnUiThread(new Runnable() {

				@Override public void run() {
					Toast.makeText(context, context.getResources().getString(resourseStringId), Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
	
	/**
	 * Show short/long toast on UI thread
	 * 
	 * @param context
	 *            application context or activity's context
	 * @param resourseStringId
	 *            id of message to display
	 * @param isShort
	 *            define toast duration
	 */
	public static void showShortToast(final Context context, final String message) {
		try {
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		}
		catch (Exception ex) {
			Activity activity = (Activity) context;
			activity.runOnUiThread(new Runnable() {

				@Override public void run() {
					Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

}
