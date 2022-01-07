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

import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.aar.prelim.PwcgMissionData;
import pwcg.aar.prelim.PwcgMissionDataEvaluator;
import pwcg.aar.prelim.claims.AARClaimPanelData;
import pwcg.aar.prelim.claims.AARClaimPanelEventTabulator;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.data.MissionHeader;
import pwcg.mission.data.PwcgGeneratedMissionVehicleData;

@ExtendWith(MockitoExtension.class)
public class AARClaimPanelEventTabulatorTest
{
    @Mock private Campaign campaign;
    @Mock private Company company;
    @Mock private PwcgMissionDataEvaluator pwcgMissionDataEvaluator;
    @Mock private AARPreliminaryData aarPreliminarytData;
    @Mock private PwcgMissionData pwcgMissionData;
    @Mock private MissionHeader missionHeader;

    private Map<Integer, PwcgGeneratedMissionVehicleData> missionPlanes  = new HashMap<>();    
    private Date campaignDate;
    
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaignDate = DateUtils.getDateYYYYMMDD("19420420");
        Mockito.when(campaign.getDate()).thenReturn(campaignDate);
        List<Company> playerSquadrons = new ArrayList<>();
        playerSquadrons.add(company);
        
        Mockito.when(aarPreliminarytData.getPwcgMissionData()).thenReturn(pwcgMissionData);

        PwcgGeneratedMissionVehicleData alliedYak = new PwcgGeneratedMissionVehicleData();
        alliedYak.setVehicleType("yak1s69");
        alliedYak.setCompanyId(10111011);
        alliedYak.setCrewMemberSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1);

        PwcgGeneratedMissionVehicleData alliedYak2 = new PwcgGeneratedMissionVehicleData();
        alliedYak2.setVehicleType("yak1s69");
        alliedYak2.setCompanyId(10111011);
        alliedYak2.setCrewMemberSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 2);

        PwcgGeneratedMissionVehicleData alliedIL2 = new PwcgGeneratedMissionVehicleData();
        alliedIL2.setVehicleType("il2m42");
        alliedIL2.setCompanyId(10121503);
        alliedIL2.setCrewMemberSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 3);

        missionPlanes.put(alliedYak.getCrewMemberSerialNumber(), alliedYak);
        missionPlanes.put(alliedYak2.getCrewMemberSerialNumber(), alliedYak2);
        missionPlanes.put(alliedIL2.getCrewMemberSerialNumber(), alliedIL2);
       
        PwcgGeneratedMissionVehicleData axis109 = new PwcgGeneratedMissionVehicleData();
        axis109.setVehicleType("bf109f4");
        axis109.setCompanyId(20111051);
        axis109.setCrewMemberSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 4);

        missionPlanes.put(axis109.getCrewMemberSerialNumber(), axis109);

        Mockito.when(pwcgMissionData.getMissionPlanes()).thenReturn(missionPlanes);
    }
    
    @Test
    public void germanMission () throws PWCGException
    {             
        AARClaimPanelEventTabulator claimPanelEventTabulator = new AARClaimPanelEventTabulator(campaign, aarPreliminarytData, Side.AXIS);
        AARClaimPanelData claimPanelData = claimPanelEventTabulator.tabulateForAARClaimPanel();
        Assertions.assertTrue (claimPanelData.getEnemyTankTypesInMission().size() == 2);
        
    }
    
    @Test
    public void russianMission () throws PWCGException
    {             
        AARClaimPanelEventTabulator claimPanelEventTabulator = new AARClaimPanelEventTabulator(campaign, aarPreliminarytData, Side.ALLIED);
        AARClaimPanelData claimPanelData = claimPanelEventTabulator.tabulateForAARClaimPanel();
        Assertions.assertTrue (claimPanelData.getEnemyTankTypesInMission().size() == 1);
        
    }
}
