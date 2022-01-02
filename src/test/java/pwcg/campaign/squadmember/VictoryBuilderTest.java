package pwcg.campaign.squadmember;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogGroundUnit;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTurret;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.crewmember.Victory;
import pwcg.campaign.crewmember.VictoryBuilder;
import pwcg.campaign.crewmember.VictoryDescription;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.logfiles.event.AType12;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class VictoryBuilderTest
{
    @Mock
    private Campaign campaign;
    
    @Mock
    private ConfigManagerCampaign configManager;
    
    @Mock
    private CampaignPersonnelManager personnelManager;
    
    @Mock
    private CrewMember victor;
    
    @Mock
    private CrewMember victim;

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);

        Mockito.when(configManager.getIntConfigParam(ConfigItemKeys.DetailedVictoryDescriptionKey)).thenReturn(1);
        
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
        Mockito.when(configManager.getIntConfigParam(ConfigItemKeys.DetailedVictoryDescriptionKey)).thenReturn(0);

        LogPlane logVictor = new LogPlane(1);
        logVictor.setCrashedInSight(true);
        logVictor.setName(victor.getNameAndRank());
        logVictor.setCrewMemberSerialNumber(victor.getSerialNumber());
        logVictor.setVehicleType("Bf109 F-2");
        logVictor.setSquadronId(20111052);
        logVictor.intializeCrewMember(victor.getSerialNumber());
        logVictor.getLogCrewMember().setStatus(CrewMemberStatus.STATUS_ACTIVE);

        LogPlane logVictim = new LogPlane(2);
        logVictim.setCrashedInSight(true);
        logVictim.setName(victim.getNameAndRank());
        logVictim.setCrewMemberSerialNumber(victim.getSerialNumber());
        logVictim.setVehicleType("Il-2 mod.1941");
        logVictim.setSquadronId(10121312);
        logVictim.intializeCrewMember(victim.getSerialNumber());
        logVictim.getLogCrewMember().setStatus(CrewMemberStatus.STATUS_CAPTURED);

        LogVictory logVictory = new LogVictory(10);
        logVictory.setLocation(new Coordinate (100000, 0, 100000));
        logVictory.setVictim(logVictim);
        logVictory.setVictor(logVictor);

        VictoryBuilder victoryBuilder = new VictoryBuilder(campaign);
        Victory victory = victoryBuilder.buildVictory(DateUtils.getDateYYYYMMDD("19421103"), logVictory);
        
        Assertions.assertTrue (victory.getVictor().getCrewMemberName().equals(victor.getNameAndRank()));
        Assertions.assertTrue (victory.getVictim().getCrewMemberName().equals(victim.getNameAndRank()));
        
        VictoryDescription victoryDescription = new VictoryDescription(campaign, victory);
        String victoryDescriptionText = victoryDescription.createVictoryDescription();
        
        String verificationSegment =  "A Il-2 mod.1941 of 621st Ground Attack Air Regiment was brought down by Ofw Hans Schmidt";

        assert(!victoryDescriptionText.contains(verificationSegment));
    }
    
    @Test
    public void buildVictoryFogOfWar () throws PWCGException
    {
        LogPlane logVictor = new LogPlane(1);
        logVictor.setCrashedInSight(true);
        logVictor.setName(victor.getNameAndRank());
        logVictor.setCrewMemberSerialNumber(victor.getSerialNumber());
        logVictor.setVehicleType("Bf109 F-2");
        logVictor.setSquadronId(20111052);
        logVictor.intializeCrewMember(victor.getSerialNumber());
        logVictor.getLogCrewMember().setStatus(CrewMemberStatus.STATUS_ACTIVE);

        LogPlane logVictim = new LogPlane(2);
        logVictim.setCrashedInSight(true);
        logVictim.setName(victim.getNameAndRank());
        logVictim.setCrewMemberSerialNumber(victim.getSerialNumber());
        logVictim.setVehicleType("Il-2 mod.1941");
        logVictim.setSquadronId(10121312);
        logVictim.intializeCrewMember(victim.getSerialNumber());
        logVictim.getLogCrewMember().setStatus(CrewMemberStatus.STATUS_CAPTURED);

        LogVictory logVictory = new LogVictory(10);
        logVictory.setLocation(new Coordinate (100000, 0, 100000));
        logVictory.setVictim(logVictim);
        logVictory.setVictor(logVictor);

        VictoryBuilder victoryBuilder = new VictoryBuilder(campaign);
        Victory victory = victoryBuilder.buildVictory(DateUtils.getDateYYYYMMDD("19421103"), logVictory);
        
        Assertions.assertTrue (victory.getVictor().getCrewMemberName().equals(victor.getNameAndRank()));
        Assertions.assertTrue (victory.getVictim().getCrewMemberName().equals(victim.getNameAndRank()));
        
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
        logVictor.setCrewMemberSerialNumber(victor.getSerialNumber());
        logVictor.setVehicleType("Bf109 F-2");
        logVictor.setSquadronId(20111052);
        logVictor.intializeCrewMember(victor.getSerialNumber());
        logVictor.getLogCrewMember().setStatus(CrewMemberStatus.STATUS_ACTIVE);

        LogGroundUnit logVictim = new LogGroundUnit(1000);
        logVictim.setName(victim.getNameAndRank());
        logVictim.setVehicleType("gaz-aa");

        LogVictory logVictory = new LogVictory(10);
        logVictory.setLocation(new Coordinate (100000, 0, 100000));
        logVictory.setVictim(logVictim);
        logVictory.setVictor(logVictor);

        VictoryBuilder victoryBuilder = new VictoryBuilder(campaign);
        Victory victory = victoryBuilder.buildVictory(DateUtils.getDateYYYYMMDD("19421103"), logVictory);
        
        Assertions.assertTrue (victory.getVictor().getCrewMemberName().equals(victor.getNameAndRank()));
        Assertions.assertTrue (victory.getVictim().getType().equals("gaz-aa"));
        
        VictoryDescription victoryDescription = new VictoryDescription(campaign, victory);
        String victoryDescriptionText = victoryDescription.createVictoryDescription();
        
        String verificationSegment=  "A truck was destroyed by Ofw Hans Schmidt of I./JG52";

        assert(victoryDescriptionText.contains(verificationSegment));
    }

    @Test
    public void buildVictoryGunnerPlane () throws PWCGException
    {
        Mockito.when(configManager.getIntConfigParam(ConfigItemKeys.DetailedVictoryDescriptionKey)).thenReturn(0);

        LogPlane logVictor = new LogPlane(1);
        logVictor.setCrashedInSight(true);
        logVictor.setName(victor.getNameAndRank());
        logVictor.setCrewMemberSerialNumber(victor.getSerialNumber());
        logVictor.setVehicleType("Ju 88 A-4");
        logVictor.setSquadronId(20111052);
        logVictor.intializeCrewMember(victor.getSerialNumber());
        logVictor.getLogCrewMember().setStatus(CrewMemberStatus.STATUS_ACTIVE);

        AType12 atype12 = new AType12("200", "Turret_Ju88A4_1", "Turret_Ju88A4_1", logVictor.getCountry(), logVictor.getId(), new Coordinate(500000, 0, 50000));
        LogTurret logVictorTurret = logVictor.createTurret(atype12);

        LogPlane logVictim = new LogPlane(3);
        logVictim.setCrashedInSight(true);
        logVictim.setName(victim.getNameAndRank());
        logVictim.setCrewMemberSerialNumber(victim.getSerialNumber());
        logVictim.setVehicleType("Il-2 mod.1941");
        logVictim.setSquadronId(10121312);
        logVictim.intializeCrewMember(victim.getSerialNumber());
        logVictim.getLogCrewMember().setStatus(CrewMemberStatus.STATUS_CAPTURED);

        LogVictory logVictory = new LogVictory(10);
        logVictory.setLocation(new Coordinate (100000, 0, 100000));
        logVictory.setVictim(logVictim);
        logVictory.setVictor(logVictorTurret);

        VictoryBuilder victoryBuilder = new VictoryBuilder(campaign);
        Victory victory = victoryBuilder.buildVictory(DateUtils.getDateYYYYMMDD("19421103"), logVictory);

        Assertions.assertTrue (victory.getVictor().getCrewMemberName().equals(victor.getNameAndRank()));
        Assertions.assertTrue (victory.getVictor().isGunner());
        Assertions.assertTrue (victory.getVictim().getCrewMemberName().equals(victim.getNameAndRank()));

        VictoryDescription victoryDescription = new VictoryDescription(campaign, victory);
        String victoryDescriptionText = victoryDescription.createVictoryDescription();

        String verificationSegment =  "A Il-2 mod.1941 of 621st Ground Attack Air Regiment was brought down by a gunner flying with Ofw Hans Schmidt";

        assert(!victoryDescriptionText.contains(verificationSegment));
    }
}
