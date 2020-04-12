package pwcg.campaign.context;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateRange;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.FileUtils;

public class FrontDatesForMap
{
    private FrontMapIdentifier frontMapIdentifier;
    private Map<String, Date> frontDates = new TreeMap<>();
    private List<DateRange> mapActiveDates = new ArrayList<>();

    public FrontDatesForMap(FrontMapIdentifier frontMapIdentifier)
    {
        this.frontMapIdentifier = frontMapIdentifier;
    }
    
    public void addFrontDate(String dateString) throws PWCGException
    {
        Date subDirDate = DateUtils.getDateYYYYMMDD(dateString);
        frontDates.put(dateString, subDirDate);
    }

    public void cleanUnwantedDateDirectories(String mapName) throws PWCGException
    {
        String frontPath = PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName;
        File frontPathHandle = new File(frontPath);
        if (!frontPathHandle.exists())
        {
            throw new PWCGException("Directory does not exist: " + frontPath);
        }

        for (File subDir : frontPathHandle.listFiles())
        {
            if (subDir.isDirectory())
            {
                if (subDir.getName().startsWith("19"))
                {
                    if (!findDateInList(subDir.getName()))
                    {
                        FileUtils.deleteRecursive(subDir.getAbsolutePath());
                    }
                }
            }
        }

        if (frontDates.isEmpty())
        {
            throw new PWCGException("No date directories in: " + frontPath + " for map " + frontMapIdentifier);
        }
    }

    private boolean findDateInList(String searchFor)
    {
        for (Date mapDate : frontDates.values())
        {
            String mapDateStr = DateUtils.getDateStringYYYYMMDD(mapDate);
            if (mapDateStr.trim().contains(searchFor))
            {
                
                return true;
            }
        }
        return false;
    }

    public Date getFrontDateForDate(Date date) throws PWCGException
    {
        Date closestFrontDate = null;
        for (String frontDateString : frontDates.keySet())
        {
            Date frontDate = frontDates.get(frontDateString);
            if (date.after(frontDate) || date.equals(frontDate))
            {
                closestFrontDate = frontDate;
            }    
        }
        
        if (closestFrontDate == null)
        {
            List<Date> orderedDateList = new ArrayList<>(frontDates.values());
            closestFrontDate = orderedDateList.get(0);
        }

        return closestFrontDate;
    }

    public boolean isMapActive(Date date)
    {
        for (DateRange dateRange : mapActiveDates)
        {
            if (dateRange.isInDateRange(date))
            {
                return true;
            }
        }
        return false;
    }

    public Date getEarliestMapDate() throws PWCGException
    {
        Date earliestMapDate = null;
        for (DateRange dateRange : mapActiveDates)
        {
            if (earliestMapDate == null)
            {
                earliestMapDate = dateRange.getStartDate();
            }
            else
            {
                Date startDate = dateRange.getStartDate();
                if (startDate.before(earliestMapDate))
                {
                    earliestMapDate = startDate;
                }
            }
        }

        return earliestMapDate;
    }

    public void addMapDateRange(Date startDate, Date endDate)
    {
        DateRange mapDateRange = new DateRange(startDate, endDate);
        mapActiveDates.add(mapDateRange);
    }

    public Date getLatestMapDate() throws PWCGException
    {
        Date latestMapDate = null;
        for (DateRange dateRange : mapActiveDates)
        {
            if (latestMapDate == null)
            {
                latestMapDate = dateRange.getEndDate();
            }
            else
            {
                Date endDate = dateRange.getEndDate();
                if (endDate.before(latestMapDate))
                {
                    latestMapDate = endDate;
                }
            }
        }

        return latestMapDate;
    }

    public List<Date> getFrontDates()
    {
        List<Date> orderedDateList = new ArrayList<>(frontDates.values());
        return orderedDateList;
    }

}
