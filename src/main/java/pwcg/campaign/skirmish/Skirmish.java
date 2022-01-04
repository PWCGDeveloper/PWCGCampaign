package pwcg.campaign.skirmish;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.PwcgRole;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.target.TargetType;

public class Skirmish
{
    public static String CARGO_ROUTE_BATTLE = "Cargo Route";
    public static String SHIPPING_ENCOUNTER_BATTLE = "Ship Encounter";

    private String skirmishName;
    private Coordinate neCorner;
    private Coordinate swCorner;
    private Date startDate;
    private Date stopDate;
    private Side attackerAir;
    private Side attackerGround;
    private SkirmishProfileType profileType;
    private List<SkirmishIconicFlights> iconicFlightTypes = new ArrayList<>();
    private List<SkirmishForceRoleConversion> forcedRoleConversions = new ArrayList<>();

    public Skirmish(
            String skirmishName, 
            Coordinate position, 
            Date date, 
            Side attackerAir, 
            Side attackerGround, 
            SkirmishProfileType profileType, 
            List<SkirmishIconicFlights> iconicFlightTypes,
            List<SkirmishForceRoleConversion> forcedRoleConversions)
    {
        this.skirmishName = skirmishName;
        this.swCorner = new Coordinate(position.getXPos() - 5000, 0, position.getZPos() - 5000);
        this.neCorner = new Coordinate(position.getXPos() + 5000, 0, position.getZPos() + 5000);
        this.startDate = date;
        this.stopDate = date;
        this.attackerAir = attackerAir;
        this.attackerGround = attackerGround;
        this.profileType = profileType;
        this.iconicFlightTypes = iconicFlightTypes;
        this.forcedRoleConversions = forcedRoleConversions;
    }

    public Skirmish()
    {
    }

    public String getSkirmishName()
    {
        return skirmishName;
    }

    public void setSkirmishName(String skirmishName)
    {
        this.skirmishName = skirmishName;
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

    public boolean needsMoreIconicFlightType(FlightInformation flightInformation, int currentCount) throws PWCGException
    {
        for (SkirmishIconicFlights iconicFlightType : iconicFlightTypes)
        {
            if (iconicFlightType.getSide() == flightInformation.getSquadron().determineSide())
            {
                if (iconicFlightType.getFlightType().isLowAltEquivalentFlightType(flightInformation.getFlightType()))
                {
                    if (currentCount < iconicFlightType.getMaxForcedFlightTypes())
                    {
                        PWCGLogger.log(LogLevel.DEBUG,
                                "Accept Skirmish: " + flightInformation.getSquadron().determineDisplayName(flightInformation.getCampaign().getDate())
                                        + " flying " + flightInformation.getFlightType());
                        return true;
                    }
                    else
                    {
                        PWCGLogger.log(LogLevel.DEBUG, "Reject Skirmish because has enough of flight type: "
                                + flightInformation.getSquadron().determineDisplayName(flightInformation.getCampaign().getDate()));
                    }
                }
                else
                {
                    PWCGLogger.log(LogLevel.DEBUG,
                            "Reject Skirmish because wrong flight type: "
                                    + flightInformation.getSquadron().determineDisplayName(flightInformation.getCampaign().getDate()) + " flying "
                                    + flightInformation.getFlightType());
                }
            }
            else
            {
                PWCGLogger.log(LogLevel.DEBUG, "Reject Skirmish because wrong side: "
                        + flightInformation.getSquadron().determineDisplayName(flightInformation.getCampaign().getDate()));
            }
        }

        return false;
    }

    public boolean hasFlighTypeForRole(Company squadron, PwcgRole role) throws PWCGException
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

    public FlightTypes getFlighTypeForRole(Company squadron, PwcgRole role) throws PWCGException
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

    public PwcgRole forceRoleConversion(PwcgRole role, Side side)
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

    private List<SkirmishProfileElement> getSkirmishProfileElementForSide(Company squadron) throws PWCGException
    {
        List<SkirmishProfileElement> skirmishElementsForSide = new ArrayList<>();
        SkirmishProfileAirAssociation squadronAssociation = getSkirmishAssociation(squadron);

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

    private SkirmishProfileAirAssociation getSkirmishAssociation(Company squadron) throws PWCGException
    {
        SkirmishProfileAirAssociation squadronAssociation = SkirmishProfileAirAssociation.DEFENDER;
        if (squadron.determineSide() == attackerAir)
        {
            squadronAssociation = SkirmishProfileAirAssociation.ATTACKER;
        }
        return squadronAssociation;
    }

    public boolean hasTargetType(TargetType targetType) throws PWCGException
    {
        SkirmishProfile skirmishProfile = PWCGContext.getInstance().getSkirmishProfileManager().getSkirmishProfile(profileType);
        for (SkirmishProfileElement skirmishProfileElement : skirmishProfile.getSkirmishProfileElements())
        {
            if (skirmishProfileElement.getTargetType() == targetType)
            {
                return true;
            }
        }
        return false;
    }

    public TargetType getTargetTypeForFlightType(FlightTypes flightType, Side side) throws PWCGException
    {
        SkirmishProfileAirAssociation attackerOrDefender = getSkirmishProfileAirAssociationForSide(side);

        SkirmishProfile skirmishProfile = PWCGContext.getInstance().getSkirmishProfileManager().getSkirmishProfile(profileType);
        for (SkirmishProfileElement skirmishProfileElement : skirmishProfile.getSkirmishProfileElements())
        {
            if (skirmishProfileElement.getAssociation() == attackerOrDefender)
            {
                if (skirmishProfileElement.getPreferredFlightType() == flightType)
                {
                    return skirmishProfileElement.getTargetType();
                }
            }
        }
        return TargetType.TARGET_NONE;
    }

    private SkirmishProfileAirAssociation getSkirmishProfileAirAssociationForSide(Side side)
    {
        if (attackerAir == side)
        {
            return SkirmishProfileAirAssociation.ATTACKER;
        }
        return SkirmishProfileAirAssociation.DEFENDER;
    }

    public boolean isCargoRouteBattle()
    {
        if (skirmishName.startsWith(CARGO_ROUTE_BATTLE))
        {
            return true;
        }
        return false;
    }

    public boolean isShipEncounterZoneBattle()
    {
        if (skirmishName.startsWith(SHIPPING_ENCOUNTER_BATTLE))
        {
            return true;
        }
        return false;
    }

    public Side getAttackerAir()
    {
        return attackerAir;
    }

    public Side getAttackerGround()
    {
        return attackerGround;
    }

    public SkirmishProfileType getProfileType()
    {
        return profileType;
    }
}
