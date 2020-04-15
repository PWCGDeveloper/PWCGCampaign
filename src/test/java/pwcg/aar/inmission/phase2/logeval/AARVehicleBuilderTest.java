package pwcg.aar.inmission.phase2.logeval;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.aar.inmission.phase1.parse.AARLogEventData;
import pwcg.aar.inmission.phase1.parse.event.IAType12;
import pwcg.aar.prelim.PwcgMissionData;
import pwcg.aar.prelim.PwcgMissionDataEvaluator;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class AARVehicleBuilderTest
{

    @Mock
    private Campaign campaign;

    @Mock
    private CampaignPersonnelManager campaignPersonnelManager;

    @Mock
    PwcgMissionData pwcgMissionData;
    
    @Mock
    private SquadronMember squadronMember;
    
    @Mock
    private AARLogEventData logEventData;

    @Mock
    private PwcgMissionDataEvaluator pwcgMissionDataEvaluator;
    
    @Mock
    private SquadronPersonnel squadronPersonnel;

    private TestMissionEntityGenerator testMissionEntityGenerator;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
    }

    @Test
    public void testSortVehicles () throws PWCGException
    {        
        runTest(2, 1, 1, 1, 3, 4);
        runTest(0, 1, 1, 1, 3, 4);
        runTest(2, 0, 1, 1, 3, 4);
        runTest(2, 1, 0, 1, 3, 4);
        runTest(2, 1, 1, 0, 3, 4);
        runTest(2, 1, 0, 0, 3, 4);
        runTest(2, 1, 1, 1, 0, 4);
        runTest(2, 1, 1, 1, 3, 0);
        runTest(2, 1, 1, 1, 0, 0);
    }

    @Test (expected = PWCGException.class)
    public void testSortVehiclesNoPlanes () throws PWCGException
    {        
        runTest(0, 0, 1, 1, 3, 4);
    }

    public void runTest(int numFrenchPlanes, int numGermanPlanes,
                    int numFrenchBalloons, int numGermanBalloons,
                    int numFrenchTrucks, int numGermanTrucks) throws PWCGException
    {
        Mockito.when(campaign.getPersonnelManager()).thenReturn(campaignPersonnelManager);
        Mockito.when(campaignPersonnelManager.getAnyCampaignMember(ArgumentMatchers.<Integer>any())).thenReturn(squadronMember);        
        Mockito.when(campaignPersonnelManager.getSquadronPersonnel(ArgumentMatchers.<Integer>any())).thenReturn(squadronPersonnel);
        Mockito.when(squadronPersonnel.getSquadronMember(ArgumentMatchers.<Integer>any())).thenReturn(squadronMember);
        
        Mockito.when(squadronMember.isPlayer()).thenReturn(false);

        testMissionEntityGenerator = new TestMissionEntityGenerator();
        testMissionEntityGenerator.makeMissionArtifacts(numFrenchPlanes, numGermanPlanes, numFrenchBalloons, numGermanBalloons, numFrenchTrucks, numGermanTrucks);
        
        List<IAType12> vehiclesSpawnEvents = testMissionEntityGenerator.getVehicles();
        Mockito.when(logEventData.getVehicles()).thenReturn(vehiclesSpawnEvents);
        
        AARVehicleBuilder aarVehicleBuilder = createAARVehicleBuilder(); 
        aarVehicleBuilder.buildVehicleListsByVehicleType(logEventData);
        
        assert(aarVehicleBuilder.getLogPlanes().size() == numFrenchPlanes + numGermanPlanes);
        assert(aarVehicleBuilder.getLogBalloons().size() == numFrenchBalloons + numGermanBalloons);
        assert(aarVehicleBuilder.getLogGroundUNits().size() == numFrenchTrucks + numGermanTrucks);
    }
    
    
    private AARVehicleBuilder createAARVehicleBuilder() throws PWCGException
    {
        AARBotVehicleMapper botPlaneMapper = new AARBotVehicleMapper(logEventData);
        AARVehiclePlaneLanded landedMapper = new AARVehiclePlaneLanded(logEventData);

        Mockito.when(pwcgMissionDataEvaluator.wasPilotAssignedToMissionByName("French PilotA")).thenReturn(true);
        Mockito.when(pwcgMissionDataEvaluator.wasPilotAssignedToMissionByName("French PilotB")).thenReturn(true);
        Mockito.when(pwcgMissionDataEvaluator.wasPilotAssignedToMissionByName("German PilotA")).thenReturn(true);
        Mockito.when(pwcgMissionDataEvaluator.wasPilotAssignedToMissionByName("German PilotB")).thenReturn(true);
        
        Mockito.when(pwcgMissionDataEvaluator.getPlaneForPilotByName("French PilotA")).thenReturn(testMissionEntityGenerator.getMissionPlane(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1));
        Mockito.when(pwcgMissionDataEvaluator.getPlaneForPilotByName("French PilotB")).thenReturn(testMissionEntityGenerator.getMissionPlane(SerialNumber.AI_STARTING_SERIAL_NUMBER + 2));
        Mockito.when(pwcgMissionDataEvaluator.getPlaneForPilotByName("German PilotA")).thenReturn(testMissionEntityGenerator.getMissionPlane(SerialNumber.AI_STARTING_SERIAL_NUMBER + 100));
        Mockito.when(pwcgMissionDataEvaluator.getPlaneForPilotByName("German PilotB")).thenReturn(testMissionEntityGenerator.getMissionPlane(SerialNumber.AI_STARTING_SERIAL_NUMBER + 200));

        return new AARVehicleBuilder(botPlaneMapper, landedMapper, pwcgMissionDataEvaluator);
    }

}
