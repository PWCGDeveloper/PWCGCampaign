package pwcg.campaign.io.json;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;

public class PwcgJsonWriter<T>
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
    

    public void writeAsJson(T javaObject, Class<T> type, String directory, String filename) throws PWCGException
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyyMMdd").enableComplexMapKeySerialization().create();
        File file = new File(directory + filename);
        try (final PrintWriter pWriter = new PrintWriter(file))
        {
            final JsonWriter jWriter = gson.newJsonWriter(pWriter);
            jWriter.setIndent("\t");
            JsonElement element = gson.toJsonTree(javaObject, type);
            gson.toJson(element, jWriter);
            pWriter.println();
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
    }

}
