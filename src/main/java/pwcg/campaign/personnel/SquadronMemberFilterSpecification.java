package pwcg.campaign.personnel;

import java.util.Date;

public class SquadronMemberFilterSpecification
{
    public static final int NO_SQUADRON_FILTER = 0;
    
    private Date date;
    private boolean includeInactive = false;
    private boolean includeActive = true;
    private boolean includeAces = true;
    private boolean includePlayer = true;
    private int specifySquadron = NO_SQUADRON_FILTER;

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public boolean isIncludeAces()
    {
        return includeAces;
    }

    public void setIncludeAces(boolean includeAces)
    {
        this.includeAces = includeAces;
    }

    public boolean isIncludePlayer()
    {
        return includePlayer;
    }

    public void setIncludePlayer(boolean includePlayer)
    {
        this.includePlayer = includePlayer;
    }

    public boolean isIncludeInactive()
    {
        return includeInactive;
    }

    public void setIncludeInactive(boolean includeInactive)
    {
        this.includeInactive = includeInactive;
    }

    public boolean isIncludeActive()
    {
        return includeActive;
    }

    public void setIncludeActive(boolean includeActive)
    {
        this.includeActive = includeActive;
    }

    public int getSpecifySquadron()
    {
        return specifySquadron;
    }

    public void setSpecifySquadron(int specifySquadron)
    {
        this.specifySquadron = specifySquadron;
    }
}
