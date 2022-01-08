package pwcg.aar.ui.events.model;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignEquipmentManager;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.tank.EquippedTank;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class PlaneStatusEvent extends AARCrewMemberEvent
{
    private int crewMemberSerialNumber;
    private int planeSerialNumber;
    private String squadronName;
	private int planeStatus;
	
    public PlaneStatusEvent(Campaign campaign, LogPlane lostPlane, int planeStatus, boolean isNewsWorthy)
    {
        super(campaign, lostPlane.getSquadronId(), lostPlane.getCrewMemberSerialNumber(), campaign.getDate(), isNewsWorthy);
        this.crewMemberSerialNumber = lostPlane.getCrewMemberSerialNumber();
        this.planeSerialNumber = lostPlane.getPlaneSerialNumber();
        this.planeStatus = planeStatus;
        
        try
        {
            CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
            Company squadron = squadronManager.getCompany(super.getSquadronId());
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
        EquippedTank shotDownPlane = campaignEquipmentManager.destroyTank(planeSerialNumber, campaign.getDate());

        CampaignPersonnelManager campaignPersonnelManager = campaign.getPersonnelManager();
        CrewMember shotDownCrewMember = campaignPersonnelManager.getAnyCampaignMember(super.getCrewMemberSerialNumber());

        String prettyDate = DateUtils.getDateStringPretty(campaign.getDate());
        String planeEventText = 
                "A " + shotDownPlane.getDisplayName() +
                ",  serial number " + planeSerialNumber + 
                ",  flown by " + shotDownCrewMember.getNameAndRank() + 
                " has been lost in combat on " + prettyDate + ".\n";    ;                

        return planeEventText;
    }

    public String getPlaneAddedToDepotText(Campaign campaign) throws PWCGException
    {
        CampaignEquipmentManager campaignEquipmentManager = campaign.getEquipmentManager();
        EquippedTank shotDownPlane = campaignEquipmentManager.destroyTank(planeSerialNumber, campaign.getDate());

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
        EquippedTank shotDownPlane = campaignEquipmentManager.destroyTank(planeSerialNumber, campaign.getDate());

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
    
    public int getCrewMemberSerialNumber()
    {
        return crewMemberSerialNumber;
    }

    public String getSquadronName()
    {
        return squadronName;
    }
}
