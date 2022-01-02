package pwcg.mission.flight;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IMissionAltitudeGenerator;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.factory.MissionAltitudeGeneratorFactory;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.squadron.Company;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.flight.plane.FormationTypeCalculator;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.mcu.McuFormation;
import pwcg.mission.target.TargetType;

public class FlightInformation
{
    private int flightId = IndexGenerator.getInstance().getNextIndex();
    private Campaign campaign;
    private Mission mission;
    private FlightTypes flightType;
    private List<PlaneMcu> planes;
    private Company squadron;
    private Coordinate targetSearchStartLocation;
    private NecessaryFlightType necessaryFlightType = NecessaryFlightType.NONE;
    private TargetType roleBasedTarget = TargetType.TARGET_NONE;

    private boolean isAiTriggeredTakeoff = false;
    private int altitude = 0;
    private int flightCruisingSpeed = 0;
    private int formationType = McuFormation.FORMATION_V;

    public FlightInformation(Mission mission, NecessaryFlightType necessaryFlightType)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
        this.necessaryFlightType = necessaryFlightType;
    }

    public List<CrewMember> getParticipatingPlayersForFlight()
    {
        return mission.getParticipatingPlayers().getParticipatingPlayersForSquadron(squadron.getSquadronId());
    }

    public Mission getMission()
    {
        return mission;
    }

    public Company getSquadron()
    {
        return squadron;
    }

    public void setSquadron(Company squadron)
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
    
    public boolean isNecessaryFlight()
    {
        return (necessaryFlightType != NecessaryFlightType.NONE);
    }


    public boolean isPlayerFlight()
    {
        return (necessaryFlightType == NecessaryFlightType.PLAYER_FLIGHT);
    }

    public boolean isOpposingFlight()
    {
        return (necessaryFlightType == NecessaryFlightType.OPPOSING_FLIGHT);
    }

    public List<CrewMember> getFlightParticipatingPlayers()
    {
        return mission.getParticipatingPlayers().getParticipatingPlayersForSquadron(squadron.getSquadronId());
    }

    public void setCampaign(Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public TargetType getRoleBasedTarget()
    {
        return roleBasedTarget;
    }

    public void setRoleBasedTarget(TargetType roleBasedTarget)
    {
        this.roleBasedTarget = roleBasedTarget;
    }

    public boolean isVirtual()
    {
        boolean isVirtual = true;
        if (necessaryFlightType == NecessaryFlightType.PLAYER_ESCORT ||
            necessaryFlightType == NecessaryFlightType.PLAYER_ESCORTED ||
            necessaryFlightType == NecessaryFlightType.PLAYER_FLIGHT ||
            isAiTriggeredTakeoff)
        {
            isVirtual = false;
        }
        return isVirtual;
    }

    public boolean isAirStart() throws PWCGException
    {
        boolean airstart = true;
        if (necessaryFlightType == NecessaryFlightType.PLAYER_FLIGHT)
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
        if (necessaryFlightType == NecessaryFlightType.PLAYER_FLIGHT)
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

    public boolean isAiTriggeredTakeoff()
    {
        return isAiTriggeredTakeoff;
    }

    public void setAiTriggeredTakeoff(boolean isAiTriggeredTakeoff)
    {
        this.isAiTriggeredTakeoff = isAiTriggeredTakeoff;
    }

    public NecessaryFlightType getNecessaryFlightType()
    {
        return necessaryFlightType;
    }
}
