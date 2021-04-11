package pwcg.campaign.battle;

import java.util.Date;

public class NoBattlePeriod
{
    private Date startNoBattlePeriod;
    private Date endNoBattlePeriod;

    public NoBattlePeriod(Date startNoBattlePeriod, Date endNoBattlePeriod)
    {
        this.startNoBattlePeriod = startNoBattlePeriod;
        this.endNoBattlePeriod = endNoBattlePeriod;
    }
    
    public Date getStartNoBattlePeriod()
    {
        return startNoBattlePeriod;
    }

    public Date getEndNoBattlePeriod()
    {
        return endNoBattlePeriod;
    }

}
