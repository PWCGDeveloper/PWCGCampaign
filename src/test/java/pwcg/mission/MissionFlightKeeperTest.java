package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class MissionFlightKeeperTest
{
    @Mock private  Campaign campaign;
    @Mock private  Mission mission;
    @Mock private  MissionFlightBuilder missionFlightBuilder;
    @Mock private ConfigManagerCampaign configManagerCampaign;
    @Mock private  MissionSquadronRecorder missionSquadronChooser;
    @Mock private  IFlight alliedPlayerFlight;
    @Mock private  IFlight alliedAiFlight2;
    @Mock private  IFlight alliedAiFlight3;
    @Mock private  IFlight alliedAiFlight4;
    @Mock private  IFlight alliedAiFlight5;
    @Mock private  IFlight alliedAiFlight6;
    @Mock private  IFlight axisAiFlight1;
    @Mock private  IFlight axisAiFlight2;
    @Mock private  IFlight axisAiFlight3;
    @Mock private  IFlight axisAiFlight4;
    @Mock private  IFlight axisAiFlight5;
    @Mock private  IFlight axisAiFlight6;
    @Mock private  IFlight alliedPlayerFlight1;
    @Mock private  IFlight alliedPlayerFlight2;
    @Mock private  IFlight alliedPlayerFlight3;
    @Mock private  IFlight axisPlayerFlight1;
    @Mock private  IFlight axisPlayerFlight2;
    @Mock private  IFlight axisPlayerFlight3;
    @Mock private  Squadron axisSquadron1;
    @Mock private  Squadron axisSquadron2;
    @Mock private  Squadron axisSquadron3;
    @Mock private  Squadron axisSquadron4;
    @Mock private  Squadron axisSquadron5;
    @Mock private  Squadron alliedPlayerSquadron;
    @Mock private  Squadron alliedSquadron1;
    @Mock private  Squadron alliedSquadron2;
    @Mock private  Squadron alliedSquadron3;
    @Mock private  Squadron alliedSquadron4;
    @Mock private  Squadron alliedSquadron5;

    @Mock private  FlightInformation playerFlightInformation;
    @Mock private  FlightInformation opposingFlightInformation;
    @Mock private  FlightInformation flightInformation;

    private  List<IFlight> alliedAiFlights = new ArrayList<>(); 
    private  List<IFlight> axisAiFlights = new ArrayList<>(); 
    private  List<IFlight> alliedPlayerFlights = new ArrayList<>(); 
    private  List<IFlight> axisPlayerFlights = new ArrayList<>(); 
    private  List<IFlight> allAiFlights = new ArrayList<>(); 
    
    @Before
    public void fighterFlightTests() throws PWCGException
    {
        alliedAiFlights = new ArrayList<>(); 
        axisAiFlights = new ArrayList<>(); 
        alliedPlayerFlights = new ArrayList<>(); 
        axisPlayerFlights = new ArrayList<>(); 
        allAiFlights = new ArrayList<>(); 
        
        Mockito.when(axisSquadron1.determineSide()).thenReturn(Side.AXIS);
        Mockito.when(axisSquadron2.determineSide()).thenReturn(Side.AXIS);
        Mockito.when(axisSquadron3.determineSide()).thenReturn(Side.AXIS);
        Mockito.when(axisSquadron4.determineSide()).thenReturn(Side.AXIS);
        Mockito.when(axisSquadron5.determineSide()).thenReturn(Side.AXIS);
        
        Mockito.when(axisSquadron1.getSquadronId()).thenReturn(200001);
        Mockito.when(axisSquadron2.getSquadronId()).thenReturn(200002);
        Mockito.when(axisSquadron3.getSquadronId()).thenReturn(200003);
        Mockito.when(axisSquadron4.getSquadronId()).thenReturn(200004);
        Mockito.when(axisSquadron5.getSquadronId()).thenReturn(200005);

        Mockito.when(alliedPlayerSquadron.determineSide()).thenReturn(Side.ALLIED);
        Mockito.when(alliedPlayerSquadron.getSquadronId()).thenReturn(100000);

        Mockito.when(alliedSquadron1.determineSide()).thenReturn(Side.ALLIED);
        Mockito.when(alliedSquadron2.determineSide()).thenReturn(Side.ALLIED);
        Mockito.when(alliedSquadron3.determineSide()).thenReturn(Side.ALLIED);
        Mockito.when(alliedSquadron4.determineSide()).thenReturn(Side.ALLIED);
        Mockito.when(alliedSquadron5.determineSide()).thenReturn(Side.ALLIED);
        
        Mockito.when(alliedSquadron1.getSquadronId()).thenReturn(100001);
        Mockito.when(alliedSquadron2.getSquadronId()).thenReturn(100002);
        Mockito.when(alliedSquadron3.getSquadronId()).thenReturn(100003);
        Mockito.when(alliedSquadron4.getSquadronId()).thenReturn(100004);
        Mockito.when(alliedSquadron5.getSquadronId()).thenReturn(100005);

        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(mission.getCampaign()).thenReturn(campaign);
        Mockito.when(mission.getMissionFlightBuilder()).thenReturn(missionFlightBuilder);
        Mockito.when(mission.getMissionSquadronRecorder()).thenReturn(missionSquadronChooser);
        Mockito.when(missionFlightBuilder.getPlayerFlightsForSide(Side.ALLIED)).thenReturn(alliedPlayerFlights);
        Mockito.when(missionFlightBuilder.getPlayerFlightsForSide(Side.AXIS)).thenReturn(axisPlayerFlights);

        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManagerCampaign);
        Mockito.when(campaign.isFighterCampaign()).thenReturn(true);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD(SquadronTestProfile.RAF_184_PROFILE.getDateString()));
        
        Mockito.when(configManagerCampaign.getStringConfigParam(ConfigItemKeys.SimpleConfigCpuAllowanceKey)).thenReturn(ConfigSimple.CONFIG_LEVEL_MED);
        
        Mockito.when(configManagerCampaign.getIntConfigParam(ConfigItemKeys.AlliedFlightsToKeepKey)).thenReturn(5);
        Mockito.when(configManagerCampaign.getIntConfigParam(ConfigItemKeys.AxisFlightsToKeepKey)).thenReturn(3);
        Mockito.when(configManagerCampaign.getIntConfigParam(ConfigItemKeys.AiFighterFlightsForGroundCampaignMaxKey)).thenReturn(1);
        Mockito.when(configManagerCampaign.getIntConfigParam(ConfigItemKeys.AiFighterFlightsForFighterCampaignMaxKey)).thenReturn(3);
        
        Mockito.when(alliedPlayerFlight.getFlightId()).thenReturn(10001);
        Mockito.when(alliedPlayerFlight.isPlayerFlight()).thenReturn(true);
        Mockito.when(alliedPlayerFlight.getFlightType()).thenReturn(FlightTypes.PATROL);
        Mockito.when(alliedPlayerFlight.getSquadron()).thenReturn(alliedPlayerSquadron);
        Mockito.when(alliedPlayerFlight.getClosestContactWithPlayerDistance()).thenReturn(1000.0);
        Mockito.when(alliedPlayerFlight.getFlightInformation()).thenReturn(playerFlightInformation);
        Mockito.when(alliedPlayerFlight.getMission()).thenReturn(mission);

        Mockito.when(alliedAiFlight2.getFlightId()).thenReturn(10002);
        Mockito.when(alliedAiFlight2.isPlayerFlight()).thenReturn(false);
        Mockito.when(alliedAiFlight2.getFlightType()).thenReturn(FlightTypes.PATROL);
        Mockito.when(alliedAiFlight2.getSquadron()).thenReturn(alliedSquadron1);
        Mockito.when(alliedAiFlight2.getClosestContactWithPlayerDistance()).thenReturn(3000.0);
        Mockito.when(alliedAiFlight2.getFlightInformation()).thenReturn(flightInformation);
        Mockito.when(alliedAiFlight2.getMission()).thenReturn(mission);

        Mockito.when(alliedAiFlight3.isPlayerFlight()).thenReturn(false);
        Mockito.when(alliedAiFlight3.getFlightType()).thenReturn(FlightTypes.PATROL);
        Mockito.when(alliedAiFlight3.getSquadron()).thenReturn(alliedSquadron2);
        Mockito.when(alliedAiFlight3.getClosestContactWithPlayerDistance()).thenReturn(5000.0);
        Mockito.when(alliedAiFlight3.getFlightInformation()).thenReturn(flightInformation);

        Mockito.when(alliedAiFlight4.getFlightId()).thenReturn(10004);
        Mockito.when(alliedAiFlight4.isPlayerFlight()).thenReturn(false);
        Mockito.when(alliedAiFlight4.getFlightType()).thenReturn(FlightTypes.BOMB);
        Mockito.when(alliedAiFlight4.getSquadron()).thenReturn(alliedSquadron3);
        Mockito.when(alliedAiFlight4.getClosestContactWithPlayerDistance()).thenReturn(2000.0);
        Mockito.when(alliedAiFlight4.getFlightInformation()).thenReturn(flightInformation);
        Mockito.when(alliedAiFlight4.getMission()).thenReturn(mission);

        Mockito.when(alliedAiFlight5.isPlayerFlight()).thenReturn(false);
        Mockito.when(alliedAiFlight5.getFlightType()).thenReturn(FlightTypes.BOMB);
        Mockito.when(alliedAiFlight5.getSquadron()).thenReturn(alliedSquadron4);
        Mockito.when(alliedAiFlight5.getClosestContactWithPlayerDistance()).thenReturn(2500.0);
        Mockito.when(alliedAiFlight5.getFlightInformation()).thenReturn(flightInformation);

        Mockito.when(alliedAiFlight6.getFlightId()).thenReturn(10006);
        Mockito.when(alliedAiFlight6.isPlayerFlight()).thenReturn(false);
        Mockito.when(alliedAiFlight6.getFlightType()).thenReturn(FlightTypes.GROUND_ATTACK);
        Mockito.when(alliedAiFlight6.getSquadron()).thenReturn(alliedSquadron5);
        Mockito.when(alliedAiFlight6.getClosestContactWithPlayerDistance()).thenReturn(4000.0);
        Mockito.when(alliedAiFlight6.getFlightInformation()).thenReturn(flightInformation);
        Mockito.when(alliedAiFlight6.getMission()).thenReturn(mission);

        Mockito.when(axisAiFlight1.getFlightId()).thenReturn(20001);
        Mockito.when(axisAiFlight1.isPlayerFlight()).thenReturn(false);
        Mockito.when(axisAiFlight1.getFlightType()).thenReturn(FlightTypes.PATROL);
        Mockito.when(axisAiFlight1.getSquadron()).thenReturn(axisSquadron1);
        Mockito.when(axisAiFlight1.getClosestContactWithPlayerDistance()).thenReturn(1000.0);
        Mockito.when(axisAiFlight1.getFlightInformation()).thenReturn(flightInformation);
        Mockito.when(axisAiFlight1.getMission()).thenReturn(mission);

        Mockito.when(axisAiFlight2.getFlightId()).thenReturn(20002);
        Mockito.when(axisAiFlight3.isPlayerFlight()).thenReturn(false);
        Mockito.when(axisAiFlight2.getFlightType()).thenReturn(FlightTypes.PATROL);
        Mockito.when(axisAiFlight2.getSquadron()).thenReturn(axisSquadron2);
        Mockito.when(axisAiFlight2.getClosestContactWithPlayerDistance()).thenReturn(3000.0);
        Mockito.when(axisAiFlight2.getFlightInformation()).thenReturn(flightInformation);
        Mockito.when(axisAiFlight2.getMission()).thenReturn(mission);

        Mockito.when(axisAiFlight3.getFlightId()).thenReturn(20003);
        Mockito.when(axisAiFlight3.isPlayerFlight()).thenReturn(false);
        Mockito.when(axisAiFlight3.getFlightType()).thenReturn(FlightTypes.PATROL);
        Mockito.when(axisAiFlight3.getSquadron()).thenReturn(axisSquadron3);
        Mockito.when(axisAiFlight3.getClosestContactWithPlayerDistance()).thenReturn(5000.0);
        Mockito.when(axisAiFlight3.getFlightInformation()).thenReturn(flightInformation);
        Mockito.when(axisAiFlight3.getMission()).thenReturn(mission);

        Mockito.when(axisAiFlight4.getFlightId()).thenReturn(20004);
        Mockito.when(axisAiFlight4.isPlayerFlight()).thenReturn(false);
        Mockito.when(axisAiFlight4.getFlightType()).thenReturn(FlightTypes.BOMB);
        Mockito.when(axisAiFlight4.getSquadron()).thenReturn(axisSquadron4);
        Mockito.when(axisAiFlight4.getClosestContactWithPlayerDistance()).thenReturn(2000.0);
        Mockito.when(axisAiFlight4.getFlightInformation()).thenReturn(flightInformation);
        Mockito.when(axisAiFlight4.getMission()).thenReturn(mission);

        Mockito.when(axisAiFlight5.isPlayerFlight()).thenReturn(false);
        Mockito.when(axisAiFlight5.getFlightType()).thenReturn(FlightTypes.BOMB);
        Mockito.when(axisAiFlight5.getSquadron()).thenReturn(axisSquadron5);
        Mockito.when(axisAiFlight5.getClosestContactWithPlayerDistance()).thenReturn(4000.0);
        Mockito.when(axisAiFlight5.getFlightInformation()).thenReturn(flightInformation);

        Mockito.when(missionFlightBuilder.getAiFlights()).thenReturn(alliedAiFlights);
        Mockito.when(playerFlightInformation.isPlayerFlight()).thenReturn(true);
        Mockito.when(flightInformation.isPlayerFlight()).thenReturn(false);
        Mockito.when(flightInformation.isOpposingFlight()).thenReturn(false);
        Mockito.when(opposingFlightInformation.isPlayerFlight()).thenReturn(false);
        Mockito.when(opposingFlightInformation.isOpposingFlight()).thenReturn(true);        
    }
    
    @Test
    public void singlePlayerFighterTest() throws PWCGException
    {
        Campaign coopCampaign = CampaignCache.makeCampaign(SquadronTestProfile.RAF_184_PROFILE);
        coopCampaign.setCampaignConfigManager(configManagerCampaign);

        Mockito.when(missionFlightBuilder.hasPlayerFlightWithFlightTypes(ArgumentMatchers.anyList())).thenReturn(true);

        alliedPlayerFlights.add(alliedPlayerFlight1);
        
        alliedAiFlights.add(alliedPlayerFlight);
        alliedAiFlights.add(alliedAiFlight2);
        alliedAiFlights.add(alliedAiFlight3);
        alliedAiFlights.add(alliedAiFlight4);
        alliedAiFlights.add(alliedAiFlight5);
        alliedAiFlights.add(alliedAiFlight6);
        
        axisAiFlights.add(axisAiFlight1);
        axisAiFlights.add(axisAiFlight2);
        axisAiFlights.add(axisAiFlight3);
        axisAiFlights.add(axisAiFlight4);
        axisAiFlights.add(axisAiFlight5);
        
        allAiFlights.addAll(axisAiFlights);
        allAiFlights.addAll(alliedAiFlights);
        Mockito.when(missionFlightBuilder.getAiFlights()).thenReturn(allAiFlights);

        MissionFlightKeeper missionFlightKeeper = new MissionFlightKeeper(coopCampaign, mission);
        List<IFlight> aiFlightsKept = missionFlightKeeper.keepLimitedFlights();
        
        assert(aiFlightsKept.size() == 7);
        assert(listHasFlight(aiFlightsKept, 10001));
        assert(listHasFlight(aiFlightsKept, 10002));
        assert(listHasFlight(aiFlightsKept, 10004));
        assert(listHasFlight(aiFlightsKept, 10006));

        assert(listHasFlight(aiFlightsKept, 20001));
        assert(listHasFlight(aiFlightsKept, 20002));
        assert(listHasFlight(aiFlightsKept, 20004));
    }
    
    @Test
    public void singlePlayerFighterWithOpposingTest() throws PWCGException
    {
        Mockito.when(missionFlightBuilder.hasPlayerFlightWithFlightTypes(ArgumentMatchers.anyList())).thenReturn(true);
        Mockito.when(axisAiFlight3.getFlightInformation()).thenReturn(opposingFlightInformation);

        alliedPlayerFlights.add(alliedPlayerFlight);
        
        alliedAiFlights.add(alliedPlayerFlight);
        alliedAiFlights.add(alliedAiFlight2);
        alliedAiFlights.add(alliedAiFlight3);
        alliedAiFlights.add(alliedAiFlight4);
        alliedAiFlights.add(alliedAiFlight5);
        alliedAiFlights.add(alliedAiFlight6);
        
        axisAiFlights.add(axisAiFlight1);
        axisAiFlights.add(axisAiFlight2);
        axisAiFlights.add(axisAiFlight3);
        axisAiFlights.add(axisAiFlight4);
        axisAiFlights.add(axisAiFlight5);
        
        allAiFlights.addAll(axisAiFlights);
        allAiFlights.addAll(alliedAiFlights);
        Mockito.when(missionFlightBuilder.getAiFlights()).thenReturn(allAiFlights);

        MissionFlightKeeper missionFlightKeeper = new MissionFlightKeeper(campaign, mission);
        List<IFlight> aiFlightsKept = missionFlightKeeper.keepLimitedFlights();
        
        assert(aiFlightsKept.size() == 7);
        assert(listHasFlight(aiFlightsKept, 10001));
        assert(listHasFlight(aiFlightsKept, 10002));
        assert(listHasFlight(aiFlightsKept, 10004));
        assert(listHasFlight(aiFlightsKept, 10006));

        assert(listHasFlight(aiFlightsKept, 20001));
        assert(listHasFlight(aiFlightsKept, 20003));
        assert(listHasFlight(aiFlightsKept, 20004));
    }

    @Test
    public void singlePlayerBomberTest() throws PWCGException
    {
        Campaign coopCampaign = CampaignCache.makeCampaign(SquadronTestProfile.RAF_184_PROFILE);
        coopCampaign.setCampaignConfigManager(configManagerCampaign);

        Mockito.when(missionFlightBuilder.hasPlayerFlightWithFlightTypes(ArgumentMatchers.anyList())).thenReturn(false);
        Mockito.when(alliedPlayerFlight.getFlightType()).thenReturn(FlightTypes.BOMB);

        alliedPlayerFlights.add(alliedPlayerFlight1);
        
        alliedAiFlights.add(alliedPlayerFlight);
        alliedAiFlights.add(alliedAiFlight2);
        alliedAiFlights.add(alliedAiFlight3);
        alliedAiFlights.add(alliedAiFlight4);
        alliedAiFlights.add(alliedAiFlight5);
        alliedAiFlights.add(alliedAiFlight6);
        
        axisAiFlights.add(axisAiFlight1);
        axisAiFlights.add(axisAiFlight2);
        axisAiFlights.add(axisAiFlight3);
        axisAiFlights.add(axisAiFlight4);
        axisAiFlights.add(axisAiFlight5);

        allAiFlights.addAll(axisAiFlights);
        allAiFlights.addAll(alliedAiFlights);
        Mockito.when(missionFlightBuilder.getAiFlights()).thenReturn(allAiFlights);

        MissionFlightKeeper missionFlightKeeper = new MissionFlightKeeper(coopCampaign, mission);
        List<IFlight> aiFlightsKept = missionFlightKeeper.keepLimitedFlights();
        
        assert(aiFlightsKept.size() == 5);
        assert(listHasFlight(aiFlightsKept, 10001));
        assert(listHasFlight(aiFlightsKept, 10002));
        assert(listHasFlight(aiFlightsKept, 10006));

        assert(listHasFlight(aiFlightsKept, 20001));
        assert(listHasFlight(aiFlightsKept, 20004));
    }

    @Test
    public void coopFighterTest() throws PWCGException
    {
        Campaign coopCampaign = CampaignCache.makeCampaign(SquadronTestProfile.COOP_COMPETITIVE_PROFILE);
        coopCampaign.setCampaignConfigManager(configManagerCampaign);

        Mockito.when(missionFlightBuilder.hasPlayerFlightWithFlightTypes(ArgumentMatchers.anyList())).thenReturn(true);

        alliedPlayerFlights.add(alliedPlayerFlight1);
        alliedPlayerFlights.add(alliedPlayerFlight2);
        alliedPlayerFlights.add(alliedPlayerFlight3);

        axisPlayerFlights.add(axisAiFlight1);
        axisPlayerFlights.add(axisAiFlight2);
        
        alliedAiFlights.add(alliedPlayerFlight);
        alliedAiFlights.add(alliedAiFlight2);
        alliedAiFlights.add(alliedAiFlight3);
        alliedAiFlights.add(alliedAiFlight4);
        alliedAiFlights.add(alliedAiFlight5);
        
        axisAiFlights.add(axisAiFlight1);
        axisAiFlights.add(axisAiFlight2);
        axisAiFlights.add(axisAiFlight3);
        axisAiFlights.add(axisAiFlight4);
        axisAiFlights.add(axisAiFlight5);

        allAiFlights.addAll(axisAiFlights);
        allAiFlights.addAll(alliedAiFlights);
        Mockito.when(missionFlightBuilder.getAiFlights()).thenReturn(allAiFlights);

        MissionFlightKeeper missionFlightKeeper = new MissionFlightKeeper(coopCampaign, mission);
        List<IFlight> aiFlightsKept = missionFlightKeeper.keepLimitedFlights();
        
        assert(aiFlightsKept.size() == 3);
        assert(listHasFlight(aiFlightsKept, 10001));
        assert(listHasFlight(aiFlightsKept, 10004));
        assert(listHasFlight(aiFlightsKept, 20001));
    }
    
    @Test
    public void coopBomberTest() throws PWCGException
    {
        Campaign coopCampaign = CampaignCache.makeCampaign(SquadronTestProfile.COOP_COMPETITIVE_PROFILE);
        coopCampaign.setCampaignConfigManager(configManagerCampaign);

        Mockito.when(missionFlightBuilder.hasPlayerFlightWithFlightTypes(ArgumentMatchers.anyList())).thenReturn(false);

        alliedPlayerFlights.add(alliedPlayerFlight1);
        alliedPlayerFlights.add(alliedPlayerFlight2);
        alliedPlayerFlights.add(alliedPlayerFlight3);

        axisPlayerFlights.add(axisAiFlight1);
        axisPlayerFlights.add(axisAiFlight2);
        
        alliedAiFlights.add(alliedPlayerFlight);
        alliedAiFlights.add(alliedAiFlight2);
        alliedAiFlights.add(alliedAiFlight3);
        alliedAiFlights.add(alliedAiFlight4);
        alliedAiFlights.add(alliedAiFlight5);
        
        axisAiFlights.add(axisAiFlight1);
        axisAiFlights.add(axisAiFlight2);
        axisAiFlights.add(axisAiFlight3);
        axisAiFlights.add(axisAiFlight4);
        axisAiFlights.add(axisAiFlight5);

        allAiFlights.addAll(axisAiFlights);
        allAiFlights.addAll(alliedAiFlights);
        Mockito.when(missionFlightBuilder.getAiFlights()).thenReturn(allAiFlights);

        MissionFlightKeeper missionFlightKeeper = new MissionFlightKeeper(coopCampaign, mission);
        List<IFlight> aiFlightsKept = missionFlightKeeper.keepLimitedFlights();
        
        assert(aiFlightsKept.size() == 3);
        assert(listHasFlight(aiFlightsKept, 10001));
        assert(listHasFlight(aiFlightsKept, 10004));
        assert(listHasFlight(aiFlightsKept, 20001));
    }

    private boolean listHasFlight(List<IFlight> flights, int flightId)
    {
        for (IFlight flight : flights)
        {
            if (flight.getFlightId() == flightId)
            {
                return true;
            }
        }
        return false;
    }
}
