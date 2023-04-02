package pwcg.dev.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class FindTheObject
{
    private List<String> groups = new ArrayList<>();
    private List<String> objectsFromMission = new ArrayList<>();
    
    static public void main (String[] args)
    {
        try
        {
            FindTheObject finder = new FindTheObject();
            finder.findObjects();
        }
        catch (Exception e)
        {
             PWCGLogger.logException(e);;
        }
    }
    
    public void findObjects() throws Exception 
    {
        read("D:\\PWCG\\Bugs\\Objects\\WesternFront_ALL.Group", groups);
        read("D:\\PWCG\\Bugs\\Objects\\UniqueObjects.txt", objectsFromMission);
        
        for (String objectFromMission : objectsFromMission)
        {
            boolean found = false;
            objectFromMission += ".txt";
            for (String group : groups)
            {
                if (group.contains(objectFromMission))
                {
                    found = true;
                    break;
                }
            }
            
            if (!found)
            {
                PWCGLogger.log(LogLevel.DEBUG, "Bad object is " + objectFromMission);
            }
        }
    }

    private void read(String filename, List<String>list) throws Exception 
    {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) 
        {
            String name = line.trim();
            if (name != null && name.length() != 0)
            {
                list.add(name);
            }
        }
        
        reader.close();
    }

}
