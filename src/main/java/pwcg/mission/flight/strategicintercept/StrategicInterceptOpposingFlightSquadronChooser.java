package pwcg.mission.flight.strategicintercept;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.OpposingSquadronChooser;
import pwcg.mission.flight.FlightInformation;

public class StrategicInterceptOpposingFlightSquadronChooser
{
    private FlightInformation playerFlightInformation;

    public StrategicInterceptOpposingFlightSquadronChooser(FlightInformation playerFlightInformation)
    {
        this.playerFlightInformation = playerFlightInformation;
    }

    public List<Squadron> getOpposingBomberSquadron() throws PWCGException
    {
        List<Role> opposingFlightRoles = new ArrayList<>(Arrays.asList(Role.ROLE_BOMB));
        Side opposingSquadronSide = playerFlightInformation.getSquadron().determineEnemySide();
        OpposingSquadronChooser opposingSquadronChooser = new OpposingSquadronChooser(playerFlightInformation.getCampaign(), opposingFlightRoles, opposingSquadronSide, 5);
        List<Squadron> opposingSquadrons = opposingSquadronChooser.getOpposingSquadrons();
        return opposingSquadrons;
    }
}
