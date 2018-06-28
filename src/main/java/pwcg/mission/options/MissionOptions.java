package pwcg.mission.options;

import java.util.Date;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.Flight;
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

    public MissionOptions()
    {       
    }

    public void createFlightSpecificMissionOptions(Flight playerFlight) throws PWCGException 
    {
        PWCGContextManager.getInstance().getCurrentMap().getMapWeather().createMissionWeather(playerFlight);
        createMissionTime (playerFlight.getCampaign().getDate(), playerFlight.isNightFlight());
    }
    
    public MapSeasonalParameters getSeasonBasedParameters(Date date) throws PWCGException
    {
        Season season = PWCGContextManager.getInstance().getCurrentMap().getMapWeather().getSeason(date);
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

    private void createMissionTime(Date date, boolean isNightFlight) throws PWCGException 
    {          
        missionTime = new MissionTime(date, isNightFlight);
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
