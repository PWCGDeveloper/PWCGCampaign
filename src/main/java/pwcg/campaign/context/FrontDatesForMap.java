package pwcg.campaign.context;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateRange;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.FileUtils;

public class FrontDatesForMap
{
    private List<Date> frontDates = new ArrayList<Date>();
    private List<DateRange> mapActiveDates = new ArrayList<>();

    public void addFrontDate(String dateString) throws PWCGException
    {
        Date subDirDate = DateUtils.getDateYYYYMMDD(dateString);
        frontDates.add(subDirDate);
    }

    public void cleanUnwantedDateDirectories(String mapName) throws PWCGException
    {
        String frontPath = PWCGContextManager.getInstance().getDirectoryManager().getPwcgInputDir() + mapName;
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
                        FileUtils fileUtils = new FileUtils();
                        fileUtils.deleteRecursive(subDir.getAbsolutePath());
                    }
                }
            }
        }

        if (frontDates.isEmpty())
        {
            throw new PWCGException("No date directories in: " + frontPath);
        }
    }

    private boolean findDateInList(String searchFor)
    {
        for (Date mapDate : frontDates)
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
        boolean useMovingFront = PWCGContextManager.getInstance().determineUseMovingFront();

        if (!useMovingFront)
        {
            return frontDates.get(0);
        }

        Date closestFrontDate = null;
        closestFrontDate = frontDates.get(0);
        for (Date frontDate : frontDates)
        {
            if (frontDate.after(date))
            {
                break;
            }

            closestFrontDate = frontDate;
        }

        if (closestFrontDate == null)
        {
            throw new PWCGException("No date directories for date: " + date);
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
        return frontDates;
    }

}
