package pwcg.dev.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class FileRename
{
    
    /**
     * Copy the contents of the input to the output
     * 
     * @param from
     * @param to
     */
    public static void copyFile (File from, File to)
    {   
        InputStream inStream = null;
        OutputStream outStream = null;
        try
        {
            inStream = new FileInputStream(from);
            outStream = new FileOutputStream(to);
 
            byte[] buffer = new byte[1024];
 
            int length;
            while ((length = inStream.read(buffer)) > 0){
                outStream.write(buffer, 0, length);
            }
 
            if (inStream != null)inStream.close();
            if (outStream != null)outStream.close();
 
            PWCGLogger.log(LogLevel.DEBUG, "File Copied..");
        }
        catch(IOException e)
        {
             PWCGLogger.logException(e);
        }
    }

}
