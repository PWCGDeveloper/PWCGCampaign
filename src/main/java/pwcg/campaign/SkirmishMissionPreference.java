package pwcg.campaign;

import pwcg.campaign.api.Side;
import pwcg.campaign.plane.Role;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.target.TargetType;

public class SkirmishMissionPreference
{
    private Side side;
    private Role role;
    private FlightTypes preferredFlightType;
    private TargetType targetType;

	public SkirmishMissionPreference()
	{
	}

    public Side getSide()
    {
        return side;
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
