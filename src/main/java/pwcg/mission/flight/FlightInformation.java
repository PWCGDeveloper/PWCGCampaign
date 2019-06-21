package pwcg.mission.flight;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.target.TargetDefinition;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.flight.plane.PlaneMCU;

public class FlightInformation
{
    private Campaign campaign;
    private Mission mission;
    private Squadron squadron;
    private FlightTypes flightType;
    private boolean isPlayerFlight = false;
    private boolean isEscortedByPlayerFlight = false;
    private boolean isEscortForPlayerFlight = false;
    private List<SquadronMember> participatingPlayers;
    private TargetDefinition targetDefinition = new TargetDefinition();
    private List<PlaneMCU> planes;
    
    public FlightInformation(Mission mission)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
    }
    
    public List<SquadronMember> getParticipatingPlayersForFlight()
    {
		return mission.getParticipatingPlayers().getParticipatingPlayersForSquadron(squadron.getSquadronId());
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
        return targetDefinition.getTargetPosition();
    }
    
    public List<SquadronMember> getParticipatingPlayers() 
    {
		return participatingPlayers;
	}

	public void setCampaign(Campaign campaign) 
	{
		this.campaign = campaign;
	}
	
	public TargetDefinition getTargetDefinition() 
	{
		return targetDefinition;
	}

	public void setTargetDefinition(TargetDefinition targetDefinition) 
	{
		this.targetDefinition = targetDefinition;
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
    
    public IAirfield getDepartureAirfield() throws PWCGException
    {
        String airfieldName = squadron.determineCurrentAirfieldName(campaign.getDate());
        IAirfield departureAirfield = PWCGContextManager.getInstance().getCurrentMap().getAirfieldManager().getAirfield(airfieldName);
        return departureAirfield;
    }

    public List<PlaneMCU> getPlanes()
    {
        return planes;
    }

    public void setPlanes(List<PlaneMCU> planes)
    {
        this.planes = planes;
    }

    public Campaign getCampaign()
    {
        return campaign;
    }
}
