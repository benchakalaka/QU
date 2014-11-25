package quickutils.core;

import java.util.List;
import java.util.Set;

/**
 * Check preconditions as object is null, list is empty, etc ...
 * 
 * @author ihorkarpachev
 */
public class PreconditionsUtils {

     PreconditionsUtils () {
     }

     /**
      * Check list to null or empty value
      * 
      * @param <T>
      * @param jArray
      * @return
      */
     public static boolean isNullOrEmpty(Set <?> jArray) {
          return (isNull(jArray)) || (jArray.isEmpty());
     }

     /**
      * Check list to null or empty value
      * 
      * @param <T>
      * @param jArray
      * @return
      */
     public static boolean isNullOrEmpty(List <?> jArray) {
          return (isNull(jArray)) || (jArray.isEmpty());
     }

     /**
      * Check if object is null
      * 
      * @param obj
      *             object to check
      * @return true if object is null, otherwise return false
      */
     public static boolean isNull(Object obj) {
          return (null == obj);
     }
}