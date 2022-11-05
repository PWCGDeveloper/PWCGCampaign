package pwcg.campaign.skirmish;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.io.json.SkirmishIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.target.TargetType;

public class SkirmishManager
{
	private Skirmishes skirmishes = new Skirmishes();
    private FrontMapIdentifier map;

	public SkirmishManager(FrontMapIdentifier map)
	{
	    this.map = map;
	}
	
	public void initialize()
	{
        try
        {
            skirmishes = SkirmishIOJson.readJson(map.getMapName());
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
	}

    public Skirmish getSkirmishByName(String skirmishName) throws PWCGException 
    {     
        for (Skirmish skirmish : skirmishes.getSkirmishes())
        {
            if (skirmish.getSkirmishName().equals(skirmishName))
            {
                return skirmish;
            }
        }
        return null;
    }

    public List<Skirmish> getSkirmishesForDate(Campaign campaign, MissionHumanParticipants participatingPlayers) throws PWCGException 
    {     
        List<Skirmish> skirmishesForCampaign =  getSetPieceSkirmishesForDate(campaign);
        if (skirmishesForCampaign.isEmpty())
        {
            skirmishesForCampaign = getCargoRouteSkirmishesForDate(campaign, participatingPlayers);
        }
        return skirmishesForCampaign;
    }

    private List<Skirmish> getSetPieceSkirmishesForDate(Campaign campaign) throws PWCGException
    {
        List<Skirmish> skirmishesForCampaign = new ArrayList<>();
        for (Skirmish skirmish : skirmishes.getSkirmishes())
        {
            if (isSkirmishAtRightTime(campaign.getDate(), skirmish))
            {
                skirmishesForCampaign.add(skirmish);
            }
        }
        
        return skirmishesForCampaign;
    }

    private List<Skirmish> getCargoRouteSkirmishesForDate(Campaign campaign, MissionHumanParticipants participatingPlayers) throws PWCGException
    {
        CargoRouteSkirmishBuilder cargoRouteSkirmishBuilder = new CargoRouteSkirmishBuilder(campaign, participatingPlayers);
        List<Skirmish> skirmishesForCampaign = cargoRouteSkirmishBuilder.getSkirmishesForDate();
        return skirmishesForCampaign;
    }

	private boolean isSkirmishAtRightTime(Date date, Skirmish skirmish) throws PWCGException
    {
        if (DateUtils.isDateInRange(date, skirmish.getStartDate(), skirmish.getStopDate()))
        {
        	return true;
        }
            
        return false;
    }

    public TargetType getIconicTargetTypes(FlightInformation flightInformation) throws PWCGException
    {
        Skirmish missionSkirmish = flightInformation.getMission().getSkirmish();
        if (missionSkirmish != null)
        {
            TargetType iconicTargetType = missionSkirmish.getTargetForFlightType(flightInformation);
            return iconicTargetType;
        }

        return TargetType.TARGET_NONE;
    }

    public Skirmishes getSkirmishes()
    {
        return skirmishes;
    }
}
