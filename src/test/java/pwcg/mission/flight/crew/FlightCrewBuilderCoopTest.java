package pwcg.mission.flight.crew;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.MissionProfile;
import pwcg.mission.MissionSquadronFlightTypes;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.NecessaryFlightType;
import pwcg.mission.playerunit.crew.UnitCrewBuilder;
import pwcg.testutils.CampaignCache;
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
        coopCampaign = CampaignCache.makeCampaign(SquadronTestProfile.COOP_COMPETITIVE_PROFILE);
        PWCGContext.getInstance().setCampaign(coopCampaign);
    }

    @Test
    public void testOneOfTwoPlayerFlightGeneration() throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
    	for (CrewMember player : coopCampaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList())
    	{
    		if (player.getName().contentEquals("Squadron Mate"))
    		{
    			participatingPlayers.addCrewMember(player);
    		}
    	}
    	
        MissionSquadronFlightTypes playerFlightTypes = new MissionSquadronFlightTypes();
    	for (CrewMember player : participatingPlayers.getAllParticipatingPlayers())
    	{
    	    playerFlightTypes.add(player.determineSquadron(), FlightTypes.GROUND_ATTACK);
    	}
    	
        MissionGenerator missionGenerator = new MissionGenerator(coopCampaign);
        Mission mission = missionGenerator.makeTestCoopMissionFromFlightType(participatingPlayers, playerFlightTypes, MissionProfile.DAY_TACTICAL_MISSION);
        
        FlightInformation flightInformation = new FlightInformation(mission, NecessaryFlightType.PLAYER_FLIGHT);
        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(SquadronTestProfile.COOP_COMPETITIVE_PROFILE.getSquadronId());
        flightInformation.setCompany(squadron);
        
        UnitCrewBuilder flightCrewBuilder = new UnitCrewBuilder(flightInformation);
        List<CrewMember> assignedCrewMap = flightCrewBuilder.createCrewAssignmentsForFlight(4);
        
        boolean playerFound = false;
        boolean playerShouldNotBeFound = true;
        CompanyPersonnel squadronPersonnel = coopCampaign.getPersonnelManager().getCompanyPersonnel(SquadronTestProfile.COOP_COMPETITIVE_PROFILE.getSquadronId());        
        for (CrewMember crew : assignedCrewMap)
        {
            assert(squadronPersonnel.isActiveCrewMember(crew.getSerialNumber()));
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
    	for (CrewMember player : coopCampaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList())
    	{
    		if (player.getCompanyId() == SquadronTestProfile.COOP_COMPETITIVE_PROFILE.getSquadronId())
    		{
    			participatingPlayers.addCrewMember(player);
    		}
    	}
        
        List<FlightTypes> playerFlightTypeList = makeFlightTypes(participatingPlayers);
        MissionSquadronFlightTypes playerFlightTypes = new MissionSquadronFlightTypes();
        for (int i = 0; i < participatingPlayers.getParticipatingSquadronIds().size(); ++i)
        {
            Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(participatingPlayers.getParticipatingSquadronIds().get(i));
            playerFlightTypes.add(squadron, playerFlightTypeList.get(i));
        }

        MissionGenerator missionGenerator = new MissionGenerator(coopCampaign);
        Mission mission = missionGenerator.makeTestCoopMissionFromFlightType(participatingPlayers, playerFlightTypes, MissionProfile.DAY_TACTICAL_MISSION);

        FlightInformation flightInformation = new FlightInformation(mission, NecessaryFlightType.PLAYER_FLIGHT);
        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(SquadronTestProfile.COOP_COMPETITIVE_PROFILE.getSquadronId());
        flightInformation.setCompany(squadron);
        
        UnitCrewBuilder flightCrewBuilder = new UnitCrewBuilder(flightInformation);
        List<CrewMember> assignedCrewMap = flightCrewBuilder.createCrewAssignmentsForFlight(4);
        
        boolean player1Found = false;
        boolean player2Found = false;
        CompanyPersonnel squadronPersonnel = coopCampaign.getPersonnelManager().getCompanyPersonnel(SquadronTestProfile.COOP_COMPETITIVE_PROFILE.getSquadronId());        
        for (CrewMember crew : assignedCrewMap)
        {
            assert(squadronPersonnel.isActiveCrewMember(crew.getSerialNumber()));
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
    	for (CrewMember player : coopCampaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList())
    	{
    		if (player.getCompanyId() == 10131132)
    		{
    			participatingPlayers.addCrewMember(player);
    		}
    	}
        
        List<FlightTypes> playerFlightTypeList = makeFlightTypes(participatingPlayers);
        MissionSquadronFlightTypes playerFlightTypes = new MissionSquadronFlightTypes();
        for (int i = 0; i < participatingPlayers.getParticipatingSquadronIds().size(); ++i)
        {
            Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(participatingPlayers.getParticipatingSquadronIds().get(i));
            playerFlightTypes.add(squadron, playerFlightTypeList.get(i));
        }

        MissionGenerator missionGenerator = new MissionGenerator(coopCampaign);
        Mission mission = missionGenerator.makeTestCoopMissionFromFlightType(participatingPlayers, playerFlightTypes, MissionProfile.DAY_TACTICAL_MISSION);

        FlightInformation flightInformation = new FlightInformation(mission, NecessaryFlightType.PLAYER_FLIGHT);
        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(10131132);
        flightInformation.setCompany(squadron);
        
        UnitCrewBuilder flightCrewBuilder = new UnitCrewBuilder(flightInformation);
        List<CrewMember> assignedCrewMap = flightCrewBuilder.createCrewAssignmentsForFlight(4);
        
        boolean player1Found = false;
        boolean player2Found = false;
        CompanyPersonnel squadronPersonnel = coopCampaign.getPersonnelManager().getCompanyPersonnel(10131132);        
        for (CrewMember crew : assignedCrewMap)
        {
            assert(squadronPersonnel.isActiveCrewMember(crew.getSerialNumber()));
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
