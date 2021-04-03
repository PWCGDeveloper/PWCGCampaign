package pwcg.campaign;

import pwcg.campaign.plane.Role;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.target.TargetType;

public class SkirmishProfileElement
{
    private SkirmishProfileAssociation association;
    private Role role;
    private FlightTypes preferredFlightType;
    private TargetType targetType;

    public SkirmishProfileAssociation getAssociation()
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
