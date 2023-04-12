package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.aar.data.AARContext;
import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.inmission.phase3.reconcile.victories.common.ConfirmedVictories;
import pwcg.aar.inmission.phase3.reconcile.victories.common.VictorySorter;
import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.aar.prelim.PwcgMissionDataEvaluator;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignData;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AiDeclarationResolutionFuzzyTest
{
    @Mock private AARMissionEvaluationData evaluationData;
    @Mock private Campaign campaign;
    @Mock private CampaignData campaignData;
    @Mock private Squadron squadron;
    @Mock private CampaignPersonnelManager personnelManager;
    @Mock private AARContext aarContext;
    @Mock private AARPreliminaryData preliminaryData;
    @Mock private PwcgMissionDataEvaluator pwcgMissionDataEvaluator;
    @Mock private VictorySorter victorySorter;
    @Mock private SquadronMember player;
    @Mock private SquadronMember aiSquadMember1;
    @Mock private SquadronMember aiSquadMember2;
    @Mock private SquadronMember aiNotSquadMember;
    
    private SquadronMembers campaignMembersInmission = new SquadronMembers();

    private List<LogVictory> fuzzyVictories = new ArrayList<>();        
    private List<LogVictory> emptyList = new ArrayList<>();        
    private List<SquadronMember> players = new ArrayList<>();

    private LogPlane playerVictor = new LogPlane(1);
    private LogPlane aiVictorOne = new LogPlane(2);
    private LogPlane aiVictorTwo = new LogPlane(3);

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);

        fuzzyVictories.clear();
        campaignMembersInmission.clear();
        
        Mockito.when(aarContext.getMissionEvaluationData()).thenReturn(evaluationData);
        Mockito.when(aarContext.getPreliminaryData()).thenReturn(preliminaryData);
        Mockito.when(preliminaryData.getCampaignMembersInMission()).thenReturn(campaignMembersInmission);

        playerVictor.setPilotSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        aiVictorOne.setPilotSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1);
        aiVictorTwo.setPilotSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 2);
        playerVictor.setCountry(CountryFactory.makeCountryByCountry(Country.GERMANY));
        aiVictorOne.setCountry(CountryFactory.makeCountryByCountry(Country.GERMANY));
        aiVictorTwo.setCountry(CountryFactory.makeCountryByCountry(Country.GERMANY));
        
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1000);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002);
        
        Mockito.when(victorySorter.getFirmAirVictories()).thenReturn(emptyList);
        Mockito.when(victorySorter.getFirmBalloonVictories()).thenReturn(emptyList);
        Mockito.when(victorySorter.getFuzzyAirVictories()).thenReturn(fuzzyVictories);
        Mockito.when(victorySorter.getAllUnconfirmed()).thenReturn(emptyList);
        
        players = new ArrayList<>();
        players.add(player);

        Mockito.when(player.getSerialNumber()).thenReturn(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);

        Mockito.when(aiSquadMember1.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1);
        Mockito.when(aiSquadMember1.determineCountry()).thenReturn(CountryFactory.makeCountryByCountry(Country.GERMANY));
        Mockito.when(evaluationData.getPlaneInMissionBySerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1)).thenReturn(aiVictorOne);

        Mockito.when(aiSquadMember2.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER + 2);
        Mockito.when(aiSquadMember2.determineCountry()).thenReturn(CountryFactory.makeCountryByCountry(Country.GERMANY));
        Mockito.when(evaluationData.getPlaneInMissionBySerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 2)).thenReturn(aiVictorTwo);
        
        List<Squadron> playerSquadronsInMission = new ArrayList<>();
        playerSquadronsInMission.add(squadron);
        Mockito.when(preliminaryData.getPlayerSquadronsInMission()).thenReturn(playerSquadronsInMission);

        int squadronId = SquadronTestProfile.JASTA_11_PROFILE.getSquadronId();
        playerVictor.setSquadronId(squadronId);
        aiVictorOne.setSquadronId(squadronId);
        aiVictorTwo.setSquadronId(squadronId);
        Mockito.when(aiSquadMember1.getSquadronId()).thenReturn(squadronId);
        Mockito.when(aiSquadMember2.getSquadronId()).thenReturn(squadronId);
        Mockito.when(squadron.getSquadronId()).thenReturn(squadronId);
    }

    private void createVictory(Integer victimSerialNumber) throws PWCGException
    {        
        LogPlane victim = new LogPlane(4);
        victim.setPilotSerialNumber(victimSerialNumber);
        victim.setCountry(CountryFactory.makeCountryByCountry(Country.RUSSIA));
        
        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        fuzzyVictories.add(resultVictory);
   }
    
    @Test
    public void testAiOnlyOneFuzzyVictoryAward () throws PWCGException
    {   
        campaignMembersInmission.addToSquadronMemberCollection(player);
        campaignMembersInmission.addToSquadronMemberCollection(aiSquadMember1);

        AiDeclarationResolver declarationResolution = new AiDeclarationResolver(campaign, aarContext);
        ConfirmedVictories confirmedAiVictories = declarationResolution.determineAiAirResults(victorySorter);

        Assertions.assertTrue (confirmedAiVictories.getConfirmedVictories().size() == 1);
    }
    
    @Test
    public void testAiOneFuzzyVictoryPerCrewAward () throws PWCGException
    {   
        campaignMembersInmission.addToSquadronMemberCollection(player);
        campaignMembersInmission.addToSquadronMemberCollection(aiSquadMember1);
        campaignMembersInmission.addToSquadronMemberCollection(aiSquadMember2);

        AiDeclarationResolver declarationResolution = new AiDeclarationResolver(campaign, aarContext);
        ConfirmedVictories confirmedAiVictories = declarationResolution.determineAiAirResults(victorySorter);
        
        Assertions.assertTrue (confirmedAiVictories.getConfirmedVictories().size() == 2);
    }
    
    @Test
    public void testAiFuzzyVictoryAwardFailedBecuseNoCrewOtherThanPlayer () throws PWCGException
    {   
        campaignMembersInmission.addToSquadronMemberCollection(player);

        AiDeclarationResolver declarationResolution = new AiDeclarationResolver(campaign, aarContext);
        ConfirmedVictories confirmedAiVictories = declarationResolution.determineAiAirResults(victorySorter);
        
        Assertions.assertTrue (confirmedAiVictories.getConfirmedVictories().size() == 0);
    }
    
    @Test
    public void testAiFuzzyVictoryAwardFailedBecuseAllConfirmed () throws PWCGException
    {   
        campaignMembersInmission.addToSquadronMemberCollection(player);
        campaignMembersInmission.addToSquadronMemberCollection(aiSquadMember1);
        campaignMembersInmission.addToSquadronMemberCollection(aiSquadMember2);

        for (LogVictory fuzzyVictory : fuzzyVictories)
        {
            fuzzyVictory.setConfirmed(true);
        }
        
        AiDeclarationResolver declarationResolution = new AiDeclarationResolver(campaign, aarContext);
        ConfirmedVictories confirmedAiVictories = declarationResolution.determineAiAirResults(victorySorter);
        
        Assertions.assertTrue (confirmedAiVictories.getConfirmedVictories().size() == 0);
    }
    
    @Test
    public void testAiFuzzyVictoryAwardFailedBecuseVictorIsNotSquadronMember () throws PWCGException
    {   
        campaignMembersInmission.addToSquadronMemberCollection(player);
        campaignMembersInmission.addToSquadronMemberCollection(aiSquadMember1);
        campaignMembersInmission.addToSquadronMemberCollection(aiSquadMember2);

        Mockito.when(aiSquadMember1.getSquadronId()).thenReturn(501004);
        Mockito.when(aiSquadMember2.getSquadronId()).thenReturn(501004);

        AiDeclarationResolver declarationResolution = new AiDeclarationResolver(campaign, aarContext);
        ConfirmedVictories confirmedAiVictories = declarationResolution.determineAiAirResults(victorySorter);
        
        Assertions.assertTrue (confirmedAiVictories.getConfirmedVictories().size() == 0);
    }
    
    @Test
    public void testAiFuzzyVictoryAwardFailedBecuseSameSide () throws PWCGException
    {   
        campaignMembersInmission.addToSquadronMemberCollection(player);
        campaignMembersInmission.addToSquadronMemberCollection(aiSquadMember1);
        campaignMembersInmission.addToSquadronMemberCollection(aiSquadMember2);

        Mockito.when(aiSquadMember1.determineCountry()).thenReturn(CountryFactory.makeCountryByCountry(Country.RUSSIA));
        Mockito.when(aiSquadMember2.determineCountry()).thenReturn(CountryFactory.makeCountryByCountry(Country.RUSSIA));

        aiVictorOne.setCountry(CountryFactory.makeCountryByCountry(Country.RUSSIA));
        aiVictorTwo.setCountry(CountryFactory.makeCountryByCountry(Country.RUSSIA));

        AiDeclarationResolver declarationResolution = new AiDeclarationResolver(campaign, aarContext);
        ConfirmedVictories confirmedAiVictories = declarationResolution.determineAiAirResults(victorySorter);
        
        Assertions.assertTrue (confirmedAiVictories.getConfirmedVictories().size() == 0);
    }
}
