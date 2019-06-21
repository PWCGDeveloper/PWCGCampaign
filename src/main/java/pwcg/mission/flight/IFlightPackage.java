package pwcg.mission.flight;

import pwcg.core.exception.PWCGException;

public interface IFlightPackage
{
    Flight createPackage () throws PWCGException;
}
