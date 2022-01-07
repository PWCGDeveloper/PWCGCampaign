package pwcg.aar.inmission.phase3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.aar.data.AARContext;
import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogCrewMember;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.PlayerDeclarations;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.PlayerVictoryDeclaration;
import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.aar.prelim.PwcgMissionData;
import pwcg.aar.ui.events.model.ClaimDeniedEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.crewmember.Victory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.logfiles.LogFileSet;
import pwcg.product.bos.country.BoSCountry;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignPersonnelTestHelper;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AARInMissionResultGeneratorTest
{
    private static final int GEORGES_GUYNEMER = 101064;
    private static final int WERNER_VOSS = 101175;
    private Campaign campaign;
    private List<LogCrewMember> crewMemberStatusList;
    private List<LogVictory> firmVictories;        
    private CrewMember sergentInFlight;
    private CrewMember corporalInFlight;
    private CrewMember sltInFlight;
    private CrewMember ltInFlight;
    private LogPlane playerPlaneVictor = new LogPlane(1);
    private LogPlane aiPlaneVictor = new LogPlane(2);
    private LogPlane wernerVossPlaneVictor = new LogPlane(3);
    private LogPlane gerogesGuynemerPlaneVictor = new LogPlane(4);

    @Mock private AARMissionEvaluationData evaluationData;
    @Mock private LogFileSet missionLogFileSet;
    @Mock private AARPreliminaryData preliminaryData;
    @Mock private PwcgMissionData pwcgMissionData;

    private Map<Integer, PlayerDeclarations> playerDeclarations = new HashMap<>();
    private PlayerDeclarations playerDeclarationSet;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.ESC_103_PROFILE);
        
    }

    @BeforeEach
    public void setupTest() throws PWCGException
    {        
        crewMemberStatusList = new ArrayList<>();
        firmVictories = new ArrayList<>();
        playerDeclarationSet = new PlayerDeclarations();

        playerPlaneVictor.setCompanyId(SquadronTestProfile.ESC_103_PROFILE.getCompanyId());
        aiPlaneVictor.setCompanyId(SquadronTestProfile.ESC_103_PROFILE.getCompanyId());
        wernerVossPlaneVictor.setSquadronId(401010);
        gerogesGuynemerPlaneVictor.setCompanyId(SquadronTestProfile.ESC_103_PROFILE.getCompanyId());
        
        Mockito.when(evaluationData.getCrewMembersInMission()).thenReturn(crewMemberStatusList);
        Mockito.when(evaluationData.getVictoryResults()).thenReturn(firmVictories);   
        
        createCampaignMembersInMission();
        Mockito.when(evaluationData.getPlaneInMissionBySerialNumber(playerPlaneVictor.getCrewMemberSerialNumber())).thenReturn(playerPlaneVictor);   
        Mockito.when(evaluationData.getPlaneInMissionBySerialNumber(aiPlaneVictor.getCrewMemberSerialNumber())).thenReturn(aiPlaneVictor);   
    }

    @Test
    public void testMixedToVerifyDataTransfer() throws PWCGException
    {
        addPlayerDeclarations();
        createAcesInMission();
        
        createVictory(playerPlaneVictor, SerialNumber.AI_STARTING_SERIAL_NUMBER + 100, SerialNumber.PLANE_STARTING_SERIAL_NUMBER + 100);
        createVictory(aiPlaneVictor, SerialNumber.AI_STARTING_SERIAL_NUMBER + 101, SerialNumber.PLANE_STARTING_SERIAL_NUMBER + 101);
        createVictory(aiPlaneVictor, SerialNumber.AI_STARTING_SERIAL_NUMBER + 102, SerialNumber.PLANE_STARTING_SERIAL_NUMBER + 102);
        createVictory(playerPlaneVictor, WERNER_VOSS, SerialNumber.PLANE_STARTING_SERIAL_NUMBER + 103);
        createVictory(aiPlaneVictor, GEORGES_GUYNEMER, SerialNumber.PLANE_STARTING_SERIAL_NUMBER + 104);
        
        AARContext aarContext = new AARContext(campaign);
        aarContext.setMissionEvaluationData(evaluationData);
        
        AARInMissionResultGenerator coordinatorInMission = new AARInMissionResultGenerator(campaign, aarContext);
        coordinatorInMission.generateInMissionResults(playerDeclarations);
        
        assert(aarContext.getPersonnelLosses().getPersonnelKilled().size() == 3);
        assert(aarContext.getPersonnelLosses().getPersonnelCaptured().size() == 1);
        assert(aarContext.getPersonnelLosses().getPersonnelMaimed().size() == 1);
        assert(aarContext.getPersonnelLosses().getPersonnelWounded().size() == 2);
        assert(aarContext.getPersonnelLosses().getAcesKilled(campaign).size() == 2);

        List<Victory> aiCrewMemberVictories = aarContext.getPersonnelAcheivements().getVictoryAwardsForCrewMember(aiPlaneVictor.getCrewMemberSerialNumber());
        List<Victory> playerVictories = aarContext.getPersonnelAcheivements().getVictoryAwardsForCrewMember(playerPlaneVictor.getCrewMemberSerialNumber());
        List<ClaimDeniedEvent> playerClaimsDenied = aarContext.getPersonnelAcheivements().getPlayerClaimsDenied();
        Assertions.assertTrue (aiCrewMemberVictories.size() == 3);
        Assertions.assertTrue (playerVictories.size() == 2);
        Assertions.assertTrue (playerClaimsDenied.size() == 2);
    }
    
    private void addPlayerDeclarations() throws PWCGException
    {
        for (int i = 0; i < 4; ++i)
        {
            PlayerVictoryDeclaration declaration = new PlayerVictoryDeclaration();
            declaration.setAircraftType("albatrosd5");
            playerDeclarationSet.addDeclaration(declaration);
        }
        
        CrewMember playerInFlight = campaign.findReferencePlayer();
        playerDeclarations.put(playerInFlight.getSerialNumber(), playerDeclarationSet);
    }

    private void createVictory(LogPlane victor, Integer crewMemberSerialNumber, Integer planeSerialNumber)
    {
        LogPlane victim = new LogPlane(3);
        victim.setCrewMemberSerialNumber(crewMemberSerialNumber);
        victim.setPlaneSerialNumber(planeSerialNumber);
        victim.setVehicleType("albatrosd5");
        victim.setCountry(new BoSCountry(Country.GERMANY));
        victim.setCompanyId(SquadronTestProfile.JASTA_11_PROFILE.getCompanyId());
        victim.intializeCrewMember(crewMemberSerialNumber);

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setLocation(new Coordinate(100.0, 0.0, 100.0));
        resultVictory.setVictor(victor);
        resultVictory.setVictim(victim);
        firmVictories.add(resultVictory);
    }

    private void createCampaignMembersInMission() throws PWCGException
    {        
        CrewMember playerInFlight = campaign.findReferencePlayer();
        addSquadronCrewMember(playerInFlight.getSerialNumber(), CrewMemberStatus.STATUS_WOUNDED);
        playerPlaneVictor.setCrewMemberSerialNumber(playerInFlight.getSerialNumber());
        playerPlaneVictor.setCountry(new BoSCountry(Country.FRANCE));
        playerPlaneVictor.intializeCrewMember(playerInFlight.getSerialNumber());
                
        sergentInFlight = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "Sergent");
        addSquadronCrewMember(sergentInFlight.getSerialNumber(), CrewMemberStatus.STATUS_WOUNDED);
        aiPlaneVictor.setCrewMemberSerialNumber(sergentInFlight.getSerialNumber());
        aiPlaneVictor.setCountry(new BoSCountry(Country.FRANCE));
        aiPlaneVictor.intializeCrewMember(sergentInFlight.getSerialNumber());

        corporalInFlight = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "Corporal");
        addSquadronCrewMember(corporalInFlight.getSerialNumber(), CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED);
        
        sltInFlight = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "Sous Lieutenant");
        addSquadronCrewMember(sltInFlight.getSerialNumber(), CrewMemberStatus.STATUS_KIA);
        
        ltInFlight = CampaignPersonnelTestHelper.getCrewMemberByRank(campaign, "Lieutenant");
        addSquadronCrewMember(ltInFlight.getSerialNumber(), CrewMemberStatus.STATUS_CAPTURED);
    }
    
    private void createAcesInMission() throws PWCGException
    {
        addSquadronCrewMember(WERNER_VOSS, CrewMemberStatus.STATUS_KIA);
        addSquadronCrewMember(GEORGES_GUYNEMER, CrewMemberStatus.STATUS_KIA);
    }

    
    private void addSquadronCrewMember(int serialNumber, int status)
    {
        LogCrewMember squadronCrewMember = new LogCrewMember();
        squadronCrewMember.setSerialNumber(serialNumber);
        squadronCrewMember.setStatus(status);
        crewMemberStatusList.add(squadronCrewMember);
    }
}
