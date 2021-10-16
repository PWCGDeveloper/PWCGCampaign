package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.inmission.phase3.reconcile.victories.common.ConfirmedVictories;
import pwcg.aar.inmission.phase3.reconcile.victories.common.VictorySorter;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignData;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.PlaneTypeFactory;
import pwcg.campaign.plane.PwcgRoleCategory;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.product.fc.country.FCCountry;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PlayerDeclarationResolutionFuzzyVictoryTest
{
    @Mock private Campaign campaign;
    @Mock private CampaignData campaignData;
    @Mock private CampaignPersonnelManager personnelManager;
    @Mock private PlayerVictoryDeclaration mockPlayerDeclaration;
    @Mock private AARMissionEvaluationData evaluationData;
    @Mock private VictorySorter victorySorter;
    @Mock private SquadronMembers playerMembers;
    @Mock private SquadronMember player;
    @Mock private SquadronMember ai;
    @Mock private PlaneTypeFactory planeFactory;
    
    private Map<Integer, PlayerDeclarations> playerDeclarations = new HashMap<>();
    private PlayerDeclarations playerDeclarationSet;

    private List<LogVictory> fuzzyVictories = new ArrayList<>();        
    private List<LogVictory> emptyList = new ArrayList<>();        
    private List<SquadronMember> players = new ArrayList<>();

    private LogPlane playerVictor = new LogPlane(1);
    private LogPlane aiVictor = new LogPlane(2);
    
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        
        fuzzyVictories.clear();
        
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);

        playerVictor.setPilotSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        aiVictor.setPilotSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1);
        
        createMocks();
    }
    
    private void createMocks() throws PWCGException
    {
        players = new ArrayList<>();
        players.add(player);
        
        Mockito.when(victorySorter.getFuzzyAirVictories()).thenReturn(fuzzyVictories);
        Mockito.when(victorySorter.getFirmAirVictories()).thenReturn(emptyList);

        Mockito.when(personnelManager.getAnyCampaignMember(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER)).thenReturn(player);

        Mockito.when(evaluationData.getPlaneInMissionBySerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER)).thenReturn(playerVictor);

        Mockito.when(player.getCountry()).thenReturn(Country.GERMANY);
    }
    
    @Test
    public void testPlayerFuzzyVictoryAward () throws PWCGException
    {   
        
        createPlayerDeclarations(1);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001, "se5a", PwcgRoleCategory.FIGHTER);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002, "se5a", PwcgRoleCategory.FIGHTER);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1003, "se5a", PwcgRoleCategory.FIGHTER);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter, playerDeclarations);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerAirResultsWithClaims();
        
        Assertions.assertTrue (confirmedPlayerVictories.getConfirmedVictories().size() == 1);
    }
    
    @Test
    public void testPlayerMultipleFuzzyVictoryAwards () throws PWCGException
    {   
        
        createPlayerDeclarations(3);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001, "se5a", PwcgRoleCategory.FIGHTER);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002, "se5a", PwcgRoleCategory.FIGHTER);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1003, "se5a", PwcgRoleCategory.FIGHTER);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter, playerDeclarations);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerAirResultsWithClaims();
        
        Assertions.assertTrue (confirmedPlayerVictories.getConfirmedVictories().size() == 3);
    }
    
    @Test
    public void testPlayerTwoClaimsOneMatchOneNotExact () throws PWCGException
    {   
        
        createPlayerDeclarations(2);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001, "se5a", PwcgRoleCategory.FIGHTER);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002, "sopcamel", PwcgRoleCategory.FIGHTER);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1003, "sopcamel", PwcgRoleCategory.FIGHTER);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter, playerDeclarations);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerAirResultsWithClaims();
        
        Assertions.assertTrue (confirmedPlayerVictories.getConfirmedVictories().size() == 2);
    }
    
    @Test
    public void testPlayerTwoClaimsButWrongAircraftCategory () throws PWCGException
    {   
        
        createPlayerDeclarations(2);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001, "re8", PwcgRoleCategory.BOMBER);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002, "re8", PwcgRoleCategory.BOMBER);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1003, "re8", PwcgRoleCategory.BOMBER);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter, playerDeclarations);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerAirResultsWithClaims();
        
        Assertions.assertTrue (confirmedPlayerVictories.getConfirmedVictories().size() == 0);
    }
    
    @Test
    public void testPlayerOneInexactTwoWrongCategory () throws PWCGException
    {   
        
        createPlayerDeclarations(2);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001, "re8", PwcgRoleCategory.BOMBER);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002, "re8", PwcgRoleCategory.BOMBER);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1003, "sopcamel", PwcgRoleCategory.FIGHTER);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter, playerDeclarations);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerAirResultsWithClaims();
        
        Assertions.assertTrue (confirmedPlayerVictories.getConfirmedVictories().size() == 1);
    }

    @Test
    public void testPlayerMoreDeclarationsThanVictories () throws PWCGException
    {   
        
        createPlayerDeclarations(2);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter, playerDeclarations);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerAirResultsWithClaims();
        
        Assertions.assertTrue (confirmedPlayerVictories.getConfirmedVictories().size() == 0);
    }
    
    @Test
    public void testNoFriendlyVictories () throws PWCGException
    {   
        
        createPlayerDeclarations(1);
        createFriendlyVictory(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter, playerDeclarations);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerAirResultsWithClaims();
        
        Assertions.assertTrue (confirmedPlayerVictories.getConfirmedVictories().size() == 0);
    }

    private void createVictory(Integer victimSerialNumber, String aircraftType, PwcgRoleCategory approximateRole)
    {        
        LogPlane victim = new LogPlane(1);
        victim.setPilotSerialNumber(victimSerialNumber);
        victim.setVehicleType(aircraftType);
        victim.setRoleCategory(approximateRole);
        victim.setCountry(new FCCountry(Country.BRITAIN));

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setCrossedPlayerPath(true);

        fuzzyVictories.add(resultVictory);
    }

    private void createFriendlyVictory(Integer victorSerialNumber, Integer victimSerialNumber)
    {        
        LogPlane victim = new LogPlane(3);
        victim.setPilotSerialNumber(victimSerialNumber);
        victim.setVehicleType("albatrosd5");
        victim.setCountry(new FCCountry(Country.GERMANY));

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        resultVictory.setCrossedPlayerPath(true);
        
        fuzzyVictories.add(resultVictory);
    }

    private void createPlayerDeclarations(int numDeclarations) throws PWCGException
    {
        playerDeclarationSet = new PlayerDeclarations();
        for (int i = 0; i < numDeclarations; ++i)
        {
            PlayerVictoryDeclaration declaration = new PlayerVictoryDeclaration();
            declaration.confirmDeclaration(true, "S.E.5a");
            playerDeclarationSet.addDeclaration(declaration);
        }
        
        playerDeclarations.clear();
        playerDeclarations.put(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, playerDeclarationSet);
    }
}
