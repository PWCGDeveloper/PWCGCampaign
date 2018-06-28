package pwcg.campaign.io.json;

import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;

public class JsonObjectReader<T>
{
    final Class<T> typeParameterClass;

    public JsonObjectReader(Class<T> typeParameterClass) 
    {
        this.typeParameterClass = typeParameterClass;
    }
    
	public T readJsonFile(String directory, String filename) throws PWCGException
	{
		try
		{
		    Gson gson= new GsonBuilder().setPrettyPrinting().setDateFormat("yyyyMMdd").create();
			JsonReader reader;
			String filepath = directory + filename;
			reader = new JsonReader(new FileReader(filepath));
			T object = gson.fromJson(reader, typeParameterClass);
			reader.close();
			return object;
		} 
		catch (Exception e)
		{
            Logger.logException(e);
            throw new PWCGException(e.getMessage());
		}
	}

}
