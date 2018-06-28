package pwcg.campaign.io.json;

import java.io.FileWriter;
import java.io.Writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;

public class JsonWriter<T>
{

	public void writeAsJson(T javaObject, String directory, String filename) throws PWCGException
	{
		try (Writer writer = new FileWriter(directory + filename))
		{
			Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyyMMdd").create();

			gson.toJson(javaObject, writer);
		}
		catch (Exception e)
		{
			Logger.logException(e);
			throw new PWCGException(e.getMessage());
		}
	}
}
