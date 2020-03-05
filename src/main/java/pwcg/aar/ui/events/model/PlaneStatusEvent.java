package pwcg.aar.ui.events.model;

import java.util.Date;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.squadron.Squadron;

public class PlaneStatusEvent extends AAREvent
{
    private int planeSerialNumber;
    private int squadronId;
    private String squadronName;
    private String planeDesc;
	private int planeStatus;
	
    public PlaneStatusEvent(int planeSerialNumber, String planeDesc, int squadronId, int planeStatus, Date date, boolean isNewsWorthy)
    {
        super(date, isNewsWorthy);
        this.planeSerialNumber = planeSerialNumber;
        this.planeDesc = planeDesc;
        this.squadronId = squadronId;
        this.planeStatus = planeStatus;
        
        try
        {
            SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
            Squadron squadron = squadronManager.getSquadron(squadronId);
            if (squadron != null)
            {
                this.squadronName = squadron.determineDisplayName(date);
            }
        }
        catch (Exception e)
        {
            this.squadronName = "";
            e.printStackTrace();
        }
    }

    public int getSquadronId()
    {
        return squadronId;
    }

    public int getPlaneStatus()
    {
        return planeStatus;
    }

    public int getPlaneSerialNumber()
    {
        return planeSerialNumber;
    }

    public String getSquadronName()
    {
        return squadronName;
    }

    public String getPlaneDesc()
    {
        return planeDesc;
    }
}
