package pwcg.aar.ui.events.model;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.squadron.SquadronManager;

public class AARPilotEvent extends AAREvent
{
    public static final int ALL_SQUADRONS = -1;

    private Integer squadronId = ALL_SQUADRONS;
    private int pilotSerialNumber = 0;
    private String squadronName = "";
    private String pilotName = "";

    public AARPilotEvent(Campaign campaign, int squadronId, int pilotSerialNumber, Date date, boolean isNewsWorthy)
    {
        super(date, isNewsWorthy);
        this.pilotSerialNumber = pilotSerialNumber;
        this.squadronId = squadronId;
        
        try
        {
            SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
            Squadron squadron = squadronManager.getSquadron(squadronId);
            if (squadron != null)
            {
                this.squadronName = squadron.determineDisplayName(date);
            }
            
            SquadronMember pilot = campaign.getPersonnelManager().getAnyCampaignMember(pilotSerialNumber);
            if (pilot != null)
            {
                this.pilotName = pilot.getNameAndRank();
            }
        }
        catch (Exception e)
        {
            this.squadronName = "";
            e.printStackTrace();
        }
    }

    public Integer getSquadronId()
    {
        return squadronId;
    }

    public int getPilotSerialNumber()
    {
        return pilotSerialNumber;
    }

    public String getSquadronName()
    {
        return squadronName;
    }

    public String getPilotName()
    {
        return pilotName;
    }
}
