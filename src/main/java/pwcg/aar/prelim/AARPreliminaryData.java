package pwcg.aar.prelim;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pwcg.aar.inmission.phase1.parse.AARMissionLogFileSet;
import pwcg.aar.prelim.claims.AARClaimPanelData;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class AARPreliminaryData
{
    private AARMissionLogFileSet missionLogFileSet = new AARMissionLogFileSet();
    private AARClaimPanelData claimPanelData = new AARClaimPanelData();
    private PwcgMissionData pwcgMissionData = new PwcgMissionData();
    private SquadronMembers campaignMembersInMission = new SquadronMembers();

    public AARMissionLogFileSet getMissionLogFileSet()
	{
		return missionLogFileSet;
	}

	public void setMissionLogFileSet(AARMissionLogFileSet missionLogFileSet)
	{
		this.missionLogFileSet = missionLogFileSet;
	}

	public AARClaimPanelData getClaimPanelData()
    {
        return claimPanelData;
    }
    
    public void setClaimPanelData(AARClaimPanelData claimPanelData)
    {
        this.claimPanelData = claimPanelData;
    }

    public PwcgMissionData getPwcgMissionData()
    {
        return pwcgMissionData;
    }

    public void setPwcgMissionData(PwcgMissionData pwcgMissionData)
    {
        this.pwcgMissionData = pwcgMissionData;
    }

    public SquadronMembers getCampaignMembersInMission()
    {
        return campaignMembersInMission;
    }

    public void setCampaignMembersInMission(SquadronMembers campaignMembersInMission)
    {
        this.campaignMembersInMission = campaignMembersInMission;
    }
    
    public  List<Squadron>getPlayerSquadronsInMission() throws PWCGException
    {
        Set<Integer> uniqueSquadronsInMission = new HashSet<>();
        for (SquadronMember campaignMemberInMission : campaignMembersInMission.getSquadronMemberList())
        {
            if (campaignMemberInMission.isPlayer())
            {
                uniqueSquadronsInMission.add(campaignMemberInMission.getSquadronId());
            }
        }
        
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        List<Squadron> playerSquadronsInMission = new ArrayList<>();
        for (Integer squadronId : uniqueSquadronsInMission)
        {
            Squadron squadron = squadronManager.getSquadron(squadronId);
            playerSquadronsInMission.add(squadron);
        }
        return playerSquadronsInMission;
    }
}
