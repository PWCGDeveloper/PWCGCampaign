package pwcg.campaign.squadmember;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogGroundUnit;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class VictoryBuilderTest
{
    @Mock
    private Campaign campaign;
    
    @Mock
    private CampaignPersonnelManager personnelManager;
    
    @Mock
    private SquadronMember victor;
    
    @Mock
    private SquadronMember victim;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);

        Mockito.when(personnelManager.getAnyCampaignMember(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1)).thenReturn(victor);
        Mockito.when(personnelManager.getAnyCampaignMember(SerialNumber.AI_STARTING_SERIAL_NUMBER + 2)).thenReturn(victim);
        
        Mockito.when(victor.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1);
        Mockito.when(victim.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER + 2);
        
        Mockito.when(victor.getNameAndRank()).thenReturn("Ofw Hans Schmidt");
        Mockito.when(victim.getNameAndRank()).thenReturn("Szt Ivan Ivanov");
    }
    
    @Test
    public void buildVictoryPlanePlane () throws PWCGException
    {
        LogPlane logVictor = new LogPlane(1);
        logVictor.setCrashedInSight(true);
        logVictor.setName(victor.getNameAndRank());
        logVictor.setPilotSerialNumber(victor.getSerialNumber());
        logVictor.setVehicleType("Bf109 F-2");
        logVictor.setSquadronId(20111052);
        logVictor.intializePilot(victor.getSerialNumber());
        logVictor.getLogPilot().setStatus(SquadronMemberStatus.STATUS_ACTIVE);

        LogPlane logVictim = new LogPlane(2);
        logVictim.setCrashedInSight(true);
        logVictim.setName(victim.getNameAndRank());
        logVictim.setPilotSerialNumber(victim.getSerialNumber());
        logVictim.setVehicleType("Il-2 mod.1941");
        logVictim.setSquadronId(10121312);
        logVictim.intializePilot(victim.getSerialNumber());
        logVictim.getLogPilot().setStatus(SquadronMemberStatus.STATUS_CAPTURED);

        LogVictory logVictory = new LogVictory(10);
        logVictory.setLocation(new Coordinate (100000, 0, 100000));
        logVictory.setVictim(logVictim);
        logVictory.setVictor(logVictor);

        VictoryBuilder victoryBuilder = new VictoryBuilder(campaign);
        Victory victory = victoryBuilder.buildVictory(DateUtils.getDateYYYYMMDD("19421103"), logVictory);
        
        assert (victory.getVictor().getPilotName().equals(victor.getNameAndRank()));
        assert (victory.getVictim().getPilotName().equals(victim.getNameAndRank()));
        
        VictoryDescription victoryDescription = new VictoryDescription(campaign, victory);
        String victoryDescriptionText = victoryDescription.createVictoryDescription();
        
        String verificationSegment =  "A Il-2 mod.1941 of 621st Ground Attack Air Regiment was brought down by Ofw Hans Schmidt";

        assert(victoryDescriptionText.contains(verificationSegment));
    }
    
    @Test
    public void buildVictoryPlaneGround () throws PWCGException
    {
        LogPlane logVictor = new LogPlane(1);
        logVictor.setCrashedInSight(true);
        logVictor.setName(victor.getNameAndRank());
        logVictor.setPilotSerialNumber(victor.getSerialNumber());
        logVictor.setVehicleType("Bf109 F-2");
        logVictor.setSquadronId(20111052);
        logVictor.intializePilot(victor.getSerialNumber());
        logVictor.getLogPilot().setStatus(SquadronMemberStatus.STATUS_ACTIVE);

        LogGroundUnit logVictim = new LogGroundUnit(1000);
        logVictim.setName(victim.getNameAndRank());
        logVictim.setVehicleType("Truck");

        LogVictory logVictory = new LogVictory(10);
        logVictory.setLocation(new Coordinate (100000, 0, 100000));
        logVictory.setVictim(logVictim);
        logVictory.setVictor(logVictor);

        VictoryBuilder victoryBuilder = new VictoryBuilder(campaign);
        Victory victory = victoryBuilder.buildVictory(DateUtils.getDateYYYYMMDD("19421103"), logVictory);
        
        assert (victory.getVictor().getPilotName().equals(victor.getNameAndRank()));
        assert (victory.getVictim().getType().equals("Truck"));
        
        VictoryDescription victoryDescription = new VictoryDescription(campaign, victory);
        String victoryDescriptionText = victoryDescription.createVictoryDescription();
        
        String verificationSegment=  "A Truck was destroyed by Ofw Hans Schmidt of I./JG52";

        assert(victoryDescriptionText.contains(verificationSegment));
    }
}
