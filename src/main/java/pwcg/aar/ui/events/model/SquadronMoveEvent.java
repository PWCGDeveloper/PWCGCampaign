package pwcg.aar.ui.events.model;

import java.util.Date;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.squadron.Squadron;

public class SquadronMoveEvent  extends AAREvent
{
    private IAirfield newAirfield = null;
    private IAirfield lastAirfield = null;
    private String status = "";
    private Squadron squadron = null;
    private boolean needsFerryMission = false;

    public SquadronMoveEvent ()
    {
    }

    public Squadron getSquadron()
    {
        return this.squadron;
    }

    public void setSquadron(Squadron squadron)
    {
        this.squadron = squadron;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public IAirfield getNewAirfield()
    {
        return this.newAirfield;
    }

    public void setNewAirfield(IAirfield newAirfield)
    {
        this.newAirfield = newAirfield;
    }

    public IAirfield getLastAirfield()
    {
        return this.lastAirfield;
    }

    public void setLastAirfield(IAirfield lastAirfield)
    {
        this.lastAirfield = lastAirfield;
    }

    public boolean isNeedsFerryMission()
    {
        return this.needsFerryMission;
    }

    public void setNeedsFerryMission(boolean needsFerryMission)
    {
        this.needsFerryMission = needsFerryMission;
    }

    @Override
    public Date getDate()
    {
        return super.getDate();
    }

    @Override
    public void setDate(Date date)
    {
        super.setDate(date);
    }

    @Override
    public boolean isNewsWorthy()
    {
        return super.isNewsWorthy();
    }

    @Override
    public void setNewsWorthy(boolean isNewsWorthy)
    {
        super.setNewsWorthy(isNewsWorthy);
    }
}
