package pwcg.gui.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.DirectoryReader;

public class CampaignTransitionDates
{
    private DirectoryReader directoryReader = new DirectoryReader();
    private FrontMapIdentifier mapIdentifier;
    
    public CampaignTransitionDates(FrontMapIdentifier mapIdentifier)
    {
        this.mapIdentifier = mapIdentifier;
    }

    public List<String> getCampaignTransitionDateStrings() throws PWCGException, PWCGException
    {
         Map<Date, Date> sortedDates = new TreeMap<Date, Date>();

        Date mapStart = PWCGContext.getInstance().getMap(mapIdentifier).getFrontDatesForMap().getEarliestMapDate();
        sortedDates.put(mapStart, mapStart);

        Date mapEnd = PWCGContext.getInstance().getMap(mapIdentifier).getFrontDatesForMap().getLatestMapDate();
        sortedDates.put(mapEnd, mapEnd);
        
        addMovingFrontDates(sortedDates);
        
        List<String> campaignTransitionDates = convertMapDatesToStrings(sortedDates, mapStart, mapEnd);
        
        return campaignTransitionDates;
    }

    public TreeMap<Date, Date> getCampaignTransitionDates() throws PWCGException, PWCGException
    {
        TreeMap<Date, Date> sortedDates = new TreeMap<Date, Date>();

        Date mapStart = PWCGContext.getInstance().getMap(mapIdentifier).getFrontDatesForMap().getEarliestMapDate();
        sortedDates.put(mapStart, mapStart);

        Date mapEnd = PWCGContext.getInstance().getMap(mapIdentifier).getFrontDatesForMap().getLatestMapDate();
        sortedDates.put(mapEnd, mapEnd);
        
        addMovingFrontDates(sortedDates);
                
        return sortedDates;
    }


    public List<String> convertMapDatesToStrings(Map<Date, Date> sortedDates, Date mapStart, Date mapEnd)
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
        String mapPath = PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + PWCGContext.getInstance().getMap(mapIdentifier).getMapName();      
        directoryReader.sortFilesInDir(mapPath);
        for (String filename : directoryReader.getDirectories()) 
        {
            String dirPath = PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + filename;
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
