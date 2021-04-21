package pwcg.campaign.skirmish;

import pwcg.campaign.plane.Role;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.target.TargetType;

public class SkirmishProfileElement
{
    private SkirmishProfileAirAssociation association;
    private Role role;
    private FlightTypes preferredFlightType;
    private TargetType targetType;

    public SkirmishProfileAirAssociation getAssociation()
    {
        return association;
    }

    public Role getRole()
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
