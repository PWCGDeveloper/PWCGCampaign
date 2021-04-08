package pwcg.mission.flight.scramble;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetDefinition;

public class AirfieldAttackScrambleFlightBuilder
{
    private Campaign campaign;
    private Mission mission;
    private TargetDefinition targetDefinition;

    public AirfieldAttackScrambleFlightBuilder(IFlight groundAttackFlight)
    {
        this.targetDefinition = groundAttackFlight.getTargetDefinition();
        this.mission = groundAttackFlight.getMission();
        this.campaign = groundAttackFlight.getCampaign();
    }

    public IFlight createOpposingFlight() throws PWCGException
    {
        Squadron opposingSquadron = getOpposingSquadrons();            
        return createOpposingFlight(opposingSquadron);
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
