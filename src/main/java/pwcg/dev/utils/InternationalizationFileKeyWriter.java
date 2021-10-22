package pwcg.dev.utils;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.io.json.InternationalizationRecordsIO;
import pwcg.core.config.InternationalizationRecords;
import pwcg.core.exception.PWCGException;

public class InternationalizationFileKeyWriter
{
    public static void main(String[] args)
    {
        try
        {
            PWCGContext.setProduct(PWCGProduct.BOS);
            InternationalizationFileKeyWriter internationalizationFileBuilder = new InternationalizationFileKeyWriter();
            internationalizationFileBuilder.writeKeys();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void writeKeys() throws Exception 
    {
        String filename = "International.En.json";
        InternationalizationRecords existingInternationalizationRecord = buildExistingList(filename);

        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("International.Keys.txt")));       
        for (String key : existingInternationalizationRecord.getTranslations().keySet())
        {
            System.out.println(key);
            out.write(key);
            out.newLine();
        }
        out.close();
    }
    
    private InternationalizationRecords buildExistingList(String internationalizationFile) throws PWCGException
    {
        InternationalizationRecords internationalizationRecords = InternationalizationRecordsIO.readJson(internationalizationFile);
        return internationalizationRecords;
    }
}
