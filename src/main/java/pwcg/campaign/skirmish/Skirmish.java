package pwcg.campaign.skirmish;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
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
    private Side attacker;
    private SkirmishProfileType profileType;
    private List<SkirmishIconicFlights> iconicFlightTypes = new ArrayList<>();
    private List<SkirmishForceRoleConversion> forcedRoleConversions = new ArrayList<>();

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

    public Coordinate getCenter() throws PWCGException
    {
        CoordinateBox skirmishBox = CoordinateBox.coordinateBoxFromCorners(swCorner, neCorner);
        return skirmishBox.getCenter();
    }

    public boolean needsMoreIconicFlightType (FlightTypes flightType, int currentCount) throws PWCGException
    {
        for (SkirmishIconicFlights iconicFlightType : iconicFlightTypes)
        {
            if (iconicFlightType.getFlightType() == flightType)
            {
                if (currentCount < iconicFlightType.getMaxForcedFlightTypes())
                {
                    return true;
                }
            }
        }
        
        return false;
    }

    public boolean hasFlighTypeForRole(Squadron squadron, Role role) throws PWCGException
    {
        List<SkirmishProfileElement> skirmishElementsForSide = getSkirmishProfileElementForSide(squadron);
        for (SkirmishProfileElement skirmishProfileElement : skirmishElementsForSide)
        {
            if (skirmishProfileElement.getRole() == role)
            {
                return true;
            }
        }
        
        return false;
    }

    public FlightTypes getFlighTypeForRole(Squadron squadron, Role role) throws PWCGException
    {
        List<SkirmishProfileElement> skirmishElementsForSide = getSkirmishProfileElementForSide(squadron);
        for (SkirmishProfileElement skirmishProfileElement : skirmishElementsForSide)
        {
            if (skirmishProfileElement.getRole() == role)
            {
                return skirmishProfileElement.getPreferredFlightType();
            }
        }
        
        return FlightTypes.ANY;
    }

    public TargetType getTargetForFlightType(FlightInformation flightInformation) throws PWCGException
    {
        List<SkirmishProfileElement> skirmishElementsForSide = getSkirmishProfileElementForSide(flightInformation.getSquadron());
        for (SkirmishProfileElement skirmishProfileElement : skirmishElementsForSide)
        {
            if (skirmishProfileElement.getPreferredFlightType() == flightInformation.getFlightType())
            {
                return skirmishProfileElement.getTargetType();
            }
        }
        
        return TargetType.TARGET_NONE;
    }

    public Role forceRoleConversion(Role role, Side side)
    {
        for (SkirmishForceRoleConversion forcedRoleConversion : forcedRoleConversions)
        {
            if (forcedRoleConversion.getSide() == side)
            {
                if (forcedRoleConversion.getFromRole() == role)
                {
                    return forcedRoleConversion.getToRole();
                }
            }
        }
        
        return role;
    }

    private List<SkirmishProfileElement> getSkirmishProfileElementForSide(Squadron squadron) throws PWCGException
    {
        List<SkirmishProfileElement> skirmishElementsForSide = new ArrayList<>();
        SkirmishProfileAssociation squadronAssociation = getSkirmishAssociation(squadron);

        SkirmishProfile skirmishProfile = PWCGContext.getInstance().getSkirmishProfileManager().getSkirmishProfile(profileType);
        for (SkirmishProfileElement skirmishProfileElement : skirmishProfile.getSkirmishProfileElements())
        {
            if (skirmishProfileElement.getAssociation() == squadronAssociation)
            {
                skirmishElementsForSide.add(skirmishProfileElement);
            }
        }
        
        return skirmishElementsForSide;
    }

    private SkirmishProfileAssociation getSkirmishAssociation(Squadron squadron) throws PWCGException
    {
        SkirmishProfileAssociation squadronAssociation = SkirmishProfileAssociation.DEFENDER;
        if (squadron.determineSide() == attacker)
        {
            squadronAssociation = SkirmishProfileAssociation.ATTACKER;
        }
        return squadronAssociation;
    }

    public Side getAttacker()
    {
        return attacker;
    }

    public SkirmishProfileType getProfileType()
    {
        return profileType;
    }
}
