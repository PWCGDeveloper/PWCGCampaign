package pwcg.campaign;

import pwcg.campaign.api.Side;
import pwcg.campaign.plane.Role;
import pwcg.mission.flight.FlightTypes;

public class SkirmishMissionPreference
{
    private Side side;
    private Role forRole;
    private FlightTypes preferredFlightType;

	public SkirmishMissionPreference()
	{
	}

    public Side getSide()
    {
        return side;
    }

    public Role getForRole()
    {
        return forRole;
    }

    public FlightTypes getPreferredFlightType()
    {
        return preferredFlightType;
    }

}
