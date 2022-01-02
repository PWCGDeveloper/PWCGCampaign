package pwcg.aar.ui.events.model;

import java.util.Date;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadron.Company;
import pwcg.campaign.squadron.SquadronManager;
import pwcg.core.exception.PWCGException;

public class SquadronMoveEvent  extends AAREvent
{
    private String newAirfield;
    private String lastAirfield;
    private int squadronId;
    private boolean needsFerryMission;
    private String squadronName = "";

    
    public SquadronMoveEvent(String lastAirfield, String newAirfield, int squadronId, boolean needsFerryMission, Date date, boolean isNewsWorthy)
    {
        super(date, isNewsWorthy);
        this.lastAirfield = lastAirfield;
        this.newAirfield = newAirfield;
        this.squadronId = squadronId;
        this.needsFerryMission = needsFerryMission;
        
        try
        {
            SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
            Company squadron = squadronManager.getSquadron(squadronId);
            if (squadron != null)
            {
                this.squadronName = squadron.determineDisplayName(date);
            }
        }
        catch (PWCGException e)
        {
            this.squadronName = "";
            e.printStackTrace();
        }
    }

    public String getNewAirfield()
    {
        return newAirfield;
    }

    public String getLastAirfield()
    {
        return lastAirfield;
    }

    public int getSquadronId()
    {
        return squadronId;
    }

    public boolean isNeedsFerryMission()
    {
        return needsFerryMission;
    }

    public String getSquadronName()
    {
        return squadronName;
    }
}
