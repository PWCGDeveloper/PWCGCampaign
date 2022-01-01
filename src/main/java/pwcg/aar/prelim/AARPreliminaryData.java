package pwcg.aar.prelim;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pwcg.aar.prelim.claims.AARClaimPanelData;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.squadron.SquadronManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogFileSet;

public class AARPreliminaryData
{
    private LogFileSet missionLogFileSet = new LogFileSet();
    private AARClaimPanelData claimPanelData = new AARClaimPanelData();
    private PwcgMissionData pwcgMissionData = new PwcgMissionData();
    private SquadronMembers campaignMembersInMission = new SquadronMembers();

    public LogFileSet getMissionLogFileSet()
	{
		return missionLogFileSet;
	}

	public void setMissionLogFileSet(LogFileSet missionLogFileSet)
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
