package pwcg.mission.flight.crew;

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
import pwcg.mission.MissionProfile;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.NecessaryFlightType;
import pwcg.mission.playerunit.crew.UnitCrewBuilder;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FlightCrewBuilderTest 
{
    private static Mission mission;
    private Campaign campaign;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.KG53_PROFILE);

        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        mission = missionGenerator.makeTestSingleMissionFromFlightType(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign), FlightTypes.GROUND_ATTACK, MissionProfile.DAY_TACTICAL_MISSION);
    }

    @Test
    public void testPlayerFlightGeneration() throws PWCGException
    {
        FlightInformation flightInformation = new FlightInformation(mission, NecessaryFlightType.PLAYER_FLIGHT);
        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(SquadronTestProfile.KG53_PROFILE.getCompanyId());
        flightInformation.setCompany(squadron);
        
        UnitCrewBuilder flightCrewBuilder = new UnitCrewBuilder(flightInformation);
        List<CrewMember> assignedCrewMap = flightCrewBuilder.createCrewAssignmentsForFlight(4);
        
        List<CrewMember> players = campaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList();
        boolean playerFound = false;
        CompanyPersonnel squadronPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(SquadronTestProfile.KG53_PROFILE.getCompanyId());        
        for (CrewMember crew : assignedCrewMap)
        {
            assert(squadronPersonnel.isActiveCrewMember(crew.getSerialNumber()));
            if (crew.getSerialNumber() == players.get(0).getSerialNumber())
            {
                playerFound = true;
            }
        }

        assert(playerFound);
    }

    @Test
    public void testAiFlightGeneration() throws PWCGException
    {
        FlightInformation flightInformation = new FlightInformation(mission, NecessaryFlightType.PLAYER_FLIGHT);
        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(20111052);
        flightInformation.setCompany(squadron);

        UnitCrewBuilder flightCrewBuilder = new UnitCrewBuilder(flightInformation);
        List<CrewMember> assignedCrewMap = flightCrewBuilder.createCrewAssignmentsForFlight(4);
        
        List<CrewMember> players = campaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList();
        CompanyPersonnel squadronPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(squadron.getCompanyId());        
        boolean playerFound = false;
        for (CrewMember crew : assignedCrewMap)
        {
            assert(squadronPersonnel.isActiveCrewMember(crew.getSerialNumber()));
            if (crew.getSerialNumber() == players.get(0).getSerialNumber())
            {
                playerFound = true;
            }
        }
        assert(!playerFound);
    }
}
