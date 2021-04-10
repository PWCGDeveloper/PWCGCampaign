package pwcg.mission.flight.scramble;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;

public class AirfieldAttackScrambleFlightBuilder
{
    private Campaign campaign;
    private Mission mission;
    private TargetDefinition targetDefinition;

    public static void addAirfieldScrambleToFlight(IFlight flight) throws PWCGException
    {
        if (flight.isPlayerFlight())
        {
            if (flight.getTargetDefinition().getTargetType() == TargetType.TARGET_AIRFIELD)
            {
                AirfieldAttackScrambleFlightBuilder scrambleFlightBuilder = new AirfieldAttackScrambleFlightBuilder(flight);
                IFlight opposingScrambleFlight = scrambleFlightBuilder.createOpposingFlight();
                if (opposingScrambleFlight != null)
                {
                    flight.getLinkedFlights().addLinkedFlight(opposingScrambleFlight);
                }
            }
        }

    }
    
    private AirfieldAttackScrambleFlightBuilder(IFlight groundAttackFlight)
    {
        this.targetDefinition = groundAttackFlight.getTargetDefinition();
        this.mission = groundAttackFlight.getMission();
        this.campaign = groundAttackFlight.getCampaign();
    }

    private IFlight createOpposingFlight() throws PWCGException
    {
        Squadron opposingSquadron = getOpposingSquadrons();
        if (opposingSquadron != null)
        {
            return createOpposingFlight(opposingSquadron);
        }
        else
        {
            return null;
        }
    }

    private Squadron getOpposingSquadrons() throws PWCGException
    {
        Squadron squadronForAirfield = PWCGContext.getInstance().getSquadronManager().getClosestSquadron(targetDefinition.getPosition(), campaign.getDate());
        return squadronForAirfield;
    }

    private IFlight createOpposingFlight(Squadron opposingSquadron) throws PWCGException
    {
        FlightInformation flightInformation = AirfieldAttackScrambleFlightInformationBuilder.buildAiGroundAttackOpposingFlightInformation(mission, opposingSquadron, targetDefinition.getPosition());
        AiScrambleFlight aiScrambleFlight = new AiScrambleFlight (flightInformation, targetDefinition);
        aiScrambleFlight.createFlight();
        return aiScrambleFlight;
    }    
}
