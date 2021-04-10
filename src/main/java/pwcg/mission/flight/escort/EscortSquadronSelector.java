package pwcg.mission.flight.escort;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;

public class EscortSquadronSelector
{

    public static Squadron getEscortSquadron(IFlight flight, Side friendlySide) throws PWCGException
    {
        List<Role> escortRole = new ArrayList<>();
        escortRole.add(Role.ROLE_FIGHTER);
        Squadron friendlyFighterSquadron = PWCGContext.getInstance().getSquadronManager().getEscortOrEscortedSquadron(flight.getCampaign(), flight.getMission().getMissionBorders().getCenter(), escortRole, friendlySide);
        return friendlyFighterSquadron;
    }
}
