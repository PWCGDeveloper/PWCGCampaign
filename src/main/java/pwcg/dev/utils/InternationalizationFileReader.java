package pwcg.dev.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class InternationalizationFileReader
{
    public static void main(String[] args)
    {
        try
        {
            InternationalizationFileReader internationalizationFileBuilder = new InternationalizationFileReader();
            internationalizationFileBuilder.readAndWrite();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void readAndWrite() throws Exception 
    {
        String hardCoded = "Aktivität";
        
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("International.De.txt"), StandardCharsets.UTF_8));     
        String fromFile = in.readLine();
        in.close();

        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("International.test.txt"), StandardCharsets.UTF_8));       
        out.append(hardCoded);
        out.append("\n");
        out.append(fromFile);
        out.close();
    }
}
