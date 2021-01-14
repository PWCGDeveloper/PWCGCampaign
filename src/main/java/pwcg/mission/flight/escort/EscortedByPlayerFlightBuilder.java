package pwcg.mission.flight.escort;

import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetBuilder;


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
        Squadron friendlyBomberSquadron = playerEscortFlightInformation.getMission().getMissionSquadronChooser().determineSquadronToBeEscorted(
                playerEscortFlightInformation.getCampaign(), playerEscortFlightInformation.getSquadron());
        playerEscortFlightInformation.getMission().getMissionSquadronChooser().registerSquadronInUse(friendlyBomberSquadron);
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit(friendlyBomberSquadron.determineCurrentPosition(playerEscortFlightInformation.getCampaign().getDate()));     
        
        this.escortedFlightInformation = EscortedByPlayerFlightInformationBuilder.buildEscortedByPlayerFlightInformation(
                playerEscortFlightInformation, playerEscortTargetDefinition, friendlyBomberSquadron);
        
        this.escortedTargetDefinition = buildTargetDefintion(escortedFlightInformation);

        return missionBeginUnit;
    }

    private TargetDefinition buildTargetDefintion(FlightInformation escortedFlightInformation) throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = new TargetBuilder(escortedFlightInformation);
        return targetDefinitionBuilder.buildTargetDefinition();
    }
}
