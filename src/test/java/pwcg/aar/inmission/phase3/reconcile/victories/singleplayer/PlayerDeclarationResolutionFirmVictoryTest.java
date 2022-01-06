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
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.plane.TankTypeFactory;
import pwcg.core.exception.PWCGException;
import pwcg.product.bos.country.BoSCountry;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PlayerDeclarationResolutionFirmVictoryTest
{
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

    private List<LogVictory> firmVictories = new ArrayList<>();        
    private List<LogVictory> emptyList = new ArrayList<>();        
    private List<CrewMember> players = new ArrayList<>();

    private LogPlane playerVictor = new LogPlane(1);
    private LogPlane aiVictor = new LogPlane(2);
    
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        
        firmVictories.clear();
        
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);

        playerVictor.setCrewMemberSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        playerVictor.setCountry(new BoSCountry(Country.FRANCE));
        
        aiVictor.setCrewMemberSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1);
        aiVictor.setCountry(new BoSCountry(Country.FRANCE));
        
        createMocks();
    }
    
    private void createMocks() throws PWCGException
    {
        players = new ArrayList<>();
        players.add(player);

        Mockito.when(victorySorter.getFirmAirVictories()).thenReturn(firmVictories);
        Mockito.when(victorySorter.getFirmBalloonVictories()).thenReturn(emptyList);
        Mockito.when(victorySorter.getFuzzyAirVictories()).thenReturn(emptyList);
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);

        Mockito.when(personnelManager.getAnyCampaignMember(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER)).thenReturn(player);
        Mockito.when(personnelManager.getAnyCampaignMember(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1)).thenReturn(ai);

        Mockito.when(evaluationData.getPlaneInMissionBySerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER)).thenReturn(playerVictor);

        Mockito.when(player.isPlayer()).thenReturn(true);
        Mockito.when(player.getCountry()).thenReturn(Country.FRANCE);
        Mockito.when(player.getSerialNumber()).thenReturn(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
    }
    
    @Test
    public void testPlayerFirmVictoryAward () throws PWCGException
    {   
        
        createPlayerDeclarations(1);
        createVictory(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001);
        createVictory(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002);
        createVictory(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1003);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter, playerDeclarations);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerAirResultsWithClaims();
        
        Assertions.assertTrue (confirmedPlayerVictories.getConfirmedVictories().size() == 1);
    }
    
    @Test
    public void testPlayerMultipleFirmVictoryAwards () throws PWCGException
    {   
        
        createPlayerDeclarations(2);
        createVictory(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001);
        createVictory(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002);
        createVictory(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1003);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter, playerDeclarations);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerAirResultsWithClaims();
        
        Assertions.assertTrue (confirmedPlayerVictories.getConfirmedVictories().size() == 2);
    }
    
    @Test
    public void testPlayerOneAwardForTwoDeclarationsButOnlyOneVictory () throws PWCGException
    {   
        
        createPlayerDeclarations(2);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002);
        createVictory(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1003);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter, playerDeclarations);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerAirResultsWithClaims();
        
        Assertions.assertTrue (confirmedPlayerVictories.getConfirmedVictories().size() == 1);
    }
    
    @Test
    public void testPlayerZeroAwardForTwoDeclarationsButNoVictories () throws PWCGException
    {   
        createPlayerDeclarations(2);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1003);

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


    private void createVictory(Integer victorSerialNumber, Integer victimSerialNumber)
    {        
        LogPlane victim = new LogPlane(3);
        victim.setCrewMemberSerialNumber(victimSerialNumber);
        victim.setVehicleType("albatrosd5");
        victim.setCountry(new BoSCountry(Country.GERMANY));

        LogPlane victor = new LogPlane(4);
        victor.setVehicleType("spad13");
        victor.setCrewMemberSerialNumber(victorSerialNumber);
        victor.setCountry(new BoSCountry(Country.FRANCE));

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictor(victor);
        resultVictory.setVictim(victim);
        
        firmVictories.add(resultVictory);
    }

    private void createFriendlyVictory(Integer victorSerialNumber, Integer victimSerialNumber)
    {        
        LogPlane victim = new LogPlane(3);
        victim.setCrewMemberSerialNumber(victimSerialNumber);
        victim.setVehicleType("spad13");
        victim.setCountry(new BoSCountry(Country.FRANCE));

        LogPlane victor = new LogPlane(4);
        victor.setVehicleType("spad13");
        victor.setCrewMemberSerialNumber(victorSerialNumber);
        victor.setCountry(new BoSCountry(Country.FRANCE));

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictor(victor);
        resultVictory.setVictim(victim);
        
        firmVictories.add(resultVictory);
    }

    private void createPlayerDeclarations(int numDeclarations) throws PWCGException
    {
        playerDeclarationSet = new PlayerDeclarations();
        for (int i = 0; i < numDeclarations; ++i)
        {
            PlayerVictoryDeclaration declaration = new PlayerVictoryDeclaration();
            declaration.confirmDeclaration(true, "albatrosd5");
            playerDeclarationSet.addDeclaration(declaration);
        }        
        
        playerDeclarations.clear();
        playerDeclarations.put(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, playerDeclarationSet);
    }

}
