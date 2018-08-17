package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.inmission.phase1.parse.AARLogEventData;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.prelim.PwcgMissionData;
import pwcg.aar.prelim.PwcgMissionDataEvaluator;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.mission.data.PwcgGeneratedMissionPlaneData;

@RunWith(MockitoJUnitRunner.class)
public class AARBotVehicleMapperTest
{
    @Mock
    private Campaign campaign;

    @Mock
    private CampaignPersonnelManager campaignPersonnelManager;

    @Mock
    private SquadronMember squadronMember;

    @Mock
    private PwcgMissionDataEvaluator pwcgMissionDataEvaluator;
    
    private TestMissionEntityGenerator testMissionEntityGenerator;
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
    }

    @Test
    public void testMappingBotToPlaneForFighters () throws PWCGException
    {        
        testInstanceMappingBotToPlaneForFIghters(0, 0, 1, 1, 3, 4);
        testInstanceMappingBotToPlaneForFIghters(2, 1, 1, 1, 3, 4);
        testInstanceMappingBotToPlaneForFIghters(0, 1, 1, 1, 3, 4);
        testInstanceMappingBotToPlaneForFIghters(2, 0, 1, 1, 3, 4);
        testInstanceMappingBotToPlaneForFIghters(2, 1, 0, 1, 3, 4);
        testInstanceMappingBotToPlaneForFIghters(2, 1, 1, 0, 3, 4);
        testInstanceMappingBotToPlaneForFIghters(2, 1, 0, 0, 3, 4);
        testInstanceMappingBotToPlaneForFIghters(2, 1, 1, 1, 0, 4);
        testInstanceMappingBotToPlaneForFIghters(2, 1, 1, 1, 3, 0);
        testInstanceMappingBotToPlaneForFIghters(2, 1, 1, 1, 0, 0);
    }

    public void testInstanceMappingBotToPlaneForFIghters(
                    int numFrenchPlanes, 
                    int numGermanPlanes,
                    int numFrenchBalloons, 
                    int numGermanBalloons,
                    int numFrenchTrucks, 
                    int numGermanTrucks) throws PWCGException
    {
        Mockito.when(campaign.getPersonnelManager()).thenReturn(campaignPersonnelManager);
        //Mockito.when(campaignPersonnelManager.getCampaignMember(Matchers.<Integer>any())).thenReturn(squadronMember);
        Mockito.when(campaignPersonnelManager.getAnyCampaignMember(Matchers.<Integer>any())).thenReturn(squadronMember);
        Mockito.when(campaignPersonnelManager.getAnyCampaignMember(Matchers.<Integer>any())).thenReturn(squadronMember);
        Mockito.when(squadronMember.isPlayer()).thenReturn(false);

        testMissionEntityGenerator = new TestMissionEntityGenerator();
        testMissionEntityGenerator.makeMissionArtifacts(numFrenchPlanes, numGermanPlanes, numFrenchBalloons, numGermanBalloons, numFrenchTrucks, numGermanTrucks);

        PwcgMissionData pwcgMissionData = new PwcgMissionData();
        List<PwcgGeneratedMissionPlaneData> missionPlanes = new ArrayList<>(testMissionEntityGenerator.getMissionPlanes().values());
        for (PwcgGeneratedMissionPlaneData missionPlane : missionPlanes)
        {
            pwcgMissionData.addMissionPlanes(missionPlane);
        }
        
        AARLogEventData logEventData = testMissionEntityGenerator.getAARLogEventData();
        AARBotVehicleMapper aarBotVehicleMapper = new AARBotVehicleMapper(logEventData);

        Map <String, LogPlane> planeAiEntities = testMissionEntityGenerator.getPlaneAiEntities();
        aarBotVehicleMapper.mapBotsToCrews(planeAiEntities);
                
        assert(planeAiEntities.size() == numFrenchPlanes + numGermanPlanes);
        for (LogPlane missionResultPlane : planeAiEntities.values())
        {
            assert(missionResultPlane.getLogPilot() != null);
            if (missionResultPlane.getId().equals("1001"))
            {
                assert(missionResultPlane.getLogPilot().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER + 1);
            }
            if (missionResultPlane.getId().equals("1002"))
            {
                assert(missionResultPlane.getLogPilot().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER + 2);
            }
            if (missionResultPlane.getId().equals("2001"))
            {
                assert(missionResultPlane.getLogPilot().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER + 100);
            }
            if (missionResultPlane.getId().equals("2002"))
            {
                assert(missionResultPlane.getLogPilot().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER + 200);
            }
        }
    }
}
