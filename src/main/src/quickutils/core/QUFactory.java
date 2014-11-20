package quickutils.core;

public abstract class QUFactory {

	/**
	 * private constructor
	 */
	private QUFactory() {}

	/**
	 * Notifications Utils
	 * 
	 * @author ihorkarpachev
	 */
	public static class QNotifications extends quickutils.core.NotificationUtils {
	}

	/**
	 * Logging Utils
	 * 
	 * @author ihorkarpachev
	 */
	public static class QLog extends quickutils.core.LogUtils {
	}

	/**
	 * System Utils
	 * 
	 * @author ihorkarpachev
	 */
	public static class QSystem extends quickutils.core.SystemUtils {
	}

	/**
	 * String Utils
	 * 
	 * @author ihorkarpachev
	 */
	public static class QString extends quickutils.core.StringUtils {
	}

	/**
	 * Math Utils
	 * 
	 * @author ihorkarpachev
	 */
	public static class QMath extends quickutils.core.MathUtils {
	}

	/**
	 * Date Utils
	 * 
	 * @author ihorkarpachev
	 */
	public static class QDate extends quickutils.core.DateUtils {
	}

	/**
	 * Share Utils
	 * 
	 * @author ihorkarpachev
	 */
	public static class QShare extends quickutils.core.ShareUtils {
	}

	/**
	 * SDCard Utils
	 * 
	 * @author ihorkarpachev
	 */
	public static class QSDcard extends quickutils.core.SDcardUtils {
	}

	/**
	 * Security Utils
	 * 
	 * @author ihorkarpachev
	 */
	public static class QSecurity extends quickutils.core.SecurityUtils {
	}

	/**
	 * Animation Utils
	 * 
	 * @author ihorkarpachev
	 */
	public static class QAnimation extends quickutils.core.AnimationUtils {
	}

	/**
	 * Image Utils
	 * 
	 * @author ihorkarpachev
	 */
	public static class QImage extends quickutils.core.ImageUtils {
	}

	/**
	 * Collection Utils
	 * 
	 * @author ihorkarpachev
	 */
	public static class QCollection extends quickutils.core.CollectionUtils {
	}

	/**
	 * Prediction Utils
	 * 
	 * @author ihorkarpachev
	 */
	public static class QPreconditions extends quickutils.core.PreconditionsUtils {
	}

	/**
	 * View Utils
	 * 
	 * @author ihorkarpachev
	 */
	public static class QViews extends ViewUtils {
	}

}
