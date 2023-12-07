package pwcg.aar.inmission.prelim;

import java.util.ArrayList;
import java.util.Date;
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

import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.aar.prelim.PwcgMissionData;
import pwcg.aar.prelim.PwcgMissionDataEvaluator;
import pwcg.aar.prelim.claims.AARClaimPanelData;
import pwcg.aar.prelim.claims.AARClaimPanelEventTabulator;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignEquipmentManager;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.data.MissionHeader;
import pwcg.mission.data.PwcgGeneratedMissionPlaneData;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AARClaimPanelEventTabulatorTest
{
    @Mock private Campaign campaign;
    @Mock private Squadron squad;
    @Mock private PwcgMissionDataEvaluator pwcgMissionDataEvaluator;
    @Mock private AARPreliminaryData aarPreliminarytData;
    @Mock private PwcgMissionData pwcgMissionData;
    @Mock private MissionHeader missionHeader;
    @Mock private EquippedPlane alliedYak1Equipped;
    @Mock private EquippedPlane alliedYak2Equipped;
    @Mock private EquippedPlane alliedIL2Equipped;
    @Mock private EquippedPlane axis109Equipped;
    @Mock private CampaignEquipmentManager equipmentManager;

    private Map<Integer, PwcgGeneratedMissionPlaneData> missionPlanes  = new HashMap<>();    
    private Date campaignDate;
    
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaignDate = DateUtils.getDateYYYYMMDD("19420420");
        
        Mockito.when(campaign.getEquipmentManager()).thenReturn(equipmentManager);
        Mockito.when(equipmentManager.getAnyPlaneWithPreference(Integer.valueOf(SerialNumber.PLANE_STARTING_SERIAL_NUMBER + 1))).thenReturn(alliedYak1Equipped);
        Mockito.when(equipmentManager.getAnyPlaneWithPreference(Integer.valueOf(SerialNumber.PLANE_STARTING_SERIAL_NUMBER + 2))).thenReturn(alliedYak2Equipped);
        Mockito.when(equipmentManager.getAnyPlaneWithPreference(Integer.valueOf(SerialNumber.PLANE_STARTING_SERIAL_NUMBER + 3))).thenReturn(alliedIL2Equipped);
        Mockito.when(equipmentManager.getAnyPlaneWithPreference(Integer.valueOf(SerialNumber.PLANE_STARTING_SERIAL_NUMBER + 4))).thenReturn(axis109Equipped);

        Mockito.when(alliedYak1Equipped.getType()).thenReturn("yak1");
        Mockito.when(alliedYak2Equipped.getType()).thenReturn("yak1");
        Mockito.when(alliedIL2Equipped.getType()).thenReturn("il242");
        Mockito.when(axis109Equipped.getType()).thenReturn("bf109f4");
        
        
        Mockito.when(campaign.getDate()).thenReturn(campaignDate);
        List<Squadron> playerSquadrons = new ArrayList<>();
        playerSquadrons.add(squad);
        
        Mockito.when(aarPreliminarytData.getPwcgMissionData()).thenReturn(pwcgMissionData);

        PwcgGeneratedMissionPlaneData alliedYak = new PwcgGeneratedMissionPlaneData();
        alliedYak.setSquadronId(10111011);
        alliedYak.setPilotSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1);
        alliedYak.setPlaneSerialNumber(SerialNumber.PLANE_STARTING_SERIAL_NUMBER + 1);

        PwcgGeneratedMissionPlaneData alliedYak2 = new PwcgGeneratedMissionPlaneData();
        alliedYak2.setSquadronId(10111011);
        alliedYak2.setPilotSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 2);
        alliedYak2.setPlaneSerialNumber(SerialNumber.PLANE_STARTING_SERIAL_NUMBER + 2);

        PwcgGeneratedMissionPlaneData alliedIL2 = new PwcgGeneratedMissionPlaneData();
        alliedIL2.setSquadronId(10121503);
        alliedIL2.setPilotSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 3);
        alliedIL2.setPlaneSerialNumber(SerialNumber.PLANE_STARTING_SERIAL_NUMBER + 3);

        missionPlanes.put(alliedYak.getPilotSerialNumber(), alliedYak);
        missionPlanes.put(alliedYak2.getPilotSerialNumber(), alliedYak2);
        missionPlanes.put(alliedIL2.getPilotSerialNumber(), alliedIL2);
       
        PwcgGeneratedMissionPlaneData axis109 = new PwcgGeneratedMissionPlaneData();
        axis109.setSquadronId(20111051);
        axis109.setPilotSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 4);
        axis109.setPlaneSerialNumber(SerialNumber.PLANE_STARTING_SERIAL_NUMBER + 4);

        missionPlanes.put(axis109.getPilotSerialNumber(), axis109);

        Mockito.when(pwcgMissionData.getMissionPlanes()).thenReturn(missionPlanes);
    }
    
    @Test
    public void germanMission () throws PWCGException
    {             
        AARClaimPanelEventTabulator claimPanelEventTabulator = new AARClaimPanelEventTabulator(campaign, aarPreliminarytData, Side.AXIS);
        AARClaimPanelData claimPanelData = claimPanelEventTabulator.tabulateForAARClaimPanel();
        Assertions.assertTrue (claimPanelData.getEnemyPlaneTypesInMission().size() == 2);
        
    }
    
    @Test
    public void russianMission () throws PWCGException
    {             
        AARClaimPanelEventTabulator claimPanelEventTabulator = new AARClaimPanelEventTabulator(campaign, aarPreliminarytData, Side.ALLIED);
        AARClaimPanelData claimPanelData = claimPanelEventTabulator.tabulateForAARClaimPanel();
        Assertions.assertTrue (claimPanelData.getEnemyPlaneTypesInMission().size() == 1);
        
    }
}
