package pwcg.campaign;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.target.TargetType;

public class Skirmish
{
    private String name;
	private Coordinate neCorner;
	private Coordinate swCorner;
    private Date startDate;
    private Date stopDate;
    private Side aggressor;
    private List<FlightTypes> iconicFlightTypes = new ArrayList<>();
    private List<SkirmishMissionPreference> skirmishMissionPreferences = new ArrayList<>();

	public Skirmish()
	{
	}

    public String getName()
    {
        return name;
    }

    public CoordinateBox getCoordinateBox() throws PWCGException
    {
        CoordinateBox skirmishBox = CoordinateBox.coordinateBoxFromCorners(swCorner, neCorner);
        return skirmishBox;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public Date getStopDate()
    {
        return stopDate;
    }

    public Side getAggressor()
    {
        return aggressor;
    }

    public List<SkirmishMissionPreference> getSkirmishMissionPreferences()
    {
        return skirmishMissionPreferences;
    }

    public Coordinate getCenter() throws PWCGException
    {
        CoordinateBox skirmishBox = CoordinateBox.coordinateBoxFromCorners(swCorner, neCorner);
        return skirmishBox.getCenter();
    }

    public FlightTypes getFlighTypeForRole(Squadron squadron, Role role) throws PWCGException
    {
        for (SkirmishMissionPreference skirmishMissionPreference : skirmishMissionPreferences)
        {
            if (skirmishMissionPreference.getSide() == squadron.determineSide())
            {
                if (skirmishMissionPreference.getRole() == role)
                {
                    return skirmishMissionPreference.getPreferredFlightType();
                }
            }
        }
        
        return FlightTypes.ANY;
    }

    public boolean isIconicFlightType (FlightTypes flightTypes) throws PWCGException
    {
        for (FlightTypes iconicFlightType : iconicFlightTypes)
        {
            if (iconicFlightType == flightTypes)
            {
                return true;
            }
        }
        
        return false;
    }

    public TargetType getTargetForFlightType(FlightInformation flightInformation) throws PWCGException
    {
        for (SkirmishMissionPreference skirmishMissionPreference : skirmishMissionPreferences)
        {
            if (skirmishMissionPreference.getSide() == flightInformation.getSquadron().determineSide())
            {
                if (skirmishMissionPreference.getPreferredFlightType() == flightInformation.getFlightType())
                {
                    return skirmishMissionPreference.getTargetType();
                }
            }
        }
        
        return TargetType.TARGET_NONE;
    }
}
