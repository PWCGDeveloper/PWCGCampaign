package pwcg.campaign.skirmish;

import pwcg.campaign.tank.PwcgRole;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.target.TargetType;

public class SkirmishProfileElement
{
    private SkirmishProfileAirAssociation association;
    private PwcgRole role;
    private FlightTypes preferredFlightType;
    private TargetType targetType;

    public SkirmishProfileAirAssociation getAssociation()
    {
        return association;
    }

    public PwcgRole getRole()
    {
        return role;
    }

    public FlightTypes getPreferredFlightType()
    {
        return preferredFlightType;
    }

    public TargetType getTargetType()
    {
        return targetType;
    }

}
