package quickutils.core;

import java.util.Map;

/**
 * Class represents main logger
 * 
 * @author ihorkarpachev
 */
public class LogUtils {

     LogUtils () {
     }

     /**
      * Sends an INFO log message.
      * 
      * @param message
      *             The message you would like logged.
      */
     public static void info(String message) {
          CallerInfo callerInfo = getCallerInfo();
          android.util.Log.i(callerInfo.tag, callerInfo.methodName + " " + message);
     }

     /**
      * Print map to console
      * 
      * @param mp
      *             map to be printed
      */
     @SuppressWarnings ( "rawtypes" ) public static void printMap(Map mp) {
          if ( null != mp ) {
               for ( Object key : mp.keySet() ) {
                    try {
                         android.util.Log.i("", "( " + key + " = " + mp.get(key) + " )");
                    } catch (Exception ex) {
                         ex.printStackTrace();
                    }
               }
          }
     }

     /**
      * Sends a DEBUG log message and log the exception.
      * 
      * @param message
      *             The message you would like logged.
      * @param throwable
      *             An exception to log
      */
     public static void debug(String message) {
          CallerInfo callerInfo = getCallerInfo();
          android.util.Log.d(callerInfo.tag, callerInfo.methodName + " " + message);
     }

     /**
      * Sends a DEBUG log message and log the exception.
      * 
      * @param message
      *             The message you would like logged.
      * @param throwable
      *             An exception to log
      */
     public static void debug(String message, Throwable throwable) {
          CallerInfo callerInfo = getCallerInfo();
          android.util.Log.d(callerInfo.tag, callerInfo.methodName + " " + message, throwable);
     }

     /**
      * Sends an WARNING log message
      * 
      * @param message
      */
     public static void warning(String message) {
          CallerInfo callerInfo = getCallerInfo();
          android.util.Log.w(callerInfo.tag, callerInfo.methodName + " " + message);
     }

     /**
      * Sends an WARNING log message
      * 
      * @param message
      * @param throwable
      */
     public static void warning(String message, Throwable throwable) {
          CallerInfo callerInfo = getCallerInfo();
          android.util.Log.w(callerInfo.tag, callerInfo.methodName + " " + message, throwable);
     }

     /**
      * Sends an ERROR log message
      * 
      * @param message
      *             The message you would like logged.
      */
     public static void error(String message, Throwable throwable) {
          CallerInfo callerInfo = getCallerInfo();
          android.util.Log.e(callerInfo.tag, callerInfo.methodName + " " + message, throwable);
     }

     /**
      * Sends an ERROR log message
      * 
      * @param message
      *             The message you would like logged.
      */
     public static void error(String message) {
          CallerInfo callerInfo = getCallerInfo();
          android.util.Log.e(callerInfo.tag, callerInfo.methodName + " " + message);
     }

     /**
      * Sends an INFO log message.
      * 
      * @param message
      *             The message you would like logged.
      * @param throwable
      *             error to be displayed
      */
     public static void info(String message, Throwable throwable) {
          CallerInfo callerInfo = getCallerInfo();
          android.util.Log.i(callerInfo.tag, callerInfo.methodName + " " + message, throwable);
     }

     private static CallerInfo getCallerInfo() {
          CallerInfo res = new CallerInfo();
          StackTraceElement element = Thread.currentThread().getStackTrace()[4];
          res.tag = element.getClassName();
          res.tag = res.tag.substring(res.tag.lastIndexOf('.') + 1);
          res.methodName = element.getMethodName() + "( line = " + element.getLineNumber() + " )";
          return res;
     }

     /**
      * CallerInfo class represent holder for method name and class name
      * 
      * @author ihorkarpachev
      */
     private static class CallerInfo {

          public String tag;
          public String methodName;
     }
}