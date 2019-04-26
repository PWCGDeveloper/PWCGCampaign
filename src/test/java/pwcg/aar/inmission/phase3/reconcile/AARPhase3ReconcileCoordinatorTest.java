package pwcg.aar.inmission.phase3.reconcile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

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
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadmember.Victory;
import pwcg.campaign.ww1.country.RoFCountry;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignPersonnelTestHelper;
import pwcg.testutils.SquadrontTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class AARPhase3ReconcileCoordinatorTest
{
    private Campaign campaign;
    private List<LogPilot> pilotStatusList;
    private List<LogPilot> aceStatusList;
    private List<LogVictory> firmVictories;        
    private SquadronMember playerInFlight;
    private SquadronMember sergentInFlight;
    private SquadronMember corporalInFlight;
    private SquadronMember sltInFlight;
    private SquadronMember ltInFlight;
    private LogPlane playerPlaneVictor = new LogPlane(1);
    private LogPlane aiPlaneVictor = new LogPlane(2);

    @Mock private AARMissionEvaluationData evaluationData;
    @Mock private AARMissionLogFileSet missionLogFileSet;
    @Mock private AARContext aarContext;
    @Mock private AARPreliminaryData preliminaryData;
    @Mock private PwcgMissionData pwcgMissionData;

    private Map<Integer, PlayerDeclarations> playerDeclarations = new HashMap<>();
    private PlayerDeclarations playerDeclarationSet;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        PWCGContextManager.getInstance().changeContext(FrontMapIdentifier.FRANCE_MAP);
        campaign = CampaignCache.makeCampaignForceCreation(SquadrontTestProfile.ESC_103_PROFILE);
        
        aceStatusList = new ArrayList<>();
        pilotStatusList = new ArrayList<>();
        firmVictories = new ArrayList<>();
        playerDeclarationSet = new PlayerDeclarations();

        playerPlaneVictor.setSquadronId(101003);
        aiPlaneVictor.setSquadronId(101003);

        Mockito.when(aarContext.getPreliminaryData()).thenReturn(preliminaryData);
        Mockito.when(preliminaryData.getPwcgMissionData()).thenReturn(pwcgMissionData);
        
        Mockito.when(aarContext.getMissionEvaluationData()).thenReturn(evaluationData);
        Mockito.when(evaluationData.getPilotsInMission()).thenReturn(pilotStatusList);
        Mockito.when(evaluationData.getAceCrewsInMission()).thenReturn(aceStatusList);   
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
        
        createVictory(playerPlaneVictor, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1000, SerialNumber.PLANE_STARTING_SERIAL_NUMBER + 1000);
        createVictory(aiPlaneVictor, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001, SerialNumber.PLANE_STARTING_SERIAL_NUMBER + 1001);
        createVictory(aiPlaneVictor, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002, SerialNumber.PLANE_STARTING_SERIAL_NUMBER + 1002);

        AARPhase3ReconcileCoordinator phase3ReconcileCoordinator = new AARPhase3ReconcileCoordinator(campaign, aarContext);
        ReconciledInMissionData reconciledInMissionData = phase3ReconcileCoordinator.reconcileLogsWithAAR(playerDeclarations);
        
        assert(reconciledInMissionData.getPersonnelLossesInMission().getPersonnelKilled().size() == 1);
        assert(reconciledInMissionData.getPersonnelLossesInMission().getPersonnelCaptured().size() == 1);
        assert(reconciledInMissionData.getPersonnelLossesInMission().getPersonnelMaimed().size() == 1);
        assert(reconciledInMissionData.getPersonnelLossesInMission().getPersonnelWounded().size() == 2);
        assert(reconciledInMissionData.getPersonnelLossesInMission().getAcesKilled().size() == 2);

        List<Victory> aiPilotVictories = reconciledInMissionData.getReconciledVictoryData().getVictoryAwardsForPilot(aiPlaneVictor.getPilotSerialNumber());
        List<Victory> playerVictories = reconciledInMissionData.getReconciledVictoryData().getVictoryAwardsForPilot(playerPlaneVictor.getPilotSerialNumber());
        List<ClaimDeniedEvent> playerClaimsDenied = reconciledInMissionData.getReconciledVictoryData().getPlayerClaimsDenied();
        assert (aiPilotVictories.size() == 2);
        assert (playerVictories.size() == 1);
        assert (playerClaimsDenied.size() == 2);
    }
    
    private void addPlayerDeclarations() throws PWCGException
    {
        for (int i = 0; i < 3; ++i)
        {
            PlayerVictoryDeclaration declaration = new PlayerVictoryDeclaration();
            declaration.setAircraftType("albatrosd3");
            playerDeclarationSet.addDeclaration(declaration);
        }
        
        playerDeclarations.put(campaign.getPersonnelManager().getAllPlayers().getSquadronMemberList().get(0).getSerialNumber(), playerDeclarationSet);
    }

    private void createVictory(LogPlane victor, Integer pilotSerialNumber, Integer planeSerialNumber)
    {
        LogPlane victim = new LogPlane(3);
        victim.setPilotSerialNumber(pilotSerialNumber);
        victim.setPlaneSerialNumber(planeSerialNumber);
        victim.setVehicleType("albatrosd3");
        victim.setCountry(new RoFCountry(Country.GERMANY));
        victim.setSquadronId(501011);
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
        playerInFlight = campaign.getPersonnelManager().getAllPlayers().getSquadronMemberList().get(0);
        addSquadronPilot(playerInFlight.getSerialNumber(), SquadronMemberStatus.STATUS_WOUNDED);
        playerPlaneVictor.setPilotSerialNumber(playerInFlight.getSerialNumber());
        playerPlaneVictor.setCountry(new RoFCountry(Country.FRANCE));
        playerPlaneVictor.intializePilot(playerInFlight.getSerialNumber());
                
        sergentInFlight = CampaignPersonnelTestHelper.getSquadronMemberByRank(campaign, "Sergent");
        addSquadronPilot(sergentInFlight.getSerialNumber(), SquadronMemberStatus.STATUS_WOUNDED);
        aiPlaneVictor.setPilotSerialNumber(sergentInFlight.getSerialNumber());
        aiPlaneVictor.setCountry(new RoFCountry(Country.FRANCE));
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
        LogPilot wernerVoss = new LogPilot();
        wernerVoss.setSerialNumber(101175);
        wernerVoss.setStatus(SquadronMemberStatus.STATUS_KIA);
        
        LogPilot georgesGuynemer = new LogPilot();
        georgesGuynemer.setSerialNumber(101064);
        georgesGuynemer.setStatus(SquadronMemberStatus.STATUS_KIA);

        aceStatusList.add(wernerVoss);
        aceStatusList.add(georgesGuynemer);
    }

    
    private void addSquadronPilot(int serialNumber, int status)
    {
        LogPilot squadronCrewMember = new LogPilot();
        squadronCrewMember.setSerialNumber(serialNumber);
        squadronCrewMember.setStatus(status);
        pilotStatusList.add(squadronCrewMember);
    }
}
