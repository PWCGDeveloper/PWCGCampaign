package pwcg.aar.prelim;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.mission.data.PwcgGeneratedMissionPlaneData;

public class CampaignMembersInMissionFinder
{
    public SquadronMembers determineCampaignMembersInMission(Campaign campaign, PwcgMissionData pwcgMissionData) throws PWCGException
    {        
        SquadronMembers campaignMembersInMission = new SquadronMembers();
        for (PwcgGeneratedMissionPlaneData missionPlane : pwcgMissionData.getMissionPlanes().values())
        {
    		SquadronMember pilot = campaign.getPersonnelManager().getAnyCampaignMember(missionPlane.getPilotSerialNumber());
    		if (pilot != null)
    		{
    	        campaignMembersInMission.addSquadronMember(pilot);
    		}
         }
                        
        return campaignMembersInMission;
    }

}
