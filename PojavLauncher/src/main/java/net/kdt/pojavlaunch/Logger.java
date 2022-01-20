package net.kdt.pojavlaunch;

import androidx.annotation.Keep;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.ref.WeakReference;

/** Singleton class made to log on one file
 * The singleton part can be removed but will require more implementation from the end-dev
 */
@Keep
public class Logger {
    private static Logger loggerSingleton = null;

    /* Instance variables */
    private PrintStream logStream;
    private WeakReference<eventLogListener> logListenerWeakReference = null;

    /* No public construction */

    public static Logger getInstance(){
        if(loggerSingleton == null){
            synchronized(Logger.class){
                if(loggerSingleton == null){
                    loggerSingleton = new Logger();
                }
            }
        }
        return loggerSingleton;
    }


    /** Print the text to the log file if not censored */
    public void appendToLog(String text){
        if(shouldCensorLog(text)) return;
        appendToLogUnchecked(text);
    }

    /** Print the text to the log file, no china censoring there */
    public void appendToLogUnchecked(String text){
        logStream.println(text);
        notifyLogListener(text);
    }

    /** Disables the printing */
    public void shutdown(){
        logStream.close();
    }

    /**
     * Perform various checks to see if the log is safe to print
     * Subclasses may want to override this behavior
     * @param text The text to check
     * @return Whether the log should be censored
     */
    private static boolean shouldCensorLog(String text){
        if(text.contains("Session ID is")) return true;
        return false;
    }

    /** Small listener for anything listening to the log */
    public interface eventLogListener {
        void onEventLogged(String text);
    }

    /** Link a log listener to the logger */
    public void setLogListener(eventLogListener logListener){
        this.logListenerWeakReference = new WeakReference<>(logListener);
    }

    /** Notifies the event listener, if it exists */
    private void notifyLogListener(String text){
        if(logListenerWeakReference == null) return;
        eventLogListener logListener = logListenerWeakReference.get();
        if(logListener == null){
            logListenerWeakReference = null;
            return;
        }
        logListener.onEventLogged(text);
    }
}
