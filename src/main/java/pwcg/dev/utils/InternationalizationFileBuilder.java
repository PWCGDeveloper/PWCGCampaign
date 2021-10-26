package pwcg.dev.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.config.InternationalizationRecords;

public class InternationalizationFileBuilder
{
    private static final String language ="En";
    
    public static void main(String[] args)
    {
        try
        {
            InternationalizationFileBuilder internationalizationFileBuilder = new InternationalizationFileBuilder();
            internationalizationFileBuilder.buildFileForMissingRecords();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void buildFileForMissingRecords() throws Exception 
    {
        String filename = "International." + language + ".json";
                
        InternationalizationRecords internationalization = readLogFile();
        
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8));       
        for (String key : internationalization.getTranslations().keySet())
        {
            String value = internationalization.getTranslations().get(key);
            String line="    \"" + key + "\": \"" + value + "\"," + "\n";
            out.append(line);
        }
        out.close();
    }

    private InternationalizationRecords readLogFile() throws Exception
    {
        InternationalizationRecords internationalization = new InternationalizationRecords();
        
        BufferedReader keyReader = new BufferedReader(new InputStreamReader(new FileInputStream("International.Keys.txt"), StandardCharsets.UTF_8));        
        BufferedReader valueReader  = new BufferedReader(new InputStreamReader(new FileInputStream("International." + language + ".txt"), StandardCharsets.UTF_8));

        String line;
        List<String> keyLines = new ArrayList<>();
        while ((line = keyReader.readLine()) != null) 
        {
            keyLines.add(line.trim());
        }

        List<String> valueLines = new ArrayList<>();
        while ((line = valueReader.readLine()) != null) 
        {
            valueLines.add(line.trim());
        }

        keyReader.close();
        valueReader.close();

        if (keyLines.size() != valueLines.size())
        {
            throw new Exception("Line count mismatch");
        }
        
        for (int i = 0; i < keyLines.size(); ++i)
        {
            String key = keyLines.get(i);
            String value = valueLines.get(i);
            internationalization.add(key, value);
        }

        return internationalization;
    }
}
