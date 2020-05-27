package pwcg.mission.flight.cap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.OpposingSquadronChooser;
import pwcg.mission.flight.IFlightInformation;

public class CAPOpposingFlightSquadronChooser
{
    private IFlightInformation playerFlightInformation;

    public CAPOpposingFlightSquadronChooser(IFlightInformation playerFlightInformation)
    {
        this.playerFlightInformation = playerFlightInformation;
    }

    public Squadron getOpposingSquadrons() throws PWCGException
    {
        List<Role> opposingFlightRoles = determineOpposingRoles();
        OpposingSquadronChooser opposingSquadronChooser = new OpposingSquadronChooser(playerFlightInformation, opposingFlightRoles, 1);
        List<Squadron> selectedOpposingSquadrons = opposingSquadronChooser.getOpposingSquadrons();
        if (selectedOpposingSquadrons.size() > 0)
        {
            return selectedOpposingSquadrons.get(0);
        }
        return null;
    }

    private List<Role> determineOpposingRoles()
    {
        return new ArrayList<>(Arrays.asList(Role.ROLE_ATTACK));
    }
}
