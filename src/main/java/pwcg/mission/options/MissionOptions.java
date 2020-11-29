package pwcg.mission.options;

import java.util.Date;

import pwcg.core.exception.PWCGException;
import pwcg.mission.MissionProfile;
import pwcg.mission.utils.MissionTime;

public class MissionOptions 
{
	public static int SINGLE_MISSION = 0;
	public static int COOP_MISSION = 1;
	
	private int lCName = 0;
	private int lCDesc = 1;
	private int lCAuthor = 2;
    private String layers = "";
    private int aqmId = 0;
	
	private int missionType = SINGLE_MISSION;
	
    private String playerConfig = "";
    private MissionTime missionTime = null;
    private Date date;
    private MissionProfile missionProfile;
    

    public MissionOptions(Date date, MissionProfile missionProfile)
    {       
        this.date = date;
        this.missionProfile = missionProfile;
    }

    public void createFlightSpecificMissionOptions() throws PWCGException 
    {
        createMissionTime();
    }

    private void createMissionTime() throws PWCGException 
    {          
        missionTime = new MissionTime(date, missionProfile.isNightMission());
        missionTime.generateMissionDateTime();
    }

	
	public int getlCName() {
		return lCName;
	}

	public int getlCDesc() {
		return lCDesc;
	}

	public int getlCAuthor() {
		return lCAuthor;
	}

	public String getPlayerConfig() {
		return playerConfig;
	}

    public MissionTime getMissionTime()
    {
        return missionTime;
    }

    public String getLayers()
    {
        return layers;
    }
    
    public int getAqmId()
    {
        return aqmId;
    }

    public void setPlayerConfig(String playerConfig) {
        this.playerConfig = playerConfig;
    }
    
    public void setMissionType(int missionType) {
        this.missionType = missionType;
    }

	public int getMissionType()
	{
		return missionType;
	}

    public int getMissionHour()
    {
        if (missionTime == null)
        {
            return 12;
        }
        
        return missionTime.getMissionHour();
    }
}
