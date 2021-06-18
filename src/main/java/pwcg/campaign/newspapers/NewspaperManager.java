package pwcg.campaign.newspapers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import pwcg.campaign.api.Side;
import pwcg.campaign.io.json.NewspaperIOJson;
import pwcg.core.exception.PWCGException;

public class NewspaperManager
{
    private TreeMap<Date, Newspaper> alliedNewspapers = new TreeMap<>();
    private TreeMap<Date, Newspaper> axisNewspapers = new TreeMap<>();

    public void initialize() throws PWCGException 
    {
        alliedNewspapers.clear();
        axisNewspapers.clear();
                
        Newspapers newspapers = NewspaperIOJson.readJson(); 
        for (Newspaper newspaper : newspapers.getNewspapers())
        {
            if (newspaper.getSide() == Side.ALLIED)
            {
                alliedNewspapers.put(newspaper.getNewspaperEventDate(), newspaper);
            }
            else
            {
                axisNewspapers.put(newspaper.getNewspaperEventDate(), newspaper);
            }
        }
    }
    
    public List<Newspaper> getNewpapersToDate(Side side, Date date)
    {
        List<Newspaper> newspaperToDate = new ArrayList<>();
        List<Newspaper> newspaperForSide = getNewspapersForSide(side);
        for (Newspaper newspaper : newspaperForSide)
        {
            if (!newspaper.getNewspaperEventDate().after(date))
            {
                newspaperToDate.add(newspaper);
            }
        }
        return newspaperToDate;
        
    }
    
    public Newspaper getNewpapersForDate(Side side, Date date)
    {
        List<Newspaper> newspaperForSide = getNewspapersForSide(side);
        for (Newspaper newspaper : newspaperForSide)
        {
            if (newspaper.getNewspaperEventDate().equals(date))
            {
                return newspaper;
            }
        }
        return null;
        
    }

    private List<Newspaper> getNewspapersForSide(Side side)
    {
        TreeMap<Date, Newspaper> newspapersForSide = axisNewspapers;
        if (side == Side.ALLIED)
        {
            newspapersForSide = alliedNewspapers;
        }
        return new ArrayList<Newspaper>(newspapersForSide.values());
    }
}
