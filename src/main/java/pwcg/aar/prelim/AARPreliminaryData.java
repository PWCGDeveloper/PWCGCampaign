package pwcg.aar.prelim;

import pwcg.aar.inmission.phase1.parse.AARMissionLogFileSet;
import pwcg.campaign.squadmember.SquadronMembers;

public class AARPreliminaryData
{
    private AARMissionLogFileSet missionLogFileSet = new AARMissionLogFileSet();
    private AARClaimPanelData claimPanelData = new AARClaimPanelData();
    private PwcgMissionData pwcgMissionData = new PwcgMissionData();
    private SquadronMembers campaignMembersInMission = new SquadronMembers();
    private SquadronMembers campaignMembersOutOfMission = new SquadronMembers();

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

    public SquadronMembers getCampaignMembersOutOfMission()
    {
        return campaignMembersOutOfMission;
    }

    public void setCampaignMembersOutOfMission(SquadronMembers campaignMembersOutOfMission)
    {
        this.campaignMembersOutOfMission = campaignMembersOutOfMission;
    }
}
