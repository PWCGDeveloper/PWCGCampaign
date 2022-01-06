package pwcg.aar.inmission.phase2.logeval.crewMemberstatus;

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

import pwcg.aar.inmission.phase2.logeval.AARDestroyedStatusEvaluator;
import pwcg.aar.inmission.phase2.logeval.AARVehicleBuilder;
import pwcg.aar.inmission.phase2.logeval.crewmemberstatus.AARCrewMemberStatusCapturedEvaluator;
import pwcg.aar.inmission.phase2.logeval.crewmemberstatus.AARCrewMemberStatusDeadEvaluator;
import pwcg.aar.inmission.phase2.logeval.crewmemberstatus.AARCrewMemberStatusEvaluator;
import pwcg.aar.inmission.phase2.logeval.crewmemberstatus.AARCrewMemberStatusWoundedEvaluator;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogCrewMember;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.prelim.PwcgMissionData;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.logfiles.LogEventData;
import pwcg.core.logfiles.event.AType3;
import pwcg.core.logfiles.event.IAType2;
import pwcg.core.utils.DateUtils;
import pwcg.mission.data.MissionHeader;

@ExtendWith(MockitoExtension.class)
public class AARCrewMemberStatusEvaluatorTest
{
    @Mock private LogEventData logEventData;
    @Mock private AARVehicleBuilder aarVehicleBuilder;
    @Mock private AARCrewMemberStatusDeadEvaluator aarCrewMemberStatusDeadEvaluator;
    @Mock private AARCrewMemberStatusCapturedEvaluator aarCrewMemberStatusCapturedEvaluator;
    @Mock private AARCrewMemberStatusWoundedEvaluator aarCrewMemberStatusWoundedEvaluator;
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
        
        Mockito.when(aarCrewMemberStatusDeadEvaluator.isCrewMemberDead()).thenReturn(false);
        Mockito.when(aarCrewMemberStatusCapturedEvaluator.isCrewMemberCaptured(
        		ArgumentMatchers.<FrontMapIdentifier>any(), 
        		ArgumentMatchers.<Coordinate>any(), 
        		ArgumentMatchers.<Side>any())).thenReturn(false);
        Mockito.when(aarCrewMemberStatusWoundedEvaluator.getCrewMemberWoundedLevel(ArgumentMatchers.anyList())).thenReturn(CrewMemberStatus.STATUS_ACTIVE);

        AARCrewMemberStatusEvaluator aarCrewMemberStatusEvaluator = makeEvaluator();
        
        aarCrewMemberStatusEvaluator.determineFateOfCrewsInMission();
        for (LogPlane resultPlaneAfter : aarVehicleBuilder.getLogPlanes().values())
        {
            LogCrewMember crewmanAfter = resultPlaneAfter.getLogCrewMember();
            Assertions.assertTrue (crewmanAfter.getStatus() == CrewMemberStatus.STATUS_ACTIVE);
        }        
    }

    @Test
    public void testCrewWounded () throws PWCGException
    {
        testSetup();
        
        Mockito.when(aarCrewMemberStatusWoundedEvaluator.getCrewMemberWoundedLevel(ArgumentMatchers.anyList())).
            thenReturn(CrewMemberStatus.STATUS_WOUNDED);
        Mockito.when(aarCrewMemberStatusCapturedEvaluator.isCrewMemberCaptured(
        		ArgumentMatchers.<FrontMapIdentifier>any(), 
        		ArgumentMatchers.<Coordinate>any(), 
        		ArgumentMatchers.<Side>any())).thenReturn(false);
        Mockito.when(aarCrewMemberStatusDeadEvaluator.isCrewMemberDead()).thenReturn(false);

        AARCrewMemberStatusEvaluator aarCrewMemberStatusEvaluator = makeEvaluator();
        
        runTestWithStatusCheck(aarCrewMemberStatusEvaluator, CrewMemberStatus.STATUS_WOUNDED);
    }

    /**
     * Crew was wounded and captured.
     */
    @Test
    public void testCrewCaptured () throws PWCGException
    {
        testSetup();
        
        Mockito.when(aarCrewMemberStatusWoundedEvaluator.getCrewMemberWoundedLevel(ArgumentMatchers.anyList())).
            thenReturn(CrewMemberStatus.STATUS_WOUNDED);
        Mockito.when(aarCrewMemberStatusCapturedEvaluator.isCrewMemberCaptured(
        		ArgumentMatchers.<FrontMapIdentifier>any(), 
        		ArgumentMatchers.<Coordinate>any(), 
        		ArgumentMatchers.<Side>any())).thenReturn(true);
        Mockito.when(aarCrewMemberStatusDeadEvaluator.isCrewMemberDead()).thenReturn(false);

        AARCrewMemberStatusEvaluator aarCrewMemberStatusEvaluator = makeEvaluator();
        
        aarCrewMemberStatusEvaluator.determineFateOfCrewsInMission();
        
        runTestWithStatusCheck(aarCrewMemberStatusEvaluator, CrewMemberStatus.STATUS_CAPTURED);
    }


    /**
     * Crew was wounded and captured and killed.
     */
    @Test
    public void testCrewKilled () throws PWCGException
    {
        testSetup();
        
        Mockito.when(aarCrewMemberStatusWoundedEvaluator.getCrewMemberWoundedLevel(ArgumentMatchers.anyList())).
            thenReturn(CrewMemberStatus.STATUS_WOUNDED);
        Mockito.when(aarCrewMemberStatusCapturedEvaluator.isCrewMemberCaptured(
        		ArgumentMatchers.<FrontMapIdentifier>any(), 
        		ArgumentMatchers.<Coordinate>any(), 
        		ArgumentMatchers.<Side>any())).thenReturn(true);
        Mockito.when(aarCrewMemberStatusDeadEvaluator.isCrewMemberDead()).thenReturn(true);

        AARCrewMemberStatusEvaluator aarCrewMemberStatusEvaluator = makeEvaluator();
        
        aarCrewMemberStatusEvaluator.determineFateOfCrewsInMission();
        
        runTestWithStatusCheck(aarCrewMemberStatusEvaluator, CrewMemberStatus.STATUS_KIA);
    }

    private void testSetup() throws PWCGException
    {
        AType3 atype3 = new AType3("T:54877 AType:3 AID:-1 TID:35839 POS(112150.266,93.277,111696.758)");
        Mockito.when(logEventData.getDestroyedEventForPlaneByBot(ArgumentMatchers.<String>any())).thenReturn(atype3);
        Mockito.when(logEventData.getDamageForBot(ArgumentMatchers.<String>any())).thenReturn(new ArrayList<IAType2>());

        Map <String, LogPlane> planeAiEntities = makePlaneEntities();

        Mockito.when(aarVehicleBuilder.getLogPlanes()).thenReturn(planeAiEntities);
    }

    private AARCrewMemberStatusEvaluator makeEvaluator() throws PWCGException
    {
        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);
        Mockito.when(configManager.getIntConfigParam(ConfigItemKeys.CrewMemberInjuryKey)).thenReturn(4);
        Mockito.when(pwcgMissionData.getMissionHeader()).thenReturn(missionHeader);
        Mockito.when(missionHeader.getMapName()).thenReturn(FrontMapIdentifier.STALINGRAD_MAP.getMapName());

        AARCrewMemberStatusEvaluator aarCrewMemberStatusEvaluator = new AARCrewMemberStatusEvaluator(campaign, pwcgMissionData, destroyedStatusEvaluator, logEventData, aarVehicleBuilder);
        aarCrewMemberStatusEvaluator.setAarCrewMemberStatusCapturedEvaluator(aarCrewMemberStatusCapturedEvaluator);
        aarCrewMemberStatusEvaluator.setAarCrewMemberStatusWoundedEvaluator(aarCrewMemberStatusWoundedEvaluator);
        aarCrewMemberStatusEvaluator.setAarCrewMemberStatusDeadEvaluator(aarCrewMemberStatusDeadEvaluator);
        return aarCrewMemberStatusEvaluator;
    }

    private void runTestWithStatusCheck(AARCrewMemberStatusEvaluator aarCrewMemberStatusEvaluator, int expectedStatus) throws PWCGException
    {
        aarCrewMemberStatusEvaluator.determineFateOfCrewsInMission();
        for (LogPlane resultPlaneAfter : aarVehicleBuilder.getLogPlanes().values())
        {
            LogCrewMember crewmanAfter = resultPlaneAfter.getLogCrewMember();
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
        resultPlane.intializeCrewMember(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        
        return planeAiEntities;
    }
}
