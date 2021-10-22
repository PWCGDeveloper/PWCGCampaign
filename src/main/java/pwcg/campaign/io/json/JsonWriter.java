package pwcg.campaign.io.json;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;

public class JsonWriter<T>
{

    public void writeAsJson(T javaObject, String directory, String filename) throws PWCGException
    {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(directory + filename), StandardCharsets.UTF_8))
        {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .setDateFormat("yyyyMMdd")
                    .enableComplexMapKeySerialization()
                    .create();

            gson.toJson(javaObject, writer);
            writer.close();
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
    }
}
