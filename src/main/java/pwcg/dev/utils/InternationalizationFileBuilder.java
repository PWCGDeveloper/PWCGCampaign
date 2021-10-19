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
        Map<String, String> internationalizationRecords = readLogFile();
        // BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("BoSData\\Input\\International\\International.Sp.json", true), StandardCharsets.UTF_8));
        
        //Writer fstream = new OutputStreamWriter(new FileOutputStream("BoSData\\Input\\International\\International.Sp.json", true), "8859_1");
        //Writer fstream = new OutputStreamWriter(new FileOutputStream("BoSData\\Input\\International\\International.Sp.json", true), "UTF-8");
        //Writer fstream = new OutputStreamWriter(new FileOutputStream("BoSData\\Input\\International\\International.Sp.json", true), StandardCharsets.UTF_8);

        Writer out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("BoSData\\Input\\International\\International.Ru.json"), "UTF-8"));
        
        for (String key : internationalizationRecords.keySet())
        {
            String value = internationalizationRecords.get(key);
            String line="    \"" + key + "\": \"" + value + "\"," + "\n";
            System.out.println(line);
            out.append(line);
        }
        out.close();
    }

    private Map<String, String> readLogFile() throws Exception
    {
        Map<String, String> internationalizationMap = new TreeMap<>();
        
        BufferedReader keyReader = new BufferedReader(new FileReader("BoSData\\Input\\International\\International.En.json"));        
        BufferedReader valueReader  = new BufferedReader(new InputStreamReader(new FileInputStream("BoSData\\Input\\International\\International.Ru.All.json"), "UTF-8"));

        String line;
        
        List<String> keyLines = new ArrayList<>();
        while ((line = keyReader.readLine()) != null) 
        {
            keyLines.add(line);
        }

        List<String> valueLines = new ArrayList<>();
        while ((line = valueReader.readLine()) != null) 
        {
            valueLines.add(line);
        }

        keyReader.close();
        valueReader.close();

        if (keyLines.size() != valueLines.size())
        {
            throw new Exception("Line mismatch");
        }
        
        String delimiter = "\": ";
        for (int i = 0; i < keyLines.size(); ++i)
        {
            String keyLine = keyLines.get(i);
            if (thisIsARecord(keyLine))
            {
                int endKey = keyLine.indexOf(delimiter);
                String key = keyLine.substring(0, endKey);
                key = key.trim();
                key = key.substring(1);
                
                String valueLine = valueLines.get(i);
                int endValue = valueLine.indexOf(delimiter);
                String value = valueLine.substring(0, endValue);
                value = value.trim();
                value = value.substring(1, value.length());

                internationalizationMap.put(key, value);
            }
        }

        return internationalizationMap;
    }

    private boolean thisIsARecord(String line)
    {
        if (line.contains("{") || line.contains("}"))
        {
            return false;
        }
        return true;
    }
}
