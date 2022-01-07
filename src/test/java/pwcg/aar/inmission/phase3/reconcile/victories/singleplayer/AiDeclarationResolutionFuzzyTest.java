package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
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
import pwcg.campaign.company.Company;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AiDeclarationResolutionFuzzyTest
{
    @Mock private AARMissionEvaluationData evaluationData;
    @Mock private Campaign campaign;
    @Mock private CampaignData campaignData;
    @Mock private Company squadron;
    @Mock private CampaignPersonnelManager personnelManager;
    @Mock private AARContext aarContext;
    @Mock private AARPreliminaryData preliminaryData;
    @Mock private PwcgMissionDataEvaluator pwcgMissionDataEvaluator;
    @Mock private VictorySorter victorySorter;
    @Mock private CrewMember player;
    @Mock private CrewMember aiSquadMember1;
    @Mock private CrewMember aiSquadMember2;
    @Mock private CrewMember aiNotSquadMember;
    
    private CrewMembers campaignMembersInmission = new CrewMembers();

    private List<LogVictory> fuzzyVictories = new ArrayList<>();        
    private List<LogVictory> emptyList = new ArrayList<>();        
    private List<CrewMember> players = new ArrayList<>();

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

        playerVictor.setCrewMemberSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        aiVictorOne.setCrewMemberSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1);
        aiVictorTwo.setCrewMemberSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 2);
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
        Mockito.when(aiSquadMember1.determineCountry(ArgumentMatchers.<Date>any())).thenReturn(CountryFactory.makeCountryByCountry(Country.GERMANY));
        Mockito.when(evaluationData.getPlaneInMissionBySerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1)).thenReturn(aiVictorOne);

        Mockito.when(aiSquadMember2.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER + 2);
        Mockito.when(aiSquadMember2.determineCountry(ArgumentMatchers.<Date>any())).thenReturn(CountryFactory.makeCountryByCountry(Country.GERMANY));
        Mockito.when(evaluationData.getPlaneInMissionBySerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 2)).thenReturn(aiVictorTwo);
        
        List<Company> playerSquadronsInMission = new ArrayList<>();
        playerSquadronsInMission.add(squadron);
        Mockito.when(preliminaryData.getPlayerSquadronsInMission()).thenReturn(playerSquadronsInMission);

        int squadronId = SquadronTestProfile.JASTA_11_PROFILE.getCompanyId();
        playerVictor.setSquadronId(squadronId);
        aiVictorOne.setSquadronId(squadronId);
        aiVictorTwo.setSquadronId(squadronId);
        Mockito.when(aiSquadMember1.getCompanyId()).thenReturn(squadronId);
        Mockito.when(aiSquadMember2.getCompanyId()).thenReturn(squadronId);
        Mockito.when(squadron.getCompanyId()).thenReturn(squadronId);
    }

    private void createVictory(Integer victimSerialNumber) throws PWCGException
    {        
        LogPlane victim = new LogPlane(4);
        victim.setCrewMemberSerialNumber(victimSerialNumber);
        victim.setCountry(CountryFactory.makeCountryByCountry(Country.RUSSIA));
        
        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictim(victim);
        fuzzyVictories.add(resultVictory);
   }
    
    @Test
    public void testAiOnlyOneFuzzyVictoryAward () throws PWCGException
    {   
        campaignMembersInmission.addToCrewMemberCollection(player);
        campaignMembersInmission.addToCrewMemberCollection(aiSquadMember1);

        AiDeclarationResolver declarationResolution = new AiDeclarationResolver(campaign, aarContext);
        ConfirmedVictories confirmedAiVictories = declarationResolution.determineAiAirResults(victorySorter);

        Assertions.assertTrue (confirmedAiVictories.getConfirmedVictories().size() == 1);
    }
    
    @Test
    public void testAiOneFuzzyVictoryPerCrewAward () throws PWCGException
    {   
        campaignMembersInmission.addToCrewMemberCollection(player);
        campaignMembersInmission.addToCrewMemberCollection(aiSquadMember1);
        campaignMembersInmission.addToCrewMemberCollection(aiSquadMember2);

        AiDeclarationResolver declarationResolution = new AiDeclarationResolver(campaign, aarContext);
        ConfirmedVictories confirmedAiVictories = declarationResolution.determineAiAirResults(victorySorter);
        
        Assertions.assertTrue (confirmedAiVictories.getConfirmedVictories().size() == 2);
    }
    
    @Test
    public void testAiFuzzyVictoryAwardFailedBecuseNoCrewOtherThanPlayer () throws PWCGException
    {   
        campaignMembersInmission.addToCrewMemberCollection(player);

        AiDeclarationResolver declarationResolution = new AiDeclarationResolver(campaign, aarContext);
        ConfirmedVictories confirmedAiVictories = declarationResolution.determineAiAirResults(victorySorter);
        
        Assertions.assertTrue (confirmedAiVictories.getConfirmedVictories().size() == 0);
    }
    
    @Test
    public void testAiFuzzyVictoryAwardFailedBecuseAllConfirmed () throws PWCGException
    {   
        campaignMembersInmission.addToCrewMemberCollection(player);
        campaignMembersInmission.addToCrewMemberCollection(aiSquadMember1);
        campaignMembersInmission.addToCrewMemberCollection(aiSquadMember2);

        for (LogVictory fuzzyVictory : fuzzyVictories)
        {
            fuzzyVictory.setConfirmed(true);
        }
        
        AiDeclarationResolver declarationResolution = new AiDeclarationResolver(campaign, aarContext);
        ConfirmedVictories confirmedAiVictories = declarationResolution.determineAiAirResults(victorySorter);
        
        Assertions.assertTrue (confirmedAiVictories.getConfirmedVictories().size() == 0);
    }
    
    @Test
    public void testAiFuzzyVictoryAwardFailedBecuseVictorIsNotCrewMember () throws PWCGException
    {   
        campaignMembersInmission.addToCrewMemberCollection(player);
        campaignMembersInmission.addToCrewMemberCollection(aiSquadMember1);
        campaignMembersInmission.addToCrewMemberCollection(aiSquadMember2);

        Mockito.when(aiSquadMember1.getCompanyId()).thenReturn(501004);
        Mockito.when(aiSquadMember2.getCompanyId()).thenReturn(501004);

        AiDeclarationResolver declarationResolution = new AiDeclarationResolver(campaign, aarContext);
        ConfirmedVictories confirmedAiVictories = declarationResolution.determineAiAirResults(victorySorter);
        
        Assertions.assertTrue (confirmedAiVictories.getConfirmedVictories().size() == 0);
    }
    
    @Test
    public void testAiFuzzyVictoryAwardFailedBecuseSameSide () throws PWCGException
    {   
        campaignMembersInmission.addToCrewMemberCollection(player);
        campaignMembersInmission.addToCrewMemberCollection(aiSquadMember1);
        campaignMembersInmission.addToCrewMemberCollection(aiSquadMember2);

        Mockito.when(aiSquadMember1.determineCountry(ArgumentMatchers.<Date>any())).thenReturn(CountryFactory.makeCountryByCountry(Country.RUSSIA));
        Mockito.when(aiSquadMember2.determineCountry(ArgumentMatchers.<Date>any())).thenReturn(CountryFactory.makeCountryByCountry(Country.RUSSIA));

        aiVictorOne.setCountry(CountryFactory.makeCountryByCountry(Country.RUSSIA));
        aiVictorTwo.setCountry(CountryFactory.makeCountryByCountry(Country.RUSSIA));

        AiDeclarationResolver declarationResolution = new AiDeclarationResolver(campaign, aarContext);
        ConfirmedVictories confirmedAiVictories = declarationResolution.determineAiAirResults(victorySorter);
        
        Assertions.assertTrue (confirmedAiVictories.getConfirmedVictories().size() == 0);
    }
}
