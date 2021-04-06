package pwcg.mission.flight;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IMissionAltitudeGenerator;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.MissionAltitudeGeneratorFactory;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.flight.plane.FormationTypeCalculator;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.mcu.McuFormation;
import pwcg.mission.target.TargetDefinition;

public class FlightInformation
{
    private int flightId = IndexGenerator.getInstance().getNextIndex();
    private Campaign campaign;
    private Mission mission;
    private FlightTypes flightType;
    private List<PlaneMcu> planes;
    private Squadron squadron;
    private Coordinate targetSearchStartLocation;
    private boolean isPlayerFlight = false;
    private boolean isEscortedByPlayerFlight = false;
    private boolean isEscortForPlayerFlight = false;
    private boolean isScrambleOpposeFlight = false;
    private boolean isOpposingFlight = false;
    private boolean isAiTriggeredTakeoff = false;
    private int altitude = 0;
    private int flightCruisingSpeed = 0;
    private int formationType = McuFormation.FORMATION_V;
    private TargetDefinition targetDefinition;;

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
        this.formationType = FormationTypeCalculator.calculateFormationType(flightType);
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

    public boolean isOpposingFlight()
    {
        return isOpposingFlight;
    }

    public void setOpposingFlight(boolean isOpposingFlight)
    {
        this.isOpposingFlight = isOpposingFlight;
    }    
    
    public boolean isScrambleOpposeFlight()
    {
        return isScrambleOpposeFlight;
    }

    public void setScrambleOpposeFlight(boolean isScrambleOpposeFlight)
    {
        this.isScrambleOpposeFlight = isScrambleOpposeFlight;
    }

    public List<SquadronMember> getFlightParticipatingPlayers()
    {
        return mission.getParticipatingPlayers().getParticipatingPlayersForSquadron(squadron.getSquadronId());
    }

    public void setCampaign(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public boolean isVirtual()
    {
        boolean isVirtual = true;
        if (isPlayerFlight || isEscortedByPlayerFlight || isEscortForPlayerFlight || isScrambleOpposeFlight || isAiTriggeredTakeoff)
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
        if (isAiTriggeredTakeoff)
        {
            airstart = false;
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

    public Airfield getDepartureAirfield() throws PWCGException
    {
        String airfieldName = squadron.determineCurrentAirfieldName(campaign.getDate());
        Airfield departureAirfield = PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAirfield(airfieldName);
        return departureAirfield;
    }

    public List<PlaneMcu> getPlanes()
    {
        return planes;
    }

    public void setPlanes(List<PlaneMcu> planes)
    {
        this.planes = planes;
    }

    public Campaign getCampaign()
    {
        return campaign;
    }

    public Coordinate getTargetSearchStartLocation()
    {
        return targetSearchStartLocation.copy();
    }

    public void setTargetSearchStartLocation(Coordinate targetSearchStartLocation)
    {
        this.targetSearchStartLocation = targetSearchStartLocation;
    }

    public int getAltitude()
    {
        return altitude;
    }

    public void setAltitude(int altitude)
    {
        this.altitude = altitude;
    }

    public int getFlightCruisingSpeed()
    {
        return flightCruisingSpeed;
    }

    public void setCruisingSpeed(int cruisingSpeed)
    {
        this.flightCruisingSpeed = cruisingSpeed;
    }

    public void calculateAltitude() throws PWCGException
    {
        IMissionAltitudeGenerator missionAltitudeGenerator = MissionAltitudeGeneratorFactory.createMissionAltitudeGenerator();
        this.altitude = missionAltitudeGenerator.determineFlightAltitude(campaign, flightType, mission.getWeather());
    }

    public boolean isFighterMission()
    {
        boolean isFighterMission = false;
        if (flightType.isCategory(FlightTypeCategory.FIGHTER))
        {
            isFighterMission = true;
        }

        return isFighterMission;
    }

    public ICountry getCountry()
    {
        return squadron.getCountry();
    }

    public Coordinate getFlightHomePosition() throws PWCGException
    {
        return squadron.determineCurrentPosition(campaign.getDate()).copy();
    }

    public String getAirfieldName()
    {
        return squadron.determineCurrentAirfieldName(campaign.getDate());
    }

    public Airfield getAirfield()
    {
        return squadron.determineCurrentAirfieldCurrentMap(campaign.getDate());
    }

    public int getFlightId()
    {
        return flightId;
    }

    public int getFormationType()
    {
        return formationType;
    }

    public TargetDefinition getTargetDefinition()
    {
        return targetDefinition;
    }

    public void setTargetDefinition(TargetDefinition targetDefinition)
    {
        this.targetDefinition = targetDefinition;
    }

    public boolean isAiTriggeredTakeoff()
    {
        return isAiTriggeredTakeoff;
    }
    
    
}
