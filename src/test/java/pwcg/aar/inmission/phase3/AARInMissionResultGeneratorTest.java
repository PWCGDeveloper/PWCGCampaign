package pwcg.aar.inmission.phase3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.aar.data.AARContext;
import pwcg.aar.inmission.phase1.parse.AARMissionLogFileSet;
import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPilot;
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
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadmember.Victory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.product.fc.country.FCCountry;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignPersonnelTestHelper;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class AARInMissionResultGeneratorTest
{
    private static final int GEORGES_GUYNEMER = 101064;
    private static final int WERNER_VOSS = 101175;
    private Campaign campaign;
    private List<LogPilot> pilotStatusList;
    private List<LogVictory> firmVictories;        
    private SquadronMember sergentInFlight;
    private SquadronMember corporalInFlight;
    private SquadronMember sltInFlight;
    private SquadronMember ltInFlight;
    private LogPlane playerPlaneVictor = new LogPlane(1);
    private LogPlane aiPlaneVictor = new LogPlane(2);
    private LogPlane wernerVossPlaneVictor = new LogPlane(3);
    private LogPlane gerogesGuynemerPlaneVictor = new LogPlane(4);

    @Mock private AARMissionEvaluationData evaluationData;
    @Mock private AARMissionLogFileSet missionLogFileSet;
    @Mock private AARPreliminaryData preliminaryData;
    @Mock private PwcgMissionData pwcgMissionData;

    private Map<Integer, PlayerDeclarations> playerDeclarations = new HashMap<>();
    private PlayerDeclarations playerDeclarationSet;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.ESC_103_PROFILE);
        
        pilotStatusList = new ArrayList<>();
        firmVictories = new ArrayList<>();
        playerDeclarationSet = new PlayerDeclarations();

        playerPlaneVictor.setSquadronId(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        aiPlaneVictor.setSquadronId(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        wernerVossPlaneVictor.setSquadronId(401010);
        gerogesGuynemerPlaneVictor.setSquadronId(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        
        Mockito.when(evaluationData.getPilotsInMission()).thenReturn(pilotStatusList);
        Mockito.when(evaluationData.getVictoryResults()).thenReturn(firmVictories);   
        
        createCampaignMembersInMission();
        Mockito.when(evaluationData.getPlaneInMissionBySerialNumber(playerPlaneVictor.getPilotSerialNumber())).thenReturn(playerPlaneVictor);   
        Mockito.when(evaluationData.getPlaneInMissionBySerialNumber(aiPlaneVictor.getPilotSerialNumber())).thenReturn(aiPlaneVictor);   
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

        List<Victory> aiPilotVictories = aarContext.getPersonnelAcheivements().getVictoryAwardsForPilot(aiPlaneVictor.getPilotSerialNumber());
        List<Victory> playerVictories = aarContext.getPersonnelAcheivements().getVictoryAwardsForPilot(playerPlaneVictor.getPilotSerialNumber());
        List<ClaimDeniedEvent> playerClaimsDenied = aarContext.getPersonnelAcheivements().getPlayerClaimsDenied();
        assert (aiPilotVictories.size() == 3);
        assert (playerVictories.size() == 2);
        assert (playerClaimsDenied.size() == 2);
    }
    
    private void addPlayerDeclarations() throws PWCGException
    {
        for (int i = 0; i < 4; ++i)
        {
            PlayerVictoryDeclaration declaration = new PlayerVictoryDeclaration();
            declaration.setAircraftType("albatrosd5");
            playerDeclarationSet.addDeclaration(declaration);
        }
        
        SquadronMember playerInFlight = campaign.findReferencePlayer();
        playerDeclarations.put(playerInFlight.getSerialNumber(), playerDeclarationSet);
    }

    private void createVictory(LogPlane victor, Integer pilotSerialNumber, Integer planeSerialNumber)
    {
        LogPlane victim = new LogPlane(3);
        victim.setPilotSerialNumber(pilotSerialNumber);
        victim.setPlaneSerialNumber(planeSerialNumber);
        victim.setVehicleType("albatrosd5");
        victim.setCountry(new FCCountry(Country.GERMANY));
        victim.setSquadronId(SquadronTestProfile.JASTA_11_PROFILE.getSquadronId());
        victim.intializePilot(pilotSerialNumber);

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setLocation(new Coordinate(100.0, 0.0, 100.0));
        resultVictory.setVictor(victor);
        resultVictory.setVictim(victim);
        resultVictory.setCrossedPlayerPath(true);
        firmVictories.add(resultVictory);
    }

    private void createCampaignMembersInMission() throws PWCGException
    {        
        SquadronMember playerInFlight = campaign.findReferencePlayer();
        addSquadronPilot(playerInFlight.getSerialNumber(), SquadronMemberStatus.STATUS_WOUNDED);
        playerPlaneVictor.setPilotSerialNumber(playerInFlight.getSerialNumber());
        playerPlaneVictor.setCountry(new FCCountry(Country.FRANCE));
        playerPlaneVictor.intializePilot(playerInFlight.getSerialNumber());
                
        sergentInFlight = CampaignPersonnelTestHelper.getSquadronMemberByRank(campaign, "Sergent");
        addSquadronPilot(sergentInFlight.getSerialNumber(), SquadronMemberStatus.STATUS_WOUNDED);
        aiPlaneVictor.setPilotSerialNumber(sergentInFlight.getSerialNumber());
        aiPlaneVictor.setCountry(new FCCountry(Country.FRANCE));
        aiPlaneVictor.intializePilot(sergentInFlight.getSerialNumber());

        corporalInFlight = CampaignPersonnelTestHelper.getSquadronMemberByRank(campaign, "Corporal");
        addSquadronPilot(corporalInFlight.getSerialNumber(), SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED);
        
        sltInFlight = CampaignPersonnelTestHelper.getSquadronMemberByRank(campaign, "Sous Lieutenant");
        addSquadronPilot(sltInFlight.getSerialNumber(), SquadronMemberStatus.STATUS_KIA);
        
        ltInFlight = CampaignPersonnelTestHelper.getSquadronMemberByRank(campaign, "Lieutenant");
        addSquadronPilot(ltInFlight.getSerialNumber(), SquadronMemberStatus.STATUS_CAPTURED);
    }
    
    private void createAcesInMission() throws PWCGException
    {
        addSquadronPilot(WERNER_VOSS, SquadronMemberStatus.STATUS_KIA);
        addSquadronPilot(GEORGES_GUYNEMER, SquadronMemberStatus.STATUS_KIA);
    }

    
    private void addSquadronPilot(int serialNumber, int status)
    {
        LogPilot squadronCrewMember = new LogPilot();
        squadronCrewMember.setSerialNumber(serialNumber);
        squadronCrewMember.setStatus(status);
        pilotStatusList.add(squadronCrewMember);
    }
}
