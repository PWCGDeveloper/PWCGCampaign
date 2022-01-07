package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.prelim.PwcgMissionData;
import pwcg.aar.prelim.PwcgMissionDataEvaluator;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogEventData;
import pwcg.mission.data.PwcgGeneratedMissionVehicleData;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AARBotVehicleMapperTest
{
    @Mock
    private Campaign campaign;

    @Mock
    private CampaignPersonnelManager campaignPersonnelManager;

    @Mock
    private CrewMember crewMember;

    @Mock
    private PwcgMissionDataEvaluator pwcgMissionDataEvaluator;
    
    private TestMissionEntityGenerator testMissionEntityGenerator;
    
    public AARBotVehicleMapperTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
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
        //Mockito.when(campaignPersonnelManager.getCampaignMember(ArgumentMatchers.<Integer>any())).thenReturn(crewMember);
        Mockito.when(campaignPersonnelManager.getAnyCampaignMember(ArgumentMatchers.<Integer>any())).thenReturn(crewMember);
        Mockito.when(campaignPersonnelManager.getAnyCampaignMember(ArgumentMatchers.<Integer>any())).thenReturn(crewMember);
        Mockito.when(crewMember.isPlayer()).thenReturn(false);

        testMissionEntityGenerator = new TestMissionEntityGenerator();
        testMissionEntityGenerator.makeMissionArtifacts(numFrenchPlanes, numGermanPlanes, numFrenchBalloons, numGermanBalloons, numFrenchTrucks, numGermanTrucks);

        PwcgMissionData pwcgMissionData = new PwcgMissionData();
        List<PwcgGeneratedMissionVehicleData> missionPlanes = new ArrayList<>(testMissionEntityGenerator.getMissionPlanes().values());
        for (PwcgGeneratedMissionVehicleData missionPlane : missionPlanes)
        {
            pwcgMissionData.addMissionPlanes(missionPlane);
        }
        
        LogEventData logEventData = testMissionEntityGenerator.getAARLogEventData();
        AARBotVehicleMapper aarBotVehicleMapper = new AARBotVehicleMapper(logEventData);

        Map <String, LogPlane> planeAiEntities = testMissionEntityGenerator.getPlaneAiEntities();
        aarBotVehicleMapper.mapBotsToCrews(planeAiEntities);
                
        assert(planeAiEntities.size() == numFrenchPlanes + numGermanPlanes);
        for (LogPlane missionResultPlane : planeAiEntities.values())
        {
            assert(missionResultPlane.getLogCrewMember() != null);
            if (missionResultPlane.getId().equals("1001"))
            {
                assert(missionResultPlane.getLogCrewMember().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER + 1);
            }
            if (missionResultPlane.getId().equals("1002"))
            {
                assert(missionResultPlane.getLogCrewMember().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER + 2);
            }
            if (missionResultPlane.getId().equals("2001"))
            {
                assert(missionResultPlane.getLogCrewMember().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER + 100);
            }
            if (missionResultPlane.getId().equals("2002"))
            {
                assert(missionResultPlane.getLogCrewMember().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER + 200);
            }
        }
    }
}
