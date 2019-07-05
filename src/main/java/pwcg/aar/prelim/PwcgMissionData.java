package pwcg.aar.prelim;

import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.core.exception.PWCGException;
import pwcg.mission.data.MissionHeader;
import pwcg.mission.data.PwcgGeneratedMissionPlaneData;

public class PwcgMissionData 
{
    private MissionHeader missionHeader = new MissionHeader();
    private String missionDescription = "";
	private Map<Integer, PwcgGeneratedMissionPlaneData> missionPlanes  = new HashMap<>();

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

    public  Map<Integer, PwcgGeneratedMissionPlaneData> getMissionPlanes()
    {
        return missionPlanes;
    }

    public  PwcgGeneratedMissionPlaneData getMissionPlane(Integer pilotSerialNumber)
    {
        return missionPlanes.get(pilotSerialNumber);
    }

    public void addMissionPlanes(PwcgGeneratedMissionPlaneData  missionPlane) throws PWCGException
    {
        missionPlanes.put(missionPlane.getPilotSerialNumber(), missionPlane);
    }

    public String getMissionDescription()
    {
        return missionDescription;
    }

    public void setMissionDescription(String missionDescription)
    {
        this.missionDescription = missionDescription;
    }

    public void setMissionPlanes(Map<Integer, PwcgGeneratedMissionPlaneData> missionPlanes)
    {
        this.missionPlanes = missionPlanes;
    }
    
    public FrontMapIdentifier getMapId()
    {
        String mapName = missionHeader.getMapName();
        FrontMapIdentifier mapId = PWCGMap.getFrontMapIdentifierForName(mapName);
        return mapId;
    }
 }
