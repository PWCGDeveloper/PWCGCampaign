package pwcg.mission.flight.balloonBust;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.OpposingSquadronChooser;
import pwcg.mission.flight.IFlightInformation;

public class BalloonBustOpposingFlightSquadronChooser
{
    private IFlightInformation playerFlightInformation;

    public BalloonBustOpposingFlightSquadronChooser(IFlightInformation playerFlightInformation)
    {
        this.playerFlightInformation = playerFlightInformation;
    }

    public List<Squadron> getOpposingSquadrons() throws PWCGException
    {
        int numSquadronsToGet = determineNumberOfOpposingFlights();
        List<Role> opposingFlightRoles = determineOpposingRoles();
        OpposingSquadronChooser opposingSquadronChooser = new OpposingSquadronChooser(playerFlightInformation, opposingFlightRoles, numSquadronsToGet);
        return opposingSquadronChooser.getOpposingSquadrons();            
    }

    private List<Role> determineOpposingRoles()
    {
        return new ArrayList<>(Arrays.asList(Role.ROLE_FIGHTER));
    }

    private int determineNumberOfOpposingFlights() 
    {
        int numOpposingFlights = 1;
        return numOpposingFlights;
    }
}
