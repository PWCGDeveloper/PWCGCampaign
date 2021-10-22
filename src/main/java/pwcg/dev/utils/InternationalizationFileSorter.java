package pwcg.dev.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;

public class InternationalizationFileSorter
{
    public static void main(String[] args)
    {
        try
        {
            PWCGContext.setProduct(PWCGProduct.BOS);
            InternationalizationFileSorter sorter = new InternationalizationFileSorter();
            sorter.sortEnglishRecords();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void sortEnglishRecords() throws Exception 
    {        
        BufferedReader valueReader = new BufferedReader(new FileReader("InternationalizationAnalysis.txt"));
        String line;

        List<String> valueLines = new ArrayList<>();
        while ((line = valueReader.readLine()) != null) 
        {
            valueLines.add(line.trim());
        }
        valueReader.close();

        Collections.sort(valueLines);
        
        BufferedWriter valueWriter = new BufferedWriter(new FileWriter("InternationalizationAnalysis.sorted.txt"));
        for (String outputLine : valueLines)
        {
            valueWriter.write(outputLine); 
            valueWriter.newLine();
        }
        valueWriter.close();
    }
}
