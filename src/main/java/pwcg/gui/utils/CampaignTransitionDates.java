package pwcg.gui.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGDirectoryManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.DirectoryReader;

public class CampaignTransitionDates
{
    private DirectoryReader directoryReader = new DirectoryReader();

    public List<String> getCampaignTransitionDates() throws PWCGException, PWCGException
    {
         Map<Date, Date> sortedDates = new TreeMap<Date, Date>();

        Date mapStart = PWCGContextManager.getInstance().getCurrentMap().getFrontDatesForMap().getEarliestMapDate();
        sortedDates.put(mapStart, mapStart);

        Date mapEnd = PWCGContextManager.getInstance().getCurrentMap().getFrontDatesForMap().getLatestMapDate();
        sortedDates.put(mapEnd, mapEnd);
        
        addMovingFrontDates(sortedDates);
        
        List<String> campaignTransitionDates = createNewStartDates(sortedDates, mapStart, mapEnd);
        
        return campaignTransitionDates;
    }


    private List<String> createNewStartDates(Map<Date, Date> sortedDates, Date mapStart, Date mapEnd)
    {
        List<String> newDates = new ArrayList<String>();
        for (Date date : sortedDates.values()) 
        {
            if (!date.before(mapStart) && !date.after(mapEnd))
            {
                String dateString = DateUtils.getDateStringYYYYMMDD(date);
                newDates.add(dateStringFromDirString(dateString));
            }
        }
        
        return newDates;
    }

    private String dateStringFromDirString(String dirString)
    {
        String formattedDate = dirString.substring(6, 8) + "/" + dirString.substring(4, 6) + "/" + dirString.substring(0, 4);

        return formattedDate;
    }

    private void addMovingFrontDates(Map<Date, Date> sortedDates) throws PWCGException
    {
        String mapPath = PWCGDirectoryManager.getInstance().getPwcgInputDir() + PWCGContextManager.getInstance().getCurrentMap().getMapName();      
        directoryReader.sortilesInDir(mapPath);
        for (String filename : directoryReader.getDirectories()) 
        {
            String dirPath = PWCGDirectoryManager.getInstance().getPwcgInputDir() + filename;
            File child = new File(dirPath);
            if (child.getName().contains("19"))
            {
                String dateStr = child.getName();
                Date date = DateUtils.getDateYYYYMMDD(dateStr);
                sortedDates.put(date, date);
            }
        }
    }


}
