package pwcg.aar.inmission.phase2.logeval.pilotstatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.inmission.phase1.parse.AARLogEventData;
import pwcg.aar.inmission.phase2.logeval.AARDestroyedStatusEvaluator;
import pwcg.aar.inmission.phase2.logeval.AARVehicleBuilder;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPilot;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.prelim.PwcgMissionData;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.logfiles.event.AType3;
import pwcg.core.logfiles.event.IAType2;
import pwcg.core.utils.DateUtils;
import pwcg.mission.data.MissionHeader;

@ExtendWith(MockitoExtension.class)
public class AARPilotStatusEvaluatorTest
{
    @Mock private AARLogEventData logEventData;
    @Mock private AARVehicleBuilder aarVehicleBuilder;
    @Mock private AARPilotStatusDeadEvaluator aarPilotStatusDeadEvaluator;
    @Mock private AARPilotStatusCapturedEvaluator aarPilotStatusCapturedEvaluator;
    @Mock private AARPilotStatusWoundedEvaluator aarPilotStatusWoundedEvaluator;
    @Mock private AARDestroyedStatusEvaluator destroyedStatusEvaluator;
    @Mock private Campaign campaign;
    @Mock private PwcgMissionData pwcgMissionData;
    @Mock private MissionHeader missionHeader;
    @Mock private ConfigManagerCampaign configManager;
    
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420801"));
    }

    @Test
    public void testCrewSurvived () throws PWCGException
    {
        testSetup();
        
        Mockito.when(aarPilotStatusDeadEvaluator.isCrewMemberDead()).thenReturn(false);
        Mockito.when(aarPilotStatusCapturedEvaluator.isCrewMemberCaptured(
        		ArgumentMatchers.<FrontMapIdentifier>any(), 
        		ArgumentMatchers.<Coordinate>any(), 
        		ArgumentMatchers.<Side>any())).thenReturn(false);
        Mockito.when(aarPilotStatusWoundedEvaluator.getCrewMemberWoundedLevel(ArgumentMatchers.anyList())).thenReturn(SquadronMemberStatus.STATUS_ACTIVE);

        AARPilotStatusEvaluator aarPilotStatusEvaluator = makeEvaluator();
        
        aarPilotStatusEvaluator.determineFateOfCrewsInMission();
        for (LogPlane resultPlaneAfter : aarVehicleBuilder.getLogPlanes().values())
        {
            LogPilot crewmanAfter = resultPlaneAfter.getLogPilot();
            Assertions.assertTrue (crewmanAfter.getStatus() == SquadronMemberStatus.STATUS_ACTIVE);
        }        
    }

    @Test
    public void testCrewWounded () throws PWCGException
    {
        testSetup();
        
        Mockito.when(aarPilotStatusWoundedEvaluator.getCrewMemberWoundedLevel(ArgumentMatchers.anyList())).
            thenReturn(SquadronMemberStatus.STATUS_WOUNDED);
        Mockito.when(aarPilotStatusCapturedEvaluator.isCrewMemberCaptured(
        		ArgumentMatchers.<FrontMapIdentifier>any(), 
        		ArgumentMatchers.<Coordinate>any(), 
        		ArgumentMatchers.<Side>any())).thenReturn(false);
        Mockito.when(aarPilotStatusDeadEvaluator.isCrewMemberDead()).thenReturn(false);

        AARPilotStatusEvaluator aarPilotStatusEvaluator = makeEvaluator();
        
        runTestWithStatusCheck(aarPilotStatusEvaluator, SquadronMemberStatus.STATUS_WOUNDED);
    }

    /**
     * Crew was wounded and captured.
     */
    @Test
    public void testCrewCaptured () throws PWCGException
    {
        testSetup();
        
        Mockito.when(aarPilotStatusWoundedEvaluator.getCrewMemberWoundedLevel(ArgumentMatchers.anyList())).
            thenReturn(SquadronMemberStatus.STATUS_WOUNDED);
        Mockito.when(aarPilotStatusCapturedEvaluator.isCrewMemberCaptured(
        		ArgumentMatchers.<FrontMapIdentifier>any(), 
        		ArgumentMatchers.<Coordinate>any(), 
        		ArgumentMatchers.<Side>any())).thenReturn(true);
        Mockito.when(aarPilotStatusDeadEvaluator.isCrewMemberDead()).thenReturn(false);

        AARPilotStatusEvaluator aarPilotStatusEvaluator = makeEvaluator();
        
        aarPilotStatusEvaluator.determineFateOfCrewsInMission();
        
        runTestWithStatusCheck(aarPilotStatusEvaluator, SquadronMemberStatus.STATUS_CAPTURED);
    }


    /**
     * Crew was wounded and captured and killed.
     */
    @Test
    public void testCrewKilled () throws PWCGException
    {
        testSetup();
        
        Mockito.when(aarPilotStatusWoundedEvaluator.getCrewMemberWoundedLevel(ArgumentMatchers.anyList())).
            thenReturn(SquadronMemberStatus.STATUS_WOUNDED);
        Mockito.when(aarPilotStatusCapturedEvaluator.isCrewMemberCaptured(
        		ArgumentMatchers.<FrontMapIdentifier>any(), 
        		ArgumentMatchers.<Coordinate>any(), 
        		ArgumentMatchers.<Side>any())).thenReturn(true);
        Mockito.when(aarPilotStatusDeadEvaluator.isCrewMemberDead()).thenReturn(true);

        AARPilotStatusEvaluator aarPilotStatusEvaluator = makeEvaluator();
        
        aarPilotStatusEvaluator.determineFateOfCrewsInMission();
        
        runTestWithStatusCheck(aarPilotStatusEvaluator, SquadronMemberStatus.STATUS_KIA);
    }

    private void testSetup() throws PWCGException
    {
        AType3 atype3 = new AType3("T:54877 AType:3 AID:-1 TID:35839 POS(112150.266,93.277,111696.758)");
        Mockito.when(logEventData.getDestroyedEventForPlaneByBot(ArgumentMatchers.<String>any())).thenReturn(atype3);
        Mockito.when(logEventData.getDamageForBot(ArgumentMatchers.<String>any())).thenReturn(new ArrayList<IAType2>());

        Map <String, LogPlane> planeAiEntities = makePlaneEntities();

        Mockito.when(aarVehicleBuilder.getLogPlanes()).thenReturn(planeAiEntities);
    }

    private AARPilotStatusEvaluator makeEvaluator() throws PWCGException
    {
        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);
        Mockito.when(configManager.getIntConfigParam(ConfigItemKeys.PilotInjuryKey)).thenReturn(4);
        Mockito.when(pwcgMissionData.getMissionHeader()).thenReturn(missionHeader);
        Mockito.when(missionHeader.getMapName()).thenReturn(FrontMapIdentifier.ARRAS_MAP.getMapName());

        AARPilotStatusEvaluator aarPilotStatusEvaluator = new AARPilotStatusEvaluator(campaign, pwcgMissionData, destroyedStatusEvaluator, logEventData, aarVehicleBuilder);
        aarPilotStatusEvaluator.setAarPilotStatusCapturedEvaluator(aarPilotStatusCapturedEvaluator);
        aarPilotStatusEvaluator.setAarPilotStatusWoundedEvaluator(aarPilotStatusWoundedEvaluator);
        aarPilotStatusEvaluator.setAarPilotStatusDeadEvaluator(aarPilotStatusDeadEvaluator);
        return aarPilotStatusEvaluator;
    }

    private void runTestWithStatusCheck(AARPilotStatusEvaluator aarPilotStatusEvaluator, int expectedStatus) throws PWCGException
    {
        aarPilotStatusEvaluator.determineFateOfCrewsInMission();
        for (LogPlane resultPlaneAfter : aarVehicleBuilder.getLogPlanes().values())
        {
            LogPilot crewmanAfter = resultPlaneAfter.getLogPilot();
            Assertions.assertTrue (crewmanAfter.getStatus() == expectedStatus);
        }
    }

    private Map <String, LogPlane> makePlaneEntities() throws PWCGException
    {
        Map <String, LogPlane> planeAiEntities = new HashMap <>();
        LogPlane resultPlane = new LogPlane(2);
        resultPlane.setLandAt(new Coordinate());
        ICountry country = CountryFactory.makeCountryByCountry(Country.BRITAIN);
        resultPlane.setCountry(country);
        planeAiEntities.put("11111", resultPlane);
        resultPlane.intializePilot(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        
        return planeAiEntities;
    }
}
