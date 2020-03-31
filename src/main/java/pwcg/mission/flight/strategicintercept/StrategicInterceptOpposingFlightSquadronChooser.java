package pwcg.mission.flight.strategicintercept;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.OpposingSquadronChooser;
import pwcg.mission.flight.IFlightInformation;

public class StrategicInterceptOpposingFlightSquadronChooser
{
    private IFlightInformation playerFlightInformation;

    public StrategicInterceptOpposingFlightSquadronChooser(IFlightInformation playerFlightInformation)
    {
        this.playerFlightInformation = playerFlightInformation;
    }

    public Squadron getOpposingBomberSquadron() throws PWCGException
    {
        List<Role> opposingFlightRoles = new ArrayList<>(Arrays.asList(Role.ROLE_BOMB));
        OpposingSquadronChooser opposingSquadronChooser = new OpposingSquadronChooser(playerFlightInformation, opposingFlightRoles, 1);
        List<Squadron> opposingSquadronss = opposingSquadronChooser.getOpposingSquadrons();
        return pickOne(opposingSquadronss);
    }

    private Squadron pickOne(List<Squadron> opposingSquadrons)
    {
        if (opposingSquadrons.isEmpty())
        {
            return null;
        }
        
        int index = RandomNumberGenerator.getRandom(opposingSquadrons.size());
        return opposingSquadrons.get(index);
    }
}
