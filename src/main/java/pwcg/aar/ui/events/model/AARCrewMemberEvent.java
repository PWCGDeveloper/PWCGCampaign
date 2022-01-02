package pwcg.aar.ui.events.model;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.squadron.Company;
import pwcg.campaign.squadron.SquadronManager;

public class AARCrewMemberEvent extends AAREvent
{
    public static final int ALL_SQUADRONS = -1;

    private Integer squadronId = ALL_SQUADRONS;
    private int crewMemberSerialNumber = 0;
    private String squadronName = "";
    private String crewMemberName = "";

    public AARCrewMemberEvent(Campaign campaign, int squadronId, int crewMemberSerialNumber, Date date, boolean isNewsWorthy)
    {
        super(date, isNewsWorthy);
        this.crewMemberSerialNumber = crewMemberSerialNumber;
        this.squadronId = squadronId;
        
        try
        {
            SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
            Company squadron = squadronManager.getSquadron(squadronId);
            if (squadron != null)
            {
                this.squadronName = squadron.determineDisplayName(date);
            }
            
            CrewMember crewMember = campaign.getPersonnelManager().getAnyCampaignMember(crewMemberSerialNumber);
            if (crewMember != null)
            {
                this.crewMemberName = crewMember.getNameAndRank();
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

    public int getCrewMemberSerialNumber()
    {
        return crewMemberSerialNumber;
    }

    public String getSquadronName()
    {
        return squadronName;
    }

    public String getCrewMemberName()
    {
        return crewMemberName;
    }
}
