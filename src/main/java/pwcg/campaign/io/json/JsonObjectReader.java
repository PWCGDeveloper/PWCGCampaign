package pwcg.campaign.io.json;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class JsonObjectReader<T>
{
    final Class<T> typeParameterClass;

    public JsonObjectReader(Class<T> typeParameterClass) 
    {
        this.typeParameterClass = typeParameterClass;
    }
    
	public T readJsonFile(String directory, String filename) throws PWCGException
	{
        JsonReader reader = null;
		try
		{
		    Gson gson= new GsonBuilder().setPrettyPrinting().setDateFormat("yyyyMMdd").create();
			String filepath = directory + filename;
			
			reader = new JsonReader(new InputStreamReader(new FileInputStream(filepath), "UTF-8"));
			
			T object = gson.fromJson(reader, typeParameterClass);
			reader.close();
			return object;
		} 
		catch (Exception e)
		{
            try
            {
                if (reader != null)
                {
                    reader.close();
                }
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
            PWCGLogger.log(LogLevel.ERROR, "Error reading file " + filename);
            PWCGLogger.logException(e );
            throw new PWCGException(e.getMessage());
		}
	}

}
