package pwcg.aar.ui.events.model;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignEquipmentManager;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class PlaneStatusEvent extends AARPilotEvent
{
    private int planeSerialNumber;
    private String squadronName;
	private int planeStatus;
	
    public PlaneStatusEvent(Campaign campaign, LogPlane lostPlane, int planeStatus, boolean isNewsWorthy)
    {
        super(campaign, lostPlane.getSquadronId(), lostPlane.getPilotSerialNumber(), campaign.getDate(), isNewsWorthy);
        this.planeSerialNumber = lostPlane.getPlaneSerialNumber();
        this.planeStatus = planeStatus;
        
        try
        {
            SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
            Squadron squadron = squadronManager.getSquadron(super.getSquadronId());
            if (squadron != null)
            {
                this.squadronName = squadron.determineDisplayName(campaign.getDate());
            }
        }
        catch (Exception e)
        {
            this.squadronName = "";
            e.printStackTrace();
        }
    }

    public String getPlaneLostText(Campaign campaign) throws PWCGException
    {
        CampaignEquipmentManager campaignEquipmentManager = campaign.getEquipmentManager();
        EquippedPlane shotDownPlane = campaignEquipmentManager.destroyPlane(planeSerialNumber, campaign.getDate());

        CampaignPersonnelManager campaignPersonnelManager = campaign.getPersonnelManager();
        SquadronMember shotDownPilot = campaignPersonnelManager.getAnyCampaignMember(super.getPilotSerialNumber());

        String prettyDate = DateUtils.getDateStringPretty(campaign.getDate());
        String planeEventText = 
                "A " + shotDownPlane.getDisplayName() +
                ",  serial number " + planeSerialNumber + 
                ",  flown by " + shotDownPilot.getNameAndRank() + 
                " has been lost in combat on " + prettyDate + ".\n";    ;                

        return planeEventText;
    }

    public String getPlaneAddedToDepotText(Campaign campaign) throws PWCGException
    {
        CampaignEquipmentManager campaignEquipmentManager = campaign.getEquipmentManager();
        EquippedPlane shotDownPlane = campaignEquipmentManager.destroyPlane(planeSerialNumber, campaign.getDate());

        String prettyDate = DateUtils.getDateStringPretty(campaign.getDate());
        String planeEventText = 
                "A " + shotDownPlane.getDisplayName() +
                ",  serial number " + planeSerialNumber + 
                " has been provided to the depot for distribution to front line units on " + prettyDate + ".\n";               

        return planeEventText;
    }

    public String getPlaneWithdrawnFromServiceText(Campaign campaign) throws PWCGException
    {
        CampaignEquipmentManager campaignEquipmentManager = campaign.getEquipmentManager();
        EquippedPlane shotDownPlane = campaignEquipmentManager.destroyPlane(planeSerialNumber, campaign.getDate());

        String prettyDate = DateUtils.getDateStringPretty(campaign.getDate());
        String planeEventText = 
                "A " + shotDownPlane.getDisplayName() +
                ",  serial number " + planeSerialNumber + 
                " has been withdrawn from service on " + prettyDate + ".\n";                

        return planeEventText;
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
}
