package pwcg.dev.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.io.json.InternationalizationRecordsIO;
import pwcg.core.config.InternationalizationRecords;
import pwcg.core.exception.PWCGException;

public class InternationalizationFileBuilder
{
    public static void main(String[] args)
    {
        try
        {
            PWCGContext.setProduct(PWCGProduct.BOS);
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
        String filename = "International.Ru.json";
                
        Map<String, String> internationalizationRecordMap = new TreeMap<>();
        Map<String, String> newInternationalizationRecordMap = readLogFile();
        Map<String, String> existingInternationalizationRecordMap = buildExistingList(filename);
        internationalizationRecordMap.putAll(existingInternationalizationRecordMap);
        internationalizationRecordMap.putAll(newInternationalizationRecordMap);
        
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)));       
        for (String key : internationalizationRecordMap.keySet())
        {
            String value = internationalizationRecordMap.get(key);
            String line="    \"" + key + "\": \"" + value + "\"," + "\n";
            System.out.println(line);
            out.append(line);
        }
        out.close();
    }
    
    private Map<String, String> buildExistingList(String internationalizationFile) throws PWCGException
    {
        InternationalizationRecords internationalizationRecords = InternationalizationRecordsIO.readJson(internationalizationFile);
        return internationalizationRecords.getTranslations();
    }

    private Map<String, String> readLogFile() throws Exception
    {
        Map<String, String> internationalizationMap = new TreeMap<>();
        
        BufferedReader keyReader = new BufferedReader(new FileReader("InternationalizationAnalysis.sorted.txt"));        
        BufferedReader valueReader  = new BufferedReader(new InputStreamReader(new FileInputStream("InternationalizationAnalysis.translated.txt"), "UTF-8"));

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
            internationalizationMap.put(key, value);
        }

        return internationalizationMap;
    }
}
