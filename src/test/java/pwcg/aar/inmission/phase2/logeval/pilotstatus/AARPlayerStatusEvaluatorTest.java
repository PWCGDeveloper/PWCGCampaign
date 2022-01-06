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
public class AARPlayerStatusEvaluatorTest
{
    @Mock private LogEventData logEventData;
    @Mock private AARVehicleBuilder aarVehicleBuilder;
    @Mock private AARDestroyedStatusEvaluator destroyedStatusEvaluator;
    @Mock private AARCrewMemberStatusDeadEvaluator aarCrewMemberStatusDeadEvaluator;
    @Mock private AARCrewMemberStatusCapturedEvaluator aarCrewMemberStatusCapturedEvaluator;
    @Mock private AARCrewMemberStatusWoundedEvaluator aarCrewMemberStatusWoundedEvaluator;
    @Mock private Campaign campaign;
    @Mock private PwcgMissionData pwcgMissionData;
    @Mock private MissionHeader missionHeader;
    @Mock private ConfigManagerCampaign configManager;

    public AARPlayerStatusEvaluatorTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    @BeforeEach
    public void setupTest() throws PWCGException
    {        
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420801"));
    }

    @Test
    public void testPlayerIsInvulnerable () throws PWCGException
    {
        testPlayerStatusAdjustment(1, CrewMemberStatus.STATUS_ACTIVE);
    }

    @Test
    public void testPlayerIsWounded () throws PWCGException
    {
        testPlayerStatusAdjustment(2, CrewMemberStatus.STATUS_WOUNDED);
    }

    @Test
    public void testPlayerIsSerouslyWounded () throws PWCGException
    {
        testPlayerStatusAdjustment(3, CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED);
    }

    @Test
    public void testPlayerIsDead () throws PWCGException
    {
        testPlayerStatusAdjustment(4, CrewMemberStatus.STATUS_KIA);
    }

    private void testPlayerStatusAdjustment (int maxPlayerWound, int expectedStatus) throws PWCGException
    {
        testSetup();
        
        Mockito.when(aarCrewMemberStatusWoundedEvaluator.getCrewMemberWoundedLevel(ArgumentMatchers.anyList())).
            thenReturn(CrewMemberStatus.STATUS_WOUNDED);
        Mockito.when(aarCrewMemberStatusCapturedEvaluator.isCrewMemberCaptured(
        		ArgumentMatchers.<FrontMapIdentifier>any(), 
        		ArgumentMatchers.<Coordinate>any(), 
        		ArgumentMatchers.<Side>any())).thenReturn(true);
        Mockito.when(aarCrewMemberStatusDeadEvaluator.isCrewMemberDead()).thenReturn(true);

        AARCrewMemberStatusEvaluator aarCrewMemberStatusEvaluator = makeEvaluator(maxPlayerWound);
                
        runTestWithStatusCheck(aarCrewMemberStatusEvaluator, expectedStatus);
    }

    private void testSetup() throws PWCGException
    {
        AType3 atype3 = new AType3("T:54877 AType:3 AID:-1 TID:35839 POS(112150.266,93.277,111696.758)");
        
        Mockito.when(logEventData.getDestroyedEventForPlaneByBot(ArgumentMatchers.<String>any())).thenReturn(atype3);
        Mockito.when(logEventData.getDamageForBot(ArgumentMatchers.<String>any())).thenReturn(new ArrayList<IAType2>());

        Map <String, LogPlane> planeAiEntities = makePlaneEntities();

        Mockito.when(aarVehicleBuilder.getLogPlanes()).thenReturn(planeAiEntities);
    }

    private AARCrewMemberStatusEvaluator makeEvaluator(int maxPlayerInjury) throws PWCGException
    {
        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);
        Mockito.when(configManager.getIntConfigParam(ConfigItemKeys.CrewMemberInjuryKey)).thenReturn(maxPlayerInjury);
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

    private Map <String, LogPlane> makePlaneEntities()
    {
        Map <String, LogPlane> planeAiEntities = new HashMap <>();
        LogPlane resultPlane = new LogPlane(1);
        resultPlane.setLandAt(new Coordinate());
        ICountry country = CountryFactory.makeCountryByCountry(Country.BRITAIN);
        resultPlane.setCountry(country);
        planeAiEntities.put("11111", resultPlane);
        resultPlane.intializeCrewMember(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        
        return planeAiEntities;
    }
}
