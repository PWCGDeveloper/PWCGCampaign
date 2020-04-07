package pwcg.mission.flight.strategicintercept;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.OpposingSquadronChooser;
import pwcg.mission.flight.IFlightInformation;

public class StrategicInterceptOpposingFlightSquadronChooser
{
    private IFlightInformation playerFlightInformation;

    public StrategicInterceptOpposingFlightSquadronChooser(IFlightInformation playerFlightInformation)
    {
        this.playerFlightInformation = playerFlightInformation;
    }

    public List<Squadron> getOpposingBomberSquadron() throws PWCGException
    {
        List<Role> opposingFlightRoles = new ArrayList<>(Arrays.asList(Role.ROLE_BOMB));
        OpposingSquadronChooser opposingSquadronChooser = new OpposingSquadronChooser(playerFlightInformation, opposingFlightRoles, 5);
        List<Squadron> opposingSquadrons = opposingSquadronChooser.getOpposingSquadrons();
        return opposingSquadrons;
    }
}
