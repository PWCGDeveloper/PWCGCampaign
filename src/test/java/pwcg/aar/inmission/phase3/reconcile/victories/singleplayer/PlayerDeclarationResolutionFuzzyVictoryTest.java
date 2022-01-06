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

import pwcg.aar.inmission.phase2.logeval.AARDamageStatus;
import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogDamage;
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
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.plane.TankTypeFactory;
import pwcg.campaign.plane.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;
import pwcg.product.bos.country.BoSCountry;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PlayerDeclarationResolutionFuzzyVictoryTest
{
    private String PLAYER_PLANE_LOG_ID = "999";
    
    @Mock private Campaign campaign;
    @Mock private CampaignData campaignData;
    @Mock private CampaignPersonnelManager personnelManager;
    @Mock private PlayerVictoryDeclaration mockPlayerDeclaration;
    @Mock private AARMissionEvaluationData evaluationData;
    @Mock private VictorySorter victorySorter;
    @Mock private CrewMembers playerMembers;
    @Mock private CrewMember player;
    @Mock private CrewMember ai;
    @Mock private TankTypeFactory planeFactory;
    
    private Map<Integer, PlayerDeclarations> playerDeclarations = new HashMap<>();
    private PlayerDeclarations playerDeclarationSet;

    private List<LogVictory> fuzzyVictories = new ArrayList<>();        
    private List<LogVictory> emptyList = new ArrayList<>();        
    private List<CrewMember> players = new ArrayList<>();

    private LogPlane playerVictor = new LogPlane(1);
    private LogPlane aiVictor = new LogPlane(2);
    
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        
        fuzzyVictories.clear();
        
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);

        playerVictor.setCrewMemberSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        playerVictor.setId(PLAYER_PLANE_LOG_ID);
        aiVictor.setCrewMemberSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1);
        
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
        createVictoryWithPlayerDamage(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001, "se5a", PwcgRoleCategory.FIGHTER);
        createVictoryWithPlayerDamage(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002, "se5a", PwcgRoleCategory.FIGHTER);
        createVictoryWithPlayerDamage(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1003, "se5a", PwcgRoleCategory.FIGHTER);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter, playerDeclarations);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerAirResultsWithClaims();
        
        Assertions.assertTrue (confirmedPlayerVictories.getConfirmedVictories().size() == 1);
    }
    
    @Test
    public void testPlayerMultipleFuzzyVictoryAwards () throws PWCGException
    {   
        
        createPlayerDeclarations(3);
        createVictoryWithPlayerDamage(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001, "se5a", PwcgRoleCategory.FIGHTER);
        createVictoryWithPlayerDamage(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002, "se5a", PwcgRoleCategory.FIGHTER);
        createVictoryWithPlayerDamage(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1003, "se5a", PwcgRoleCategory.FIGHTER);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter, playerDeclarations);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerAirResultsWithClaims();
        
        Assertions.assertTrue (confirmedPlayerVictories.getConfirmedVictories().size() == 3);
    }
    
    @Test
    public void testPlayerTwoClaimsOneMatchOneNotExact () throws PWCGException
    {   
        
        createPlayerDeclarations(2);
        createVictoryWithPlayerDamage(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001, "se5a", PwcgRoleCategory.FIGHTER);
        createVictoryWithPlayerDamage(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002, "sopcamel", PwcgRoleCategory.FIGHTER);
        createVictoryWithPlayerDamage(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1003, "sopcamel", PwcgRoleCategory.FIGHTER);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter, playerDeclarations);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerAirResultsWithClaims();
        
        Assertions.assertTrue (confirmedPlayerVictories.getConfirmedVictories().size() == 2);
    }
    
    @Test
    public void testPlayerTwoClaimsButWrongAircraftCategory () throws PWCGException
    {   
        
        createPlayerDeclarations(2);
        createVictoryWithPlayerDamage(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001, "re8", PwcgRoleCategory.BOMBER);
        createVictoryWithPlayerDamage(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002, "re8", PwcgRoleCategory.BOMBER);
        createVictoryWithPlayerDamage(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1003, "re8", PwcgRoleCategory.BOMBER);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter, playerDeclarations);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerAirResultsWithClaims();
        
        Assertions.assertTrue (confirmedPlayerVictories.getConfirmedVictories().size() == 0);
    }
    
    @Test
    public void testPlayerOneInexactTwoWrongCategory () throws PWCGException
    {   
        
        createPlayerDeclarations(2);
        createVictoryWithPlayerDamage(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001, "re8", PwcgRoleCategory.BOMBER);
        createVictoryWithPlayerDamage(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002, "re8", PwcgRoleCategory.BOMBER);
        createVictoryWithPlayerDamage(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1003, "sopcamel", PwcgRoleCategory.FIGHTER);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter, playerDeclarations);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerAirResultsWithClaims();
        
        Assertions.assertTrue (confirmedPlayerVictories.getConfirmedVictories().size() == 1);
    }
    
    @Test
    public void testPlayerOneInexactTwoWrongCategoryNoPlayerDamage () throws PWCGException
    {   
        
        createPlayerDeclarations(2);
        createVictoryNoPlayerDamage(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001, "re8", PwcgRoleCategory.BOMBER);
        createVictoryNoPlayerDamage(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002, "re8", PwcgRoleCategory.BOMBER);
        createVictoryNoPlayerDamage(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1003, "sopcamel", PwcgRoleCategory.FIGHTER);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter, playerDeclarations);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerAirResultsWithClaims();
        
        Assertions.assertTrue (confirmedPlayerVictories.getConfirmedVictories().size() == 0);
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

    private void createVictoryWithPlayerDamage(Integer victimSerialNumber, String aircraftType, PwcgRoleCategory approximateRole)
    {        
        LogPlane victim = createVictimPlane(victimSerialNumber, aircraftType, approximateRole);

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        
        AARDamageStatus damageStatus = new AARDamageStatus(victim.getId());
        damageStatus.addDamage(PLAYER_PLANE_LOG_ID, new LogDamage(1));
        resultVictory.setDamageInformation(damageStatus);
        
        fuzzyVictories.add(resultVictory);
    }

    private void createVictoryNoPlayerDamage(Integer victimSerialNumber, String aircraftType, PwcgRoleCategory approximateRole)
    {        
        LogPlane victim = createVictimPlane(victimSerialNumber, aircraftType, approximateRole);

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        fuzzyVictories.add(resultVictory);
    }

    private LogPlane createVictimPlane(Integer victimSerialNumber, String aircraftType, PwcgRoleCategory approximateRole)
    {
        LogPlane victim = new LogPlane(1);
        victim.setCrewMemberSerialNumber(victimSerialNumber);
        victim.setVehicleType(aircraftType);
        victim.setRoleCategory(approximateRole);
        victim.setCountry(new BoSCountry(Country.BRITAIN));
        return victim;
    }

    private void createFriendlyVictory(Integer victorSerialNumber, Integer victimSerialNumber)
    {        
        LogPlane victim = new LogPlane(3);
        victim.setCrewMemberSerialNumber(victimSerialNumber);
        victim.setVehicleType("albatrosd5");
        victim.setCountry(new BoSCountry(Country.GERMANY));

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        
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
