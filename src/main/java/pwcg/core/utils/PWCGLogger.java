package pwcg.core.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;

public class PWCGLogger 
{
    private static LogLevel activeLogLevel = LogLevel.INFO;
    public enum LogLevel 
    {
        DEBUG,
        INFO,
        ERROR
    };
    
    public enum LogCategory
    {
        SKIN,
        VICTORY,
        PILOT_STATUS
    };
    
    private static LogCategory enabledCategories[] = 
    {
    };

    
    public static void logByCategory (LogCategory logCategory, String message)
    {
        FileWriter fw = null;
        PrintWriter pw = null;

        try
        {
            boolean categoryIsEnabled = false;
            for (LogCategory enabledCategory : enabledCategories)
            {
                if (logCategory == enabledCategory)
                {
                    categoryIsEnabled = true;
                }
            }
            
            if (categoryIsEnabled)
            {
                // For the console
                PWCGLogger.log(LogLevel.DEBUG, message);
                
                // to the file
                fw = new FileWriter ("PWCGErrorLog.txt", true);
                pw = new PrintWriter (fw);
                pw.write(message + "\n\n");
            }
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
        finally
        {
            try
            {
                if (fw != null)
                {
                    fw.close();
                }
            }
            catch (Throwable t)
            {
                t.printStackTrace();
            }
            
            try
            {
                if (pw != null)
                {
                    pw.close();
                }
            }
            catch (Throwable t)
            {
                t.printStackTrace();
            }
        }
    }
    

    
    public static void log (LogLevel logLevel, String message)
    {
        FileWriter fw = null;
        PrintWriter pw = null;

        try
        {
            if (logThisMessage(logLevel))
            {
                // For the console
                System.out.println(message);
                
                // to the file
                if (logLevel != LogLevel.DEBUG)
                {
                    fw = new FileWriter ("PWCGErrorLog.txt", true);
                    pw = new PrintWriter (fw);
                    pw.write(message + "\n\n");
                }
            }
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
        finally
        {
            try
            {
                if (fw != null)
                {
                    fw.close();
                }
            }
            catch (Throwable t)
            {
                t.printStackTrace();
            }
            
            try
            {
                if (pw != null)
                {
                    pw.close();
                }
            }
            catch (Throwable t)
            {
                t.printStackTrace();
            }
        }
    }
    
    /**
     * @param logLevel
     * @return
     */
    private static boolean logThisMessage( LogLevel logLevel)
    {
        // Log everything at debug
        if (activeLogLevel ==  LogLevel.DEBUG)
        {
            return true;
        }

        // Log all errors
        if (logLevel ==  LogLevel.ERROR)
        {
            return true;
        }

        // Info logs info and errors
        if (activeLogLevel ==  LogLevel.INFO && logLevel == LogLevel.INFO)
        {
            return true;
        }

        if (activeLogLevel ==  LogLevel.INFO && logLevel == LogLevel.ERROR)
        {
            return true;
        }

        
        return false;
    }

    
    /**
     * 
     */
    public static void eraseLog ()
    {
        try
        {
            // to the file
            File errorLog = new File ("PWCGErrorLog.txt");
            if (errorLog.exists())
            {
                errorLog.delete();
            }
         }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
    }

    
	/**
	 * @param exp
	 */
	public static void logException(Throwable exp)
	{
		FileWriter fw = null;
		PrintWriter pw = null;

		try
		{
			// For the console
		   exp.printStackTrace();
			
			// to the file
			fw = new FileWriter ("PWCGErrorLog.txt", true);
			pw = new PrintWriter (fw);
			Date errorDate = new Date();
			pw.println("");
			pw.println("");
			pw.println("PWCG Error");
			pw.println(errorDate);
            exp.printStackTrace(pw);
            exp.printStackTrace();
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
		finally
		{
			try
			{
				if (fw != null)
				{
					fw.close();
				}
			}
			catch (Throwable t)
			{
	            t.printStackTrace();
			}
			
			try
			{
				if (pw != null)
				{
					pw.close();
				}
			}
			catch (Throwable t)
			{
	            t.printStackTrace();
			}
		}
	}

    public static LogLevel getActiveLogLevel()
    {
        return activeLogLevel;
    }

    public static void setActiveLogLevel(LogLevel activeLogLevel)
    {
        PWCGLogger.activeLogLevel = activeLogLevel;
    }
	
	

}
