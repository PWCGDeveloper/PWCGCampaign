package pwcg.mission.data;

import java.util.List;

import pwcg.aar.prelim.PwcgMissionData;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;

public class PwcgGeneratedMission 
{
    private Campaign campaign;
    private PwcgMissionData pwcgMissionData;
	
	public PwcgGeneratedMission (Campaign campaign)
	{
	    this.campaign = campaign;
	    pwcgMissionData = new PwcgMissionData();
	}

	public PwcgMissionData generateMissionData(Mission mission) throws PWCGException
	{
	    MissionHeaderGenerator missionHeaderGenerator = new MissionHeaderGenerator();
	    MissionHeader missionHeader = missionHeaderGenerator.generateMissionHeader(campaign, mission);
	    pwcgMissionData.setMissionHeader(missionHeader);
        
	    MissionPlaneGenerator missionPlaneGenerator = new MissionPlaneGenerator(mission);
	    List<PwcgGeneratedMissionVehicleData> missionPlanes  = missionPlaneGenerator.generateMissionPlaneData();
	    for (PwcgGeneratedMissionVehicleData missionPlane : missionPlanes)
	    {
	        pwcgMissionData.addMissionPlanes(missionPlane);
	    }
	    
        return pwcgMissionData;
	}
 }
