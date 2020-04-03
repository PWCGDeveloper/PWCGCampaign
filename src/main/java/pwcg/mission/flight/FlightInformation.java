package pwcg.mission.flight;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IMissionAltitudeGenerator;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.MissionAltitudeGeneratorFactory;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.target.TargetDefinition;

public class FlightInformation implements IFlightInformation
{
    private int flightId = IndexGenerator.getInstance().getNextIndex(); 
    private Campaign campaign;
    private Mission mission;
    private FlightTypes flightType;
    private Squadron squadron;
    private List<PlaneMcu> planes;
    private Coordinate targetSearchStartLocation;
    private TargetDefinition targetDefinition = new TargetDefinition();
    private boolean isPlayerFlight = false;
    private boolean isEscortedByPlayerFlight = false;
    private boolean isEscortForPlayerFlight = false;
    private int altitude;

    public FlightInformation(Mission mission)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
    }
    
    @Override
    public List<SquadronMember> getParticipatingPlayersForFlight()
    {
		return mission.getParticipatingPlayers().getParticipatingPlayersForSquadron(squadron.getSquadronId());
    }
    
    @Override
    public Mission getMission()
    {
        return mission;
    }

    public void setMission(Mission mission)
    {
        this.mission = mission;
    }

    @Override
    public Squadron getSquadron()
    {
        return squadron;
    }

    public void setSquadron(Squadron squadron)
    {
        this.squadron = squadron;
    }

    @Override
    public FlightTypes getFlightType()
    {
        return flightType;
    }

    public void setFlightType(FlightTypes flightType)
    {
        this.flightType = flightType;
    }

    @Override
    public boolean isPlayerFlight()
    {
        return isPlayerFlight;
    }

    public void setPlayerFlight(boolean isPlayerFlight)
    {
        this.isPlayerFlight = isPlayerFlight;
    }

    @Override
    public boolean isEscortedByPlayerFlight()
    {
        return isEscortedByPlayerFlight;
    }

    public void setEscortedByPlayerFlight(boolean isPlayerEscortedFlight)
    {
        this.isEscortedByPlayerFlight = isPlayerEscortedFlight;
    }

    @Override
    public boolean isEscortForPlayerFlight()
    {
        return isEscortForPlayerFlight;
    }

    public void setEscortForPlayerFlight(boolean isEscortForPlayerFlight)
    {
        this.isEscortForPlayerFlight = isEscortForPlayerFlight;
    }

    @Override
    public boolean isPlayerRelatedFlight()
    {
        if (isPlayerFlight || isEscortedByPlayerFlight || isEscortForPlayerFlight)
        {
            return true;
        }
        return false;
    }
    
    @Override
    public Coordinate getTargetPosition()
    {
        return targetDefinition.getTargetPosition();
    }
    
    @Override
    public List<SquadronMember> getFlightParticipatingPlayers() 
    {
		return mission.getParticipatingPlayers().getParticipatingPlayersForSquadron(squadron.getSquadronId());
	}

    public void setCampaign(Campaign campaign) 
	{
		this.campaign = campaign;
	}
	
	@Override
    public TargetDefinition getTargetDefinition() 
	{
		return targetDefinition;
	}

    public void setTargetDefinition(TargetDefinition targetDefinition) 
	{
		this.targetDefinition = targetDefinition;
	}

	@Override
    public boolean isVirtual()
    {
        boolean isVirtual = true;
        if (isPlayerFlight || isEscortedByPlayerFlight || isEscortForPlayerFlight)
        {
            isVirtual = false;
        }
        return isVirtual;
    }

    @Override
    public boolean isAirStart() throws PWCGException
    {
        boolean airstart = true;
        if (isPlayerFlight)
        {
            ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
            if (configManager.getIntConfigParam(ConfigItemKeys.AllowAirStartsKey) != 1)
            {
                airstart = false;
            }
        }
        return airstart;
    }

    @Override
    public boolean isParkedStart() throws PWCGException
    {
        boolean parkedStart = false;
        if (isPlayerFlight)
        {
            ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
            if (configManager.getIntConfigParam(ConfigItemKeys.AllowAirStartsKey) == 2)
            {
                parkedStart = true;
            }
        }
        return parkedStart;
    }
    
    @Override
    public IAirfield getDepartureAirfield() throws PWCGException
    {
        String airfieldName = squadron.determineCurrentAirfieldName(campaign.getDate());
        IAirfield departureAirfield = PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAirfield(airfieldName);
        return departureAirfield;
    }

    @Override
    public List<PlaneMcu> getPlanes()
    {
        return planes;
    }

    public void setPlanes(List<PlaneMcu> planes)
    {
        this.planes = planes;
    }

    @Override
    public Campaign getCampaign()
    {
        return campaign;
    }

    @Override
    public Coordinate getTargetSearchStartLocation()
    {
        return targetSearchStartLocation.copy();
    }

    public void setTargetSearchStartLocation(Coordinate targetSearchStartLocation)
    {
        this.targetSearchStartLocation = targetSearchStartLocation;
    }

    @Override
    public int getAltitude()
    {
        return altitude;
    }
    
    @Override
    public void setAltitude(int altitude)
    {
        this.altitude = altitude;
    }

    @Override
    public void calculateAltitude() throws PWCGException
    {
        IMissionAltitudeGenerator missionAltitudeGenerator = MissionAltitudeGeneratorFactory.createMissionAltitudeGenerator();
        this.altitude = missionAltitudeGenerator.determineFlightAltitude(campaign, flightType);
    }

    @Override
    public boolean isFighterMission()
    {
        boolean isFighterMission = false;
        if (flightType.isCategory(FlightTypeCategory.FIGHTER))
        {
            isFighterMission = true;
        }

        return isFighterMission;
    }

    @Override
    public ICountry getCountry()
    {
        return squadron.getCountry();
    }

    @Override
    public Coordinate getFlightHomePosition() throws PWCGException
    {
        return squadron.determineCurrentPosition(campaign.getDate());
    }

    @Override
    public String getAirfieldName()
    {
        return squadron.determineCurrentAirfieldName(campaign.getDate());
    }

    @Override
    public IAirfield getAirfield()
    {
        return squadron.determineCurrentAirfieldCurrentMap(campaign.getDate());
    }
    
    @Override
    public int getFlightId()
    {
        return flightId;
    }

}
