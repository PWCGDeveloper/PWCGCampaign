package pwcg.aar.prelim;

import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.core.exception.PWCGException;
import pwcg.mission.data.MissionHeader;
import pwcg.mission.data.PwcgGeneratedMissionVehicleData;

public class PwcgMissionData 
{
    private MissionHeader missionHeader = new MissionHeader();
    private String missionDescription = "";
	private Map<Integer, PwcgGeneratedMissionVehicleData> missionPlanes  = new HashMap<>();

    public PwcgMissionData ()
    {
    }

	public MissionHeader getMissionHeader()
    {
        return missionHeader;
    }

    public void setMissionHeader(MissionHeader missionHeader)
    {
        this.missionHeader = missionHeader;
    }

    public  Map<Integer, PwcgGeneratedMissionVehicleData> getMissionPlanes()
    {
        return missionPlanes;
    }

    public  PwcgGeneratedMissionVehicleData getMissionPlane(Integer crewMemberSerialNumber)
    {
        return missionPlanes.get(crewMemberSerialNumber);
    }

    public void addMissionPlanes(PwcgGeneratedMissionVehicleData  missionPlane) throws PWCGException
    {
        missionPlanes.put(missionPlane.getCrewMemberSerialNumber(), missionPlane);
    }

    public String getMissionDescription()
    {
        return missionDescription;
    }

    public void setMissionDescription(String missionDescription)
    {
        this.missionDescription = missionDescription;
    }

    public void setMissionPlanes(Map<Integer, PwcgGeneratedMissionVehicleData> missionPlanes)
    {
        this.missionPlanes = missionPlanes;
    }
    
    public FrontMapIdentifier getMapId()
    {
        String mapName = missionHeader.getMapName();
        FrontMapIdentifier mapId = FrontMapIdentifier.getFrontMapIdentifierForName(mapName);
        return mapId;
    }
 }
