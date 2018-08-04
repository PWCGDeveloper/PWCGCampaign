package pwcg.aar.inmission.phase3.reconcile.victories;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.PlaneTypeFactory;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class PlayerDeclarationResolutionFuzzyVictoryTest
{
    @Mock 
    private Campaign campaign;
    
    @Mock
    private CampaignPersonnelManager personnelManager;

    @Mock
    private PlayerVictoryDeclaration mockPlayerDeclaration;

    @Mock
    private AARMissionEvaluationData evaluationData;

    @Mock
    private VictorySorter victorySorter;
    
    @Mock
    private SquadronMember player;
    
    @Mock
    private SquadronMember ai;

    @Mock
    private PlaneTypeFactory planeFactory;
    
    private PlayerDeclarations playerDeclarations;


    private List<LogVictory> fuzzyVictories = new ArrayList<>();        
    private List<LogVictory> emptyList = new ArrayList<>();        

    private LogPlane playerVictor = new LogPlane();
    private LogPlane aiVictor = new LogPlane();
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        
        fuzzyVictories.clear();
        
        playerVictor.setPilotSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        aiVictor.setPilotSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1);
        
        createMocks();
    }
    
    private void createMocks() throws PWCGException
    {
        
        Mockito.when(victorySorter.getFuzzyAirVictories()).thenReturn(fuzzyVictories);
        Mockito.when(victorySorter.getFuzzyBalloonVictories()).thenReturn(emptyList);
        Mockito.when(victorySorter.getFirmAirVictories()).thenReturn(emptyList);
        Mockito.when(victorySorter.getAllUnconfirmed()).thenReturn(emptyList);
        Mockito.when(campaign.getPlayer()).thenReturn(player);
        Mockito.when(campaign.getName()).thenReturn("Player Pilot");
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getBeginningOfWar());
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
        
        Mockito.when(personnelManager.getActiveCampaignMember(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER)).thenReturn(player);
        Mockito.when(personnelManager.getActiveCampaignMember(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1)).thenReturn(ai);

        Mockito.when(evaluationData.getPlaneInMissionBySerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER)).thenReturn(playerVictor);
        Mockito.when(evaluationData.getPlaneInMissionBySerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1)).thenReturn(aiVictor);

        Mockito.when(player.getSerialNumber()).thenReturn(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        Mockito.when(ai.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1);
    }

    private void createVictory(Integer victimSerialNumber, String aircraftType, Role approximateRole)
    {        
        LogPlane victim = new LogPlane();
        victim.setPilotSerialNumber(victimSerialNumber);
        victim.setVehicleType(aircraftType);
        victim.setRole(approximateRole);

        LogVictory resultVictory = new LogVictory();
        resultVictory.setVictim(victim);
        resultVictory.setCrossedPlayerPath(true);
        
        fuzzyVictories.add(resultVictory);
    }
    
    private void createPlayerDeclarations(int numDeclarations) throws PWCGException
    {
        playerDeclarations = new PlayerDeclarations();
        for (int i = 0; i < numDeclarations; ++i)
        {
            PlayerVictoryDeclaration declaration = new PlayerVictoryDeclaration();
            declaration.confirmDeclaration(true, "S.E.5a");
            playerDeclarations.addPlayerDeclaration(declaration);
        }        
    }
    
    @Test
    public void testPlayerFuzzyVictoryAward () throws PWCGException
    {   
        
        createPlayerDeclarations(1);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001, "se5a", Role.ROLE_FIGHTER);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002, "se5a", Role.ROLE_FIGHTER);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1003, "se5a", Role.ROLE_FIGHTER);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerAirResultsWithClaims(playerDeclarations);
        
        assert (confirmedPlayerVictories.getConfirmedVictories().size() == 1);
    }
    
    @Test
    public void testPlayerMultipleFuzzyVictoryAwards () throws PWCGException
    {   
        
        createPlayerDeclarations(3);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001, "se5a", Role.ROLE_FIGHTER);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002, "se5a", Role.ROLE_FIGHTER);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1003, "se5a", Role.ROLE_FIGHTER);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerAirResultsWithClaims(playerDeclarations);
        
        assert (confirmedPlayerVictories.getConfirmedVictories().size() == 3);
    }
    
    @Test
    public void testPlayerTwoClaimsOneMatchOneNotExact () throws PWCGException
    {   
        
        createPlayerDeclarations(2);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001, "se5a", Role.ROLE_FIGHTER);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002, "sopcamel", Role.ROLE_FIGHTER);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1003, "sopcamel", Role.ROLE_FIGHTER);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerAirResultsWithClaims(playerDeclarations);
        
        assert (confirmedPlayerVictories.getConfirmedVictories().size() == 2);
    }
    
    @Test
    public void testPlayerTwoClaimsButWrongAircraftCategory () throws PWCGException
    {   
        
        createPlayerDeclarations(2);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001, "re8", Role.ROLE_BOMB);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002, "re8", Role.ROLE_BOMB);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1003, "re8", Role.ROLE_BOMB);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerAirResultsWithClaims(playerDeclarations);
        
        assert (confirmedPlayerVictories.getConfirmedVictories().size() == 0);
    }
    
    @Test
    public void testPlayerOneInexactTwoWrongCategory () throws PWCGException
    {   
        
        createPlayerDeclarations(2);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001, "re8", Role.ROLE_BOMB);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002, "re8", Role.ROLE_BOMB);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1003, "sopcamel", Role.ROLE_FIGHTER);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerAirResultsWithClaims(playerDeclarations);
        
        assert (confirmedPlayerVictories.getConfirmedVictories().size() == 1);
    }

    @Test
    public void testPlayerMoreDeclarationsThanVictories () throws PWCGException
    {   
        
        createPlayerDeclarations(2);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerAirResultsWithClaims(playerDeclarations);
        
        assert (confirmedPlayerVictories.getConfirmedVictories().size() == 0);
    }

}
