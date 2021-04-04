package pwcg.mission.flight.escort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.MissionSquadronRecorder;
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
        playerEscortFlightInformation.getMission().getMissionSquadronRecorder().registerSquadronInUse(friendlyBomberSquadron);
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit(friendlyBomberSquadron.determineCurrentPosition(playerEscortFlightInformation.getCampaign().getDate()));     
        
        this.escortedFlightInformation = EscortedByPlayerFlightInformationBuilder.buildEscortedByPlayerFlightInformation(
                playerEscortFlightInformation, playerEscortTargetDefinition, friendlyBomberSquadron);
        
        this.escortedTargetDefinition = buildTargetDefintion(escortedFlightInformation);

        return missionBeginUnit;
    }
    
    private Squadron determineSquadronToBeEscorted(Campaign campaign, Squadron escortSquadron) throws PWCGException
    {
        Squadron escortedSquadron = determineSquadronForRoleToBeEscorted(campaign, escortSquadron, Role.ROLE_BOMB);
        if (escortedSquadron == null)
        {
            escortedSquadron = determineSquadronForRoleToBeEscorted(campaign, escortSquadron, Role.ROLE_ATTACK);
        }

        if (escortedSquadron == null)
        {
            throw new PWCGMissionGenerationException ("Escort mission with no viable squadrons to be escorted - please create another mission");
        }
        
        return escortedSquadron;
    }
    
    private Squadron determineSquadronForRoleToBeEscorted(Campaign campaign, Squadron escortSquadron, Role role) throws PWCGException
    {
        List<Role> bomberRole = new ArrayList<Role>(Arrays.asList(role));
        Side friendlySide = escortSquadron.determineSide();
        MissionSquadronRecorder squadronInUseRecorder = playerEscortFlightInformation.getMission().getMissionSquadronRecorder();
        List<Squadron> squadronsToExclude = squadronInUseRecorder.getSquadronsInUse();
        Squadron escortedSquadron = PWCGContext.getInstance().getSquadronManager().getSingleViableAiSquadronByRoleAndSideAndCurrentMap(campaign, bomberRole, friendlySide, squadronsToExclude);
        return escortedSquadron;
    }

    private TargetDefinition buildTargetDefintion(FlightInformation escortedFlightInformation) throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilder(escortedFlightInformation);
        return targetDefinitionBuilder.buildTargetDefinition();
    }
}
