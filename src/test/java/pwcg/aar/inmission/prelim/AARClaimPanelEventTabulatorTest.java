package pwcg.aar.inmission.prelim;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.aar.prelim.PwcgMissionData;
import pwcg.aar.prelim.PwcgMissionDataEvaluator;
import pwcg.aar.prelim.claims.AARClaimPanelData;
import pwcg.aar.prelim.claims.AARClaimPanelEventTabulator;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.data.MissionHeader;
import pwcg.mission.data.PwcgGeneratedMissionPlaneData;

@ExtendWith(MockitoExtension.class)
public class AARClaimPanelEventTabulatorTest
{
    @Mock private Campaign campaign;
    @Mock private Squadron squad;
    @Mock private PwcgMissionDataEvaluator pwcgMissionDataEvaluator;
    @Mock private AARPreliminaryData aarPreliminarytData;
    @Mock private PwcgMissionData pwcgMissionData;
    @Mock private MissionHeader missionHeader;

    private Map<Integer, PwcgGeneratedMissionPlaneData> missionPlanes  = new HashMap<>();    
    private Date campaignDate;
    
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaignDate = DateUtils.getDateYYYYMMDD("19420420");
        Mockito.when(campaign.getDate()).thenReturn(campaignDate);
        List<Squadron> playerSquadrons = new ArrayList<>();
        playerSquadrons.add(squad);
        
        Mockito.when(aarPreliminarytData.getPwcgMissionData()).thenReturn(pwcgMissionData);

        PwcgGeneratedMissionPlaneData alliedYak = new PwcgGeneratedMissionPlaneData();
        alliedYak.setAircraftType("yak1s69");
        alliedYak.setSquadronId(10111011);
        alliedYak.setPilotSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1);

        PwcgGeneratedMissionPlaneData alliedYak2 = new PwcgGeneratedMissionPlaneData();
        alliedYak2.setAircraftType("yak1s69");
        alliedYak2.setSquadronId(10111011);
        alliedYak2.setPilotSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 2);

        PwcgGeneratedMissionPlaneData alliedIL2 = new PwcgGeneratedMissionPlaneData();
        alliedIL2.setAircraftType("il2m42");
        alliedIL2.setSquadronId(10121503);
        alliedIL2.setPilotSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 3);

        missionPlanes.put(alliedYak.getPilotSerialNumber(), alliedYak);
        missionPlanes.put(alliedYak2.getPilotSerialNumber(), alliedYak2);
        missionPlanes.put(alliedIL2.getPilotSerialNumber(), alliedIL2);
       
        PwcgGeneratedMissionPlaneData axis109 = new PwcgGeneratedMissionPlaneData();
        axis109.setAircraftType("bf109f4");
        axis109.setSquadronId(20111051);
        axis109.setPilotSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 4);

        missionPlanes.put(axis109.getPilotSerialNumber(), axis109);

        Mockito.when(pwcgMissionData.getMissionPlanes()).thenReturn(missionPlanes);
    }
    
    @Test
    public void germanMission () throws PWCGException
    {             
        AARClaimPanelEventTabulator claimPanelEventTabulator = new AARClaimPanelEventTabulator(campaign, aarPreliminarytData, Side.AXIS);
        AARClaimPanelData claimPanelData = claimPanelEventTabulator.tabulateForAARClaimPanel();
        assert (claimPanelData.getEnemyPlaneTypesInMission().size() == 2);
        
    }
    
    @Test
    public void russianMission () throws PWCGException
    {             
        AARClaimPanelEventTabulator claimPanelEventTabulator = new AARClaimPanelEventTabulator(campaign, aarPreliminarytData, Side.ALLIED);
        AARClaimPanelData claimPanelData = claimPanelEventTabulator.tabulateForAARClaimPanel();
        assert (claimPanelData.getEnemyPlaneTypesInMission().size() == 1);
        
    }
}
