package pwcg.aar.ui.events;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.newspapers.Newspaper;
import pwcg.core.exception.PWCGException;

public class NewspaperEventGenerator
{
    private Campaign campaign;
    private Date newDate;

    public NewspaperEventGenerator (Campaign campaign, Date newDate)
    {
        this.campaign = campaign;
        this.newDate = newDate;
    }

    public List<Newspaper> createNewspaperEventsForElapsedTime() throws PWCGException
    {
        List<Newspaper> newspaperEventList = new ArrayList<>();

        for (Newspaper newspaper : PWCGContext.getInstance().getNewspaperManager().getNewspapersForSide(campaign.getReferencePlayer().getSide()))
        {
            if (isNewspaperWithinDateBoundaries(newspaper.getNewspaperEventDate()))
            {
                newspaperEventList.add(newspaper);
            }
        }
        return newspaperEventList;
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

    private Date getOldNewsDate()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(newDate);
        calendar.add(Calendar.MONTH, -1);
        Date oldNewsDate = calendar.getTime();
        return oldNewsDate;
    }
}
