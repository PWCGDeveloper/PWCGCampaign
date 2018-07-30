package pwcg.mission.flight;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;

public class FlightInformation
{
    private Campaign campaign;
    private Mission mission;
    private Squadron squadron;
    private FlightTypes flightType = FlightTypes.ANY;
    private boolean isPlayerFlight = false;
    private boolean isEscortedByPlayerFlight = false;
    private boolean isEscortForPlayerFlight = false;
    private Coordinate targetCoords;
    
    public FlightInformation(Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public Mission getMission()
    {
        return mission;
    }

    public void setMission(Mission mission)
    {
        this.mission = mission;
    }

    public Squadron getSquadron()
    {
        return squadron;
    }

    public void setSquadron(Squadron squadron)
    {
        this.squadron = squadron;
    }

    public FlightTypes getFlightType()
    {
        return flightType;
    }

    public void setFlightType(FlightTypes flightType)
    {
        this.flightType = flightType;
    }

    public boolean isPlayerFlight()
    {
        return isPlayerFlight;
    }

    public void setPlayerFlight(boolean isPlayerFlight)
    {
        this.isPlayerFlight = isPlayerFlight;
    }

    public boolean isEscortedByPlayerFlight()
    {
        return isEscortedByPlayerFlight;
    }

    public void setEscortedByPlayerFlight(boolean isPlayerEscortedFlight)
    {
        this.isEscortedByPlayerFlight = isPlayerEscortedFlight;
    }

    public boolean isEscortForPlayerFlight()
    {
        return isEscortForPlayerFlight;
    }

    public void setEscortForPlayerFlight(boolean isEscortForPlayerFlight)
    {
        this.isEscortForPlayerFlight = isEscortForPlayerFlight;
    }

    public Coordinate getTargetCoords()
    {
        return targetCoords;
    }

    public void setTargetCoords(Coordinate targetCoords)
    {
        this.targetCoords = targetCoords;
    }

    public boolean isVirtual()
    {
        boolean isVirtual = true;
        if (isPlayerFlight || isEscortedByPlayerFlight || isEscortForPlayerFlight)
        {
            isVirtual = false;
        }
        return isVirtual;
    }

    public boolean isFriendly() throws PWCGException
    {
        boolean isFriendly = false;
        if (squadron.determineSquadronCountry(campaign.getDate()).isSameSide(campaign.determineCountry()))
        {
            isFriendly = true;
        }
        return isFriendly;
    }

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

    public IAirfield getDepartureAirfield() throws PWCGException
    {
        String airfieldName = squadron.determineCurrentAirfieldName(campaign.getDate());
        IAirfield departureAirfield = PWCGContextManager.getInstance().getCurrentMap().getAirfieldManager().getAirfield(airfieldName);
        return departureAirfield;
    }

    public Campaign getCampaign()
    {
        return campaign;
    }
}
