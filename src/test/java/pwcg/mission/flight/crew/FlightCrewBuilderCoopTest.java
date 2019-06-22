package pwcg.mission.flight.crew;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadrontTestProfile;
import pwcg.testutils.TestParticipatingHumanBuilder;

@RunWith(MockitoJUnitRunner.class)
public class FlightCrewBuilderCoopTest 
{
    Campaign coopCampaign;
    private Mission mission;

    @Before
    public void fighterFlightTests() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        coopCampaign = CampaignCache.makeCampaign(SquadrontTestProfile.COOP_PROFILE);

        CoordinateBox missionBorders = CoordinateBox.coordinateBoxFromCenter(new Coordinate(100000.0, 0.0, 100000.0), 75000);
        mission = new Mission(coopCampaign, TestParticipatingHumanBuilder.buildTestParticipatingHumans(coopCampaign), missionBorders);
        mission.generate(FlightTypes.GROUND_ATTACK);
    }

    @Test
    public void testOneOfTwoPlayerFlightGeneration() throws PWCGException
    {
    	List<SquadronMember> participatingPlayers = new ArrayList<>();
    	for (SquadronMember player : coopCampaign.getPersonnelManager().getAllPlayers().getSquadronMemberList())
    	{
    		if (player.getName().contentEquals("Squadron Mate"))
    		{
    			participatingPlayers.add(player);
    		}
    	}
    	
        FlightInformation flightInformation = new FlightInformation(mission);
        Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(SquadrontTestProfile.COOP_PROFILE.getSquadronId());
        flightInformation.setSquadron(squadron);
        
        FlightCrewBuilder flightCrewBuilder = new FlightCrewBuilder(flightInformation);
        List<SquadronMember> assignedCrewMap = flightCrewBuilder.createCrewAssignmentsForFlight(4);
        
        boolean playerFound = false;
        boolean playerShouldNotBeFound = true;
        SquadronPersonnel squadronPersonnel = coopCampaign.getPersonnelManager().getSquadronPersonnel(SquadrontTestProfile.COOP_PROFILE.getSquadronId());        
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
    	List<SquadronMember> participatingPlayers = new ArrayList<>();
    	for (SquadronMember player : coopCampaign.getPersonnelManager().getAllPlayers().getSquadronMemberList())
    	{
    		if (player.getSquadronId() == SquadrontTestProfile.COOP_PROFILE.getSquadronId())
    		{
    			participatingPlayers.add(player);
    		}
    	}
    	
        FlightInformation flightInformation = new FlightInformation(mission);
        Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(SquadrontTestProfile.COOP_PROFILE.getSquadronId());
        flightInformation.setSquadron(squadron);
        
        FlightCrewBuilder flightCrewBuilder = new FlightCrewBuilder(flightInformation);
        List<SquadronMember> assignedCrewMap = flightCrewBuilder.createCrewAssignmentsForFlight(4);
        
        boolean player1Found = false;
        boolean player2Found = false;
        SquadronPersonnel squadronPersonnel = coopCampaign.getPersonnelManager().getSquadronPersonnel(SquadrontTestProfile.COOP_PROFILE.getSquadronId());        
        for (SquadronMember crew : assignedCrewMap)
        {
            assert(squadronPersonnel.isActiveSquadronMember(crew.getSerialNumber()));
            if (crew.getSerialNumber() == participatingPlayers.get(0).getSerialNumber())
            {
                player1Found = true;
            }
            if (crew.getSerialNumber() == participatingPlayers.get(1).getSerialNumber())
            {
                player2Found = true;
            }
        }

        assert(player1Found && player2Found);
    }
    

    @Test
    public void testTwoPlayerEnemySquadronFlightGeneration() throws PWCGException
    {
    	List<SquadronMember> participatingPlayers = new ArrayList<>();
    	for (SquadronMember player : coopCampaign.getPersonnelManager().getAllPlayers().getSquadronMemberList())
    	{
    		if (player.getSquadronId() == 10131132)
    		{
    			participatingPlayers.add(player);
    		}
    	}
    	
        FlightInformation flightInformation = new FlightInformation(mission);
        Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(10131132);
        flightInformation.setSquadron(squadron);
        
        FlightCrewBuilder flightCrewBuilder = new FlightCrewBuilder(flightInformation);
        List<SquadronMember> assignedCrewMap = flightCrewBuilder.createCrewAssignmentsForFlight(4);
        
        boolean player1Found = false;
        boolean player2Found = false;
        SquadronPersonnel squadronPersonnel = coopCampaign.getPersonnelManager().getSquadronPersonnel(10131132);        
        for (SquadronMember crew : assignedCrewMap)
        {
            assert(squadronPersonnel.isActiveSquadronMember(crew.getSerialNumber()));
            if (crew.getSerialNumber() == participatingPlayers.get(0).getSerialNumber())
            {
                player1Found = true;
            }
            if (crew.getSerialNumber() == participatingPlayers.get(1).getSerialNumber())
            {
                player2Found = true;
            }
        }

        assert(player1Found && player2Found);
    }

}
