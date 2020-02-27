package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.FlightTypes;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class MissionFlightKeeperTest
{
    @Mock private  Mission mission;
    @Mock private  MissionFlightBuilder missionFlightBuilder;
    @Mock private ConfigManagerCampaign configManagerCampaign;
    @Mock private  IFlight alliedAiFlight1;
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
    @Mock private  Squadron axisSquadron;
    @Mock private  Squadron alliedSquadron;


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
        
        Mockito.when(axisSquadron.determineSide()).thenReturn(Side.AXIS);
        Mockito.when(alliedSquadron.determineSide()).thenReturn(Side.ALLIED);

        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(mission.getMissionFlightBuilder()).thenReturn(missionFlightBuilder);
        Mockito.when(missionFlightBuilder.getAiFlightsForSide(Side.ALLIED)).thenReturn(alliedAiFlights);
        Mockito.when(missionFlightBuilder.getAiFlightsForSide(Side.AXIS)).thenReturn(axisAiFlights);
        Mockito.when(missionFlightBuilder.getPlayerFlightsForSide(Side.ALLIED)).thenReturn(alliedPlayerFlights);
        Mockito.when(missionFlightBuilder.getPlayerFlightsForSide(Side.AXIS)).thenReturn(axisPlayerFlights);
        
        Mockito.when(configManagerCampaign.getIntConfigParam(ConfigItemKeys.AlliedFlightsToKeepKey)).thenReturn(5);
        Mockito.when(configManagerCampaign.getIntConfigParam(ConfigItemKeys.AxisFlightsToKeepKey)).thenReturn(3);
        Mockito.when(configManagerCampaign.getIntConfigParam(ConfigItemKeys.AiFighterFlightsForGroundCampaignMaxKey)).thenReturn(1);
        Mockito.when(configManagerCampaign.getIntConfigParam(ConfigItemKeys.AiFighterFlightsForFighterCampaignMaxKey)).thenReturn(3);
        
        Mockito.when(alliedAiFlight1.getFlightId()).thenReturn(10001);
        Mockito.when(alliedAiFlight1.isPlayerFlight()).thenReturn(false);
        Mockito.when(alliedAiFlight1.isFlightHasFighterPlanes()).thenReturn(true);
        Mockito.when(alliedAiFlight1.getFlightType()).thenReturn(FlightTypes.PATROL);
        Mockito.when(alliedAiFlight1.getSquadron()).thenReturn(alliedSquadron);
        Mockito.when(alliedAiFlight1.getClosestContactWithPlayerDistance()).thenReturn(1000.0);

        Mockito.when(alliedAiFlight2.getFlightId()).thenReturn(10002);
        Mockito.when(alliedAiFlight2.isPlayerFlight()).thenReturn(false);
        Mockito.when(alliedAiFlight2.isFlightHasFighterPlanes()).thenReturn(true);
        Mockito.when(alliedAiFlight2.getFlightType()).thenReturn(FlightTypes.PATROL);
        Mockito.when(alliedAiFlight2.getSquadron()).thenReturn(alliedSquadron);
        Mockito.when(alliedAiFlight2.getClosestContactWithPlayerDistance()).thenReturn(3000.0);

        Mockito.when(alliedAiFlight3.getFlightId()).thenReturn(10003);
        Mockito.when(alliedAiFlight3.isPlayerFlight()).thenReturn(false);
        Mockito.when(alliedAiFlight3.isFlightHasFighterPlanes()).thenReturn(true);
        Mockito.when(alliedAiFlight3.getFlightType()).thenReturn(FlightTypes.PATROL);
        Mockito.when(alliedAiFlight3.getSquadron()).thenReturn(alliedSquadron);
        Mockito.when(alliedAiFlight3.getClosestContactWithPlayerDistance()).thenReturn(5000.0);

        Mockito.when(alliedAiFlight4.getFlightId()).thenReturn(10004);
        Mockito.when(alliedAiFlight4.isFlightHasFighterPlanes()).thenReturn(false);
        Mockito.when(alliedAiFlight4.isPlayerFlight()).thenReturn(false);
        Mockito.when(alliedAiFlight4.getFlightType()).thenReturn(FlightTypes.BOMB);
        Mockito.when(alliedAiFlight4.getSquadron()).thenReturn(alliedSquadron);
        Mockito.when(alliedAiFlight4.getClosestContactWithPlayerDistance()).thenReturn(2000.0);

        Mockito.when(alliedAiFlight5.getFlightId()).thenReturn(10005);
        Mockito.when(alliedAiFlight5.isFlightHasFighterPlanes()).thenReturn(false);
        Mockito.when(alliedAiFlight5.isPlayerFlight()).thenReturn(false);
        Mockito.when(alliedAiFlight5.getFlightType()).thenReturn(FlightTypes.BOMB);
        Mockito.when(alliedAiFlight5.getSquadron()).thenReturn(alliedSquadron);
        Mockito.when(alliedAiFlight5.getClosestContactWithPlayerDistance()).thenReturn(4000.0);

        Mockito.when(alliedAiFlight6.getFlightId()).thenReturn(10006);
        Mockito.when(alliedAiFlight6.isFlightHasFighterPlanes()).thenReturn(true);
        Mockito.when(alliedAiFlight6.isPlayerFlight()).thenReturn(false);
        Mockito.when(alliedAiFlight6.getFlightType()).thenReturn(FlightTypes.BOMB);
        Mockito.when(alliedAiFlight6.getSquadron()).thenReturn(alliedSquadron);
        Mockito.when(alliedAiFlight6.getClosestContactWithPlayerDistance()).thenReturn(6000.0);
        
        Mockito.when(axisAiFlight1.getFlightId()).thenReturn(20001);
        Mockito.when(axisAiFlight1.isFlightHasFighterPlanes()).thenReturn(true);
        Mockito.when(axisAiFlight1.isPlayerFlight()).thenReturn(false);
        Mockito.when(axisAiFlight1.getFlightType()).thenReturn(FlightTypes.PATROL);
        Mockito.when(axisAiFlight1.getSquadron()).thenReturn(axisSquadron);
        Mockito.when(axisAiFlight1.getClosestContactWithPlayerDistance()).thenReturn(1000.0);

        Mockito.when(axisAiFlight2.getFlightId()).thenReturn(20002);
        Mockito.when(axisAiFlight2.isFlightHasFighterPlanes()).thenReturn(true);
        Mockito.when(axisAiFlight3.isPlayerFlight()).thenReturn(false);
        Mockito.when(axisAiFlight2.getFlightType()).thenReturn(FlightTypes.PATROL);
        Mockito.when(axisAiFlight2.getSquadron()).thenReturn(axisSquadron);
        Mockito.when(axisAiFlight2.getClosestContactWithPlayerDistance()).thenReturn(3000.0);

        Mockito.when(axisAiFlight3.getFlightId()).thenReturn(20003);
        Mockito.when(axisAiFlight3.isFlightHasFighterPlanes()).thenReturn(true);
        Mockito.when(axisAiFlight3.isPlayerFlight()).thenReturn(false);
        Mockito.when(axisAiFlight3.getFlightType()).thenReturn(FlightTypes.PATROL);
        Mockito.when(axisAiFlight3.getSquadron()).thenReturn(axisSquadron);
        Mockito.when(axisAiFlight3.getClosestContactWithPlayerDistance()).thenReturn(5000.0);

        Mockito.when(axisAiFlight4.getFlightId()).thenReturn(20004);
        Mockito.when(axisAiFlight4.isFlightHasFighterPlanes()).thenReturn(false);
        Mockito.when(axisAiFlight4.isPlayerFlight()).thenReturn(false);
        Mockito.when(axisAiFlight4.getFlightType()).thenReturn(FlightTypes.BOMB);
        Mockito.when(axisAiFlight4.getSquadron()).thenReturn(axisSquadron);
        Mockito.when(axisAiFlight4.getClosestContactWithPlayerDistance()).thenReturn(2000.0);

        Mockito.when(axisAiFlight5.getFlightId()).thenReturn(20005);
        Mockito.when(axisAiFlight5.isFlightHasFighterPlanes()).thenReturn(false);
        Mockito.when(axisAiFlight5.isPlayerFlight()).thenReturn(false);
        Mockito.when(axisAiFlight5.getFlightType()).thenReturn(FlightTypes.BOMB);
        Mockito.when(axisAiFlight5.getSquadron()).thenReturn(axisSquadron);
        Mockito.when(axisAiFlight5.getClosestContactWithPlayerDistance()).thenReturn(4000.0);

        Mockito.when(axisAiFlight6.getFlightId()).thenReturn(20006);
        Mockito.when(axisAiFlight6.isFlightHasFighterPlanes()).thenReturn(true);
        Mockito.when(axisAiFlight6.isPlayerFlight()).thenReturn(false);
        Mockito.when(axisAiFlight6.getFlightType()).thenReturn(FlightTypes.BOMB);
        Mockito.when(axisAiFlight6.getSquadron()).thenReturn(axisSquadron);
        Mockito.when(axisAiFlight6.getClosestContactWithPlayerDistance()).thenReturn(6000.0);

        Mockito.when(alliedPlayerFlight1.getFlightId()).thenReturn(10091);
        Mockito.when(alliedPlayerFlight1.isFlightHasFighterPlanes()).thenReturn(true);
        Mockito.when(alliedPlayerFlight1.isPlayerFlight()).thenReturn(true);
        Mockito.when(alliedPlayerFlight1.getFlightType()).thenReturn(FlightTypes.PATROL);
        Mockito.when(alliedPlayerFlight1.getSquadron()).thenReturn(alliedSquadron);
        
        Mockito.when(alliedPlayerFlight2.getFlightId()).thenReturn(10092);
        Mockito.when(alliedPlayerFlight2.isFlightHasFighterPlanes()).thenReturn(true);
        Mockito.when(alliedPlayerFlight2.isPlayerFlight()).thenReturn(true);
        Mockito.when(alliedPlayerFlight2.getFlightType()).thenReturn(FlightTypes.PATROL);
        Mockito.when(alliedPlayerFlight2.getSquadron()).thenReturn(alliedSquadron);
        
        Mockito.when(alliedPlayerFlight3.getFlightId()).thenReturn(10093);
        Mockito.when(alliedPlayerFlight3.isFlightHasFighterPlanes()).thenReturn(true);
        Mockito.when(alliedPlayerFlight3.isPlayerFlight()).thenReturn(true);
        Mockito.when(alliedPlayerFlight3.getFlightType()).thenReturn(FlightTypes.BOMB);
        Mockito.when(alliedPlayerFlight3.getSquadron()).thenReturn(alliedSquadron);

        Mockito.when(axisPlayerFlight1.getFlightId()).thenReturn(20091);
        Mockito.when(axisPlayerFlight1.isFlightHasFighterPlanes()).thenReturn(true);
        Mockito.when(axisPlayerFlight1.isPlayerFlight()).thenReturn(true);
        Mockito.when(axisPlayerFlight1.getFlightType()).thenReturn(FlightTypes.PATROL);
        Mockito.when(axisPlayerFlight1.getSquadron()).thenReturn(axisSquadron);
        
        Mockito.when(axisPlayerFlight2.getFlightId()).thenReturn(20092);
        Mockito.when(axisPlayerFlight2.isFlightHasFighterPlanes()).thenReturn(true);
        Mockito.when(axisPlayerFlight2.isPlayerFlight()).thenReturn(true);
        Mockito.when(axisPlayerFlight2.getFlightType()).thenReturn(FlightTypes.PATROL);
        Mockito.when(axisPlayerFlight2.getSquadron()).thenReturn(axisSquadron);
        
        Mockito.when(axisPlayerFlight3.getFlightId()).thenReturn(20093);
        Mockito.when(axisPlayerFlight3.isFlightHasFighterPlanes()).thenReturn(true);
        Mockito.when(axisPlayerFlight3.isPlayerFlight()).thenReturn(true);
        Mockito.when(axisPlayerFlight3.getFlightType()).thenReturn(FlightTypes.BOMB);
        Mockito.when(axisPlayerFlight3.getSquadron()).thenReturn(axisSquadron);
                
        Mockito.when(missionFlightBuilder.getAiFlights()).thenReturn(alliedAiFlights);
    }
    
    @Test
    public void singlePlayerFighterTest() throws PWCGException
    {
        Campaign coopCampaign = CampaignCache.makeCampaign(SquadronTestProfile.RAF_184_PROFILE);
        coopCampaign.setCampaignConfigManager(configManagerCampaign);

        Mockito.when(missionFlightBuilder.hasPlayerFlightWithFlightTypes(Matchers.anyListOf(FlightTypes.class))).thenReturn(true);

        alliedPlayerFlights.add(alliedPlayerFlight1);
        
        alliedAiFlights.add(alliedAiFlight1);
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
        
        assert(aiFlightsKept.size() >= 6);
        assert(listHasFlight(aiFlightsKept, 10001));
        assert(listHasFlight(aiFlightsKept, 10004));
        assert(listHasFlight(aiFlightsKept, 10005));

        assert(listHasFlight(aiFlightsKept, 20001));
        assert(listHasFlight(aiFlightsKept, 20004));
    }
    
    @Test
    public void singlePlayerBomberTest() throws PWCGException
    {
        Campaign coopCampaign = CampaignCache.makeCampaign(SquadronTestProfile.RAF_184_PROFILE);
        coopCampaign.setCampaignConfigManager(configManagerCampaign);

        Mockito.when(missionFlightBuilder.hasPlayerFlightWithFlightTypes(Matchers.anyListOf(FlightTypes.class))).thenReturn(false);

        alliedPlayerFlights.add(alliedPlayerFlight1);
        
        alliedAiFlights.add(alliedAiFlight1);
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
        
        assert(aiFlightsKept.size() == 7);
        assert(listHasFlight(aiFlightsKept, 10001));
        assert(listHasFlight(aiFlightsKept, 10004));
        assert(listHasFlight(aiFlightsKept, 10005));

        assert(listHasFlight(aiFlightsKept, 20001));
        assert(listHasFlight(aiFlightsKept, 20004));
    }

    @Test
    public void coopFighterTest() throws PWCGException
    {
        Campaign coopCampaign = CampaignCache.makeCampaign(SquadronTestProfile.COOP_COMPETITIVE_PROFILE);
        coopCampaign.setCampaignConfigManager(configManagerCampaign);

        Mockito.when(missionFlightBuilder.hasPlayerFlightWithFlightTypes(Matchers.anyListOf(FlightTypes.class))).thenReturn(true);

        alliedPlayerFlights.add(alliedPlayerFlight1);
        alliedPlayerFlights.add(alliedPlayerFlight2);
        alliedPlayerFlights.add(alliedPlayerFlight3);

        axisPlayerFlights.add(axisAiFlight1);
        axisPlayerFlights.add(axisAiFlight2);
        
        alliedAiFlights.add(alliedAiFlight1);
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

        Mockito.when(missionFlightBuilder.hasPlayerFlightWithFlightTypes(Matchers.anyListOf(FlightTypes.class))).thenReturn(false);

        alliedPlayerFlights.add(alliedPlayerFlight1);
        alliedPlayerFlights.add(alliedPlayerFlight2);
        alliedPlayerFlights.add(alliedPlayerFlight3);

        axisPlayerFlights.add(axisAiFlight1);
        axisPlayerFlights.add(axisAiFlight2);
        
        alliedAiFlights.add(alliedAiFlight1);
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
