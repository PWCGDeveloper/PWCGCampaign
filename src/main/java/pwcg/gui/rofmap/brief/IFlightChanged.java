package pwcg.gui.rofmap.brief;

import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public interface IFlightChanged
{
    void flightChanged(Squadron squadron) throws PWCGException;
}
