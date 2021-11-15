package pwcg.dev.utils;

import java.io.BufferedReader;
import java.io.FileReader;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.io.json.InternationalizationRecordsIO;
import pwcg.core.config.InternationalizationRecords;

public class InternationalizationFileMerger
{
    public static void main(String[] args)
    {
        try
        {
            PWCGContext.setProduct(PWCGProduct.BOS);
            InternationalizationFileMerger internationalizationFileBuilder = new InternationalizationFileMerger();
            internationalizationFileBuilder.buildFileForMissingRecords();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void buildFileForMissingRecords() throws Exception 
    {
        InternationalizationRecords internationalizationRecords = readLogFile();
        InternationalizationRecordsIO.writeJson(internationalizationRecords, "International.New.json");
    }

    private InternationalizationRecords readLogFile() throws Exception
    {
        InternationalizationRecords internationalizationRecords = new InternationalizationRecords();
        BufferedReader reader = new BufferedReader(new FileReader("InternationalizationAnalysis.txt"));
        String line;

        while ((line = reader.readLine()) != null) 
        {
            String value = line.substring("English=".length());
            value = value.trim();
            if (!value.isEmpty())
            {
                internationalizationRecords.add(value, value);
            }
        }

        reader.close();
        return internationalizationRecords;
    }

}
