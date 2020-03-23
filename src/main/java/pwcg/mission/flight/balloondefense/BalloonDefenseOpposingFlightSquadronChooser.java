package pwcg.mission.flight.balloondefense;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.OpposingSquadronChooser;
import pwcg.mission.flight.IFlightInformation;

public class BalloonDefenseOpposingFlightSquadronChooser
{
    private IFlightInformation playerFlightInformation;

    public BalloonDefenseOpposingFlightSquadronChooser(IFlightInformation playerFlightInformation)
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
        int twoFlightsDiceRoll = RandomNumberGenerator.getRandom(100);
        if (twoFlightsDiceRoll < 10)
        {
            numOpposingFlights = 2;
        }
        return numOpposingFlights;
    }
}
