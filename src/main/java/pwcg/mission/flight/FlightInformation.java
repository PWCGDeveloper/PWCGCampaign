package pwcg.mission.flight;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IMissionAltitudeGenerator;
import pwcg.campaign.factory.MissionAltitudeGeneratorFactory;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.mcu.McuFormation;
import pwcg.mission.target.TargetType;

public class FlightInformation
{
    private int flightId = IndexGenerator.getInstance().getNextIndex();
    private Campaign campaign;
    private Mission mission;
    private FlightTypes flightType;
    private PlaneType planeType;
    private List<PlaneMcu> planes;
    private ICountry country;
    private String flightName;
    private Coordinate homePosition;
    private Coordinate targetSearchStartLocation;
    private TargetType roleBasedTarget = TargetType.TARGET_NONE;

    private boolean isAiTriggeredTakeoff = false;
    private int altitude = 0;
    private int flightCruisingSpeed = 0;
    private int formationType = McuFormation.FORMATION_V;

    public FlightInformation(FlightBuildInformation flightBuildInformation) throws PWCGException
    {
        this.mission = flightBuildInformation.getMission();
        this.campaign = flightBuildInformation.getMission().getCampaign();
        this.flightType = flightBuildInformation.getFlightType();
        this.planeType = flightBuildInformation.getPlaneType();
        this.country = flightBuildInformation.getCountry();
        this.homePosition = flightBuildInformation.getHomePosition();
        this.flightName = flightBuildInformation.getFlightName();
        this.targetSearchStartLocation = flightBuildInformation.getMission().getMissionBorders().getCenter().copy();
    }

    public Mission getMission()
    {
        return mission;
    }

    public void setCampaign(Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public TargetType getRoleBasedTarget()
    {
        return roleBasedTarget;
    }

    public PlaneType getPlaneType()
    {
        return planeType;
    }

    public void setRoleBasedTarget(TargetType roleBasedTarget)
    {
        this.roleBasedTarget = roleBasedTarget;
    }

    public boolean isVirtual()
    {
        return true;
    }

    public boolean isAirStart() throws PWCGException
    {
        return true;
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
        return country;
    }

    public int getFlightId()
    {
        return flightId;
    }

    public FlightTypes getFlightType()
    {
        return flightType;
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

    public String getFlightName()
    {
        return flightName;
    }

    public Coordinate getHomePosition()
    {
        return homePosition;
    }
}
