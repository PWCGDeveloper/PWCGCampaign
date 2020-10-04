package pwcg.mission.options;

import java.util.Date;

import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.options.MapSeasonalParameters.Season;
import pwcg.mission.utils.MissionTime;

public abstract class MissionOptions 
{
	protected MapSeasonalParameters winter = new MapSeasonalParameters();
	protected MapSeasonalParameters spring = new MapSeasonalParameters();
	protected MapSeasonalParameters summer = new MapSeasonalParameters();
	protected MapSeasonalParameters autumn = new MapSeasonalParameters();

	public static int SINGLE_MISSION = 0;
	public static int COOP_MISSION = 1;
	
	protected int lCName = 0;
	protected int lCDesc = 1;
	protected int lCAuthor = 2;
    protected String layers = "";
    protected int aqmId = 0;
    protected int seaState = 0;
	
	protected int missionType = SINGLE_MISSION;
	
    protected String playerConfig = "";
    protected MissionTime missionTime = null;
    protected Mission mission = null;

    public MissionOptions()
    {       
    }

    public void createFlightSpecificMissionOptions(Mission mission) throws PWCGException 
    {
    	this.mission = mission;
        createMissionTime();
        PWCGContext.getInstance().getCurrentMap().getMapWeather().createMissionWeather(mission);
    }
    
    public MapSeasonalParameters getSeasonBasedParameters(Date date) throws PWCGException
    {
        Season season = PWCGContext.getInstance().getCurrentMap().getMapWeather().getSeason(date);
        if (season == Season.WINTER)
        {
        	return winter;
        }
        if (season == Season.SPRING)
        {
        	return spring;
        }
        if (season == Season.SUMMER)
        {
        	return summer;
        }
        if (season == Season.AUTUMN)
        {
        	return autumn;
        }
        
    	throw new PWCGException("Badly defined season: " + season);
    }

    private void createMissionTime() throws PWCGException 
    {          
        missionTime = new MissionTime(mission.getCampaign().getDate(), mission.isNightMission());
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

    public int getSeaState()
    {
        return seaState;
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
}
