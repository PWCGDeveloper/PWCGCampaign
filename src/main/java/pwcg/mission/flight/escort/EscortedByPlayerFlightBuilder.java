package pwcg.mission.flight.escort;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilder;


public class EscortedByPlayerFlightBuilder
{
    private FlightInformation playerEscortFlightInformation;
    private TargetDefinition playerEscortTargetDefinition;
    private FlightInformation escortedFlightInformation;
    private TargetDefinition escortedTargetDefinition;

	public EscortedByPlayerFlightBuilder(FlightInformation playerEscortFlightInformation, TargetDefinition playerEscortTargetDefinition)
    {
        this.playerEscortFlightInformation = playerEscortFlightInformation;
        this.playerEscortTargetDefinition = playerEscortTargetDefinition;
    }

    public EscortedByPlayerFlight createEscortedFlight() throws PWCGException
    {
        MissionBeginUnit missionBeginUnit = buildEscortedFlightInformation();
        EscortedByPlayerFlight playerEscortedFlightEscortedByPlayer = buildEscortedFlight(missionBeginUnit);        
        return playerEscortedFlightEscortedByPlayer;
	}

    private EscortedByPlayerFlight buildEscortedFlight(MissionBeginUnit missionBeginUnit) throws PWCGException
    {
        EscortedByPlayerFlight playerEscortedFlight = new EscortedByPlayerFlight (escortedFlightInformation, escortedTargetDefinition);
		playerEscortedFlight.createFlight();
        
        return playerEscortedFlight;
    }

    private MissionBeginUnit buildEscortedFlightInformation() throws PWCGException
    {
        Squadron friendlyBomberSquadron = determineSquadronToBeEscorted(
                playerEscortFlightInformation.getCampaign(), playerEscortFlightInformation.getSquadron());
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit(friendlyBomberSquadron.determineCurrentPosition(playerEscortFlightInformation.getCampaign().getDate()));     
        
        this.escortedFlightInformation = EscortedByPlayerFlightInformationBuilder.buildEscortedByPlayerFlightInformation(
                playerEscortFlightInformation, playerEscortTargetDefinition, friendlyBomberSquadron);
        
        this.escortedTargetDefinition = buildTargetDefintion(escortedFlightInformation);

        return missionBeginUnit;
    }
    
    private Squadron determineSquadronToBeEscorted(Campaign campaign, Squadron escortSquadron) throws PWCGException
    {
        Squadron escortedSquadron = EscortSquadronSelector.getEscortedSquadron(campaign, playerEscortFlightInformation.getSquadron());

        if (escortedSquadron == null)
        {
            throw new PWCGMissionGenerationException ("Escort mission with no viable squadrons to be escorted - please create another mission");
        }
        
        return escortedSquadron;
    }

    private TargetDefinition buildTargetDefintion(FlightInformation escortedFlightInformation) throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilder(escortedFlightInformation);
        return targetDefinitionBuilder.buildTargetDefinition();
    }
}
