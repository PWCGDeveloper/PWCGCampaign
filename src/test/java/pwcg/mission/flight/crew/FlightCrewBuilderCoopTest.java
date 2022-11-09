package pwcg.mission.flight.crew;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.MissionProfile;
import pwcg.mission.MissionSquadronFlightTypes;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.NecessaryFlightType;
import pwcg.testutils.TestCampaignFactoryBuilder;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FlightCrewBuilderCoopTest 
{
    private static Campaign coopCampaign;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        coopCampaign = TestCampaignFactoryBuilder.makeCampaign(this.getClass().getCanonicalName(), SquadronTestProfile.COOP_COMPETITIVE_PROFILE);
    }

    @Test
    public void testOneOfTwoPlayerFlightGeneration() throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
    	for (SquadronMember player : coopCampaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList())
    	{
    		if (player.getName().contentEquals("Squadron Mate"))
    		{
    			participatingPlayers.addSquadronMember(player);
    		}
    	}
    	
        MissionSquadronFlightTypes playerFlightTypes = new MissionSquadronFlightTypes();
    	for (SquadronMember player : participatingPlayers.getAllParticipatingPlayers())
    	{
    	    playerFlightTypes.add(player.determineSquadron(), FlightTypes.GROUND_ATTACK);
    	}
    	
        MissionGenerator missionGenerator = new MissionGenerator(coopCampaign);
        Mission mission = missionGenerator.makeTestCoopMissionFromFlightType(participatingPlayers, playerFlightTypes, MissionProfile.DAY_TACTICAL_MISSION);
        
        FlightInformation flightInformation = new FlightInformation(mission, NecessaryFlightType.PLAYER_FLIGHT);
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(SquadronTestProfile.COOP_COMPETITIVE_PROFILE.getSquadronId());
        flightInformation.setSquadron(squadron);
        
        FlightCrewBuilder flightCrewBuilder = new FlightCrewBuilder(flightInformation);
        List<SquadronMember> assignedCrewMap = flightCrewBuilder.createCrewAssignmentsForFlight(4);
        
        boolean playerFound = false;
        boolean playerShouldNotBeFound = true;
        SquadronPersonnel squadronPersonnel = coopCampaign.getPersonnelManager().getSquadronPersonnel(SquadronTestProfile.COOP_COMPETITIVE_PROFILE.getSquadronId());        
        for (SquadronMember crew : assignedCrewMap)
        {
            assert(squadronPersonnel.isActiveSquadronMember(crew.getSerialNumber()));
            if (crew.getName().equals("Squadron Mate"))
            {
            	playerFound = true;
            }
            else if (crew.isPlayer())
            {
            	playerShouldNotBeFound = false;
            }
        }

        assert(playerFound && playerShouldNotBeFound);
    }

    @Test
    public void testTwoPlayerFlightGeneration() throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
    	for (SquadronMember player : coopCampaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList())
    	{
    		if (player.getSquadronId() == SquadronTestProfile.COOP_COMPETITIVE_PROFILE.getSquadronId())
    		{
    			participatingPlayers.addSquadronMember(player);
    		}
    	}
        
        List<FlightTypes> playerFlightTypeList = makeFlightTypes(participatingPlayers);
        MissionSquadronFlightTypes playerFlightTypes = new MissionSquadronFlightTypes();
        for (int i = 0; i < participatingPlayers.getParticipatingSquadronIds().size(); ++i)
        {
            Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(participatingPlayers.getParticipatingSquadronIds().get(i));
            playerFlightTypes.add(squadron, playerFlightTypeList.get(i));
        }

        MissionGenerator missionGenerator = new MissionGenerator(coopCampaign);
        Mission mission = missionGenerator.makeTestCoopMissionFromFlightType(participatingPlayers, playerFlightTypes, MissionProfile.DAY_TACTICAL_MISSION);

        FlightInformation flightInformation = new FlightInformation(mission, NecessaryFlightType.PLAYER_FLIGHT);
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(SquadronTestProfile.COOP_COMPETITIVE_PROFILE.getSquadronId());
        flightInformation.setSquadron(squadron);
        
        FlightCrewBuilder flightCrewBuilder = new FlightCrewBuilder(flightInformation);
        List<SquadronMember> assignedCrewMap = flightCrewBuilder.createCrewAssignmentsForFlight(4);
        
        boolean player1Found = false;
        boolean player2Found = false;
        SquadronPersonnel squadronPersonnel = coopCampaign.getPersonnelManager().getSquadronPersonnel(SquadronTestProfile.COOP_COMPETITIVE_PROFILE.getSquadronId());        
        for (SquadronMember crew : assignedCrewMap)
        {
            assert(squadronPersonnel.isActiveSquadronMember(crew.getSerialNumber()));
            if (crew.getSerialNumber() == participatingPlayers.getAllParticipatingPlayers().get(0).getSerialNumber())
            {
                player1Found = true;
            }
            if (crew.getSerialNumber() == participatingPlayers.getAllParticipatingPlayers().get(1).getSerialNumber())
            {
                player2Found = true;
            }
        }

        assert(player1Found && player2Found);
    }
    
    private List<FlightTypes> makeFlightTypes(MissionHumanParticipants participatingPlayers)
    {
        List<FlightTypes> playerFlightTypes = new ArrayList<>();
        for (int i = 0; i < participatingPlayers.getParticipatingSquadronIds().size(); ++i)
        {
            playerFlightTypes.add(FlightTypes.GROUND_ATTACK);
        }
        return playerFlightTypes;
    }
    

    @Test
    public void testTwoPlayerEnemySquadronFlightGeneration() throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
    	for (SquadronMember player : coopCampaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList())
    	{
    		if (player.getSquadronId() == 10131132)
    		{
    			participatingPlayers.addSquadronMember(player);
    		}
    	}
        
        List<FlightTypes> playerFlightTypeList = makeFlightTypes(participatingPlayers);
        MissionSquadronFlightTypes playerFlightTypes = new MissionSquadronFlightTypes();
        for (int i = 0; i < participatingPlayers.getParticipatingSquadronIds().size(); ++i)
        {
            Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(participatingPlayers.getParticipatingSquadronIds().get(i));
            playerFlightTypes.add(squadron, playerFlightTypeList.get(i));
        }

        MissionGenerator missionGenerator = new MissionGenerator(coopCampaign);
        Mission mission = missionGenerator.makeTestCoopMissionFromFlightType(participatingPlayers, playerFlightTypes, MissionProfile.DAY_TACTICAL_MISSION);

        FlightInformation flightInformation = new FlightInformation(mission, NecessaryFlightType.PLAYER_FLIGHT);
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(10131132);
        flightInformation.setSquadron(squadron);
        
        FlightCrewBuilder flightCrewBuilder = new FlightCrewBuilder(flightInformation);
        List<SquadronMember> assignedCrewMap = flightCrewBuilder.createCrewAssignmentsForFlight(4);
        
        boolean player1Found = false;
        boolean player2Found = false;
        SquadronPersonnel squadronPersonnel = coopCampaign.getPersonnelManager().getSquadronPersonnel(10131132);        
        for (SquadronMember crew : assignedCrewMap)
        {
            assert(squadronPersonnel.isActiveSquadronMember(crew.getSerialNumber()));
            if (crew.getSerialNumber() == participatingPlayers.getAllParticipatingPlayers().get(0).getSerialNumber())
            {
                player1Found = true;
            }
            if (crew.getSerialNumber() == participatingPlayers.getAllParticipatingPlayers().get(1).getSerialNumber())
            {
                player2Found = true;
            }
        }

        assert(player1Found && player2Found);
    }

}
