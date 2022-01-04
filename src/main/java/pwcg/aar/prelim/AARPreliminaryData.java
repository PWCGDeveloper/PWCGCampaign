package pwcg.aar.prelim;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pwcg.aar.prelim.claims.AARClaimPanelData;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogFileSet;

public class AARPreliminaryData
{
    private LogFileSet missionLogFileSet = new LogFileSet();
    private AARClaimPanelData claimPanelData = new AARClaimPanelData();
    private PwcgMissionData pwcgMissionData = new PwcgMissionData();
    private CrewMembers campaignMembersInMission = new CrewMembers();

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

    public CrewMembers getCampaignMembersInMission()
    {
        return campaignMembersInMission;
    }

    public void setCampaignMembersInMission(CrewMembers campaignMembersInMission)
    {
        this.campaignMembersInMission = campaignMembersInMission;
    }
    
    public  List<Company>getPlayerSquadronsInMission() throws PWCGException
    {
        Set<Integer> uniqueSquadronsInMission = new HashSet<>();
        for (CrewMember campaignMemberInMission : campaignMembersInMission.getCrewMemberList())
        {
            if (campaignMemberInMission.isPlayer())
            {
                uniqueSquadronsInMission.add(campaignMemberInMission.getCompanyId());
            }
        }
        
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> playerSquadronsInMission = new ArrayList<>();
        for (Integer squadronId : uniqueSquadronsInMission)
        {
            Company squadron = squadronManager.getCompany(squadronId);
            playerSquadronsInMission.add(squadron);
        }
        return playerSquadronsInMission;
    }
}
