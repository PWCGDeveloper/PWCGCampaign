package pwcg.campaign.io.json;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.codehaus.jackson.map.ObjectMapper;

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
        Gson gson= new GsonBuilder().setPrettyPrinting().setDateFormat("yyyyMMdd").create();
        String filepath = directory + filename;
			
        try (JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(filepath), StandardCharsets.UTF_8)))
        {			
			T object = gson.fromJson(reader, typeParameterClass);
			reader.close();
			return object;
		} 
		catch (Exception e)
		{
            PWCGLogger.log(LogLevel.ERROR, "Error reading file " + filename);
            PWCGLogger.logException(e );
            throw new PWCGException(e.getMessage());
		}
	}

	   public T readJsonFileJackson(String directory, String filename) throws PWCGException
	    {
	        ObjectMapper objectMapper = new ObjectMapper();

	        String filepath = directory + filename;
	            

	        try
	        {           
	            File file = new File(filepath);  
	            T object = objectMapper.readValue(file, typeParameterClass);
	            return object;
	        } 
	        catch (Exception e)
	        {
	            PWCGLogger.log(LogLevel.ERROR, "Error reading file " + filename);
	            PWCGLogger.logException(e );
	            throw new PWCGException(e.getMessage());
	        }
	    }


}
