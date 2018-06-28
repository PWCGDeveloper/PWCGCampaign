package pwcg.aar.ui.events;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pwcg.aar.ui.events.model.NewspaperEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.DirectoryReader;
import pwcg.gui.utils.ContextSpecificImages;

public class NewspaperEventGenerator
{
    private Campaign campaign;
    private Date newDate;

    public NewspaperEventGenerator (Campaign campaign, Date newDate)
    {
        this.campaign = campaign;
        this.newDate = newDate;
    }


    public List<NewspaperEvent> createNewspaperEventsForElapsedTime() throws PWCGException
    {
        List<NewspaperEvent> newspaperEventList = new ArrayList<>();
        
        String newspaperDir = ContextSpecificImages.imagesNewspaper();
        List<String> dateBasedNewspaperFiles =  getDateBasedNewspaperFiles(newspaperDir);
        
        for (String filename : dateBasedNewspaperFiles)
        {
            if (!isNewspaperForOurSide(filename))
            {
                continue;
            }

            Date newsPaperDate = getNewspaperDate(filename);
            if (!isNewspaperWithinDateBoundaries(newsPaperDate))
            {
                continue;
            }

            NewspaperEvent newspaperEvent = makeNewspaperEvent(newspaperDir, filename, newsPaperDate);
            newspaperEventList.add(newspaperEvent);
        }
        return newspaperEventList;
    }

    private List<String> getDateBasedNewspaperFiles(String newspaperDir) throws PWCGException
    {
        DirectoryReader directoryReader = new DirectoryReader();
        directoryReader.sortilesInDir(newspaperDir);
        List<String> newspaperFiles = directoryReader.getFiles();
        
        List<String> dateBasedNewspaperFiles = new ArrayList<>();
        for (String filename : newspaperFiles)
        {
            if (filename.startsWith("19"))
            {
                dateBasedNewspaperFiles.add(filename);
            }
        }
        
        return dateBasedNewspaperFiles;
    }
    
    private boolean isNewspaperForOurSide (String filename) throws PWCGException
    {
        if ((campaign.determineCountry().getSide() == Side.ALLIED && filename.contains("Allied")) ||
            (campaign.determineCountry().getSide() == Side.AXIS && filename.contains("German")))
        {
            return true;
        }
        
        return false;
    }

    private boolean isNewspaperWithinDateBoundaries (Date newsPaperDate)
    {
        if (newsPaperDate.after(campaign.getDate()) && !newsPaperDate.after(newDate))
        {
            Date oldNewsDate = getOldNewsDate();
            if (!newsPaperDate.before(oldNewsDate))
            {
                return true;
            }
        }
        
        return false;
    }
    
    private Date getNewspaperDate(String filename) throws PWCGException
    {
        String dateString = filename.substring(0, 8);
        Date newsPaperDate = DateUtils.getDateYYYYMMDD(dateString);
        return newsPaperDate;
    }

    private Date getOldNewsDate()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(newDate);
        calendar.add(Calendar.MONTH, -1);
        Date oldNewsDate = calendar.getTime();
        return oldNewsDate;
    }

    private NewspaperEvent makeNewspaperEvent(String newspaperDir, String filename, Date newsPaperDate)
    {
        NewspaperEvent newspaperEvent = new NewspaperEvent();
        newspaperEvent.setDate(newsPaperDate);
        newspaperEvent.setNewspaperFile(newspaperDir + "\\" + filename);
        return newspaperEvent;
    }

}
