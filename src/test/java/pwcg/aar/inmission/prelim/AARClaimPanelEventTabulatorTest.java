package pwcg.aar.inmission.prelim;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.aar.prelim.PwcgMissionData;
import pwcg.aar.prelim.PwcgMissionDataEvaluator;
import pwcg.aar.prelim.claims.AARClaimPanelData;
import pwcg.aar.prelim.claims.AARClaimPanelEventTabulator;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.data.MissionHeader;
import pwcg.mission.data.PwcgGeneratedMissionPlaneData;

@RunWith(MockitoJUnitRunner.class)
public class AARClaimPanelEventTabulatorTest
{
    @Mock
    private Campaign campaign;
    
    @Mock
    private Squadron squad;

    @Mock
    private PwcgMissionDataEvaluator pwcgMissionDataEvaluator;

    @Mock
    private AARPreliminaryData aarPreliminarytData;

    @Mock
    private PwcgMissionData pwcgMissionData;

    @Mock
    private MissionHeader missionHeader;
    
    @Mock
    private ICountry country;
    
    private Map<Integer, PwcgGeneratedMissionPlaneData> missionPlanes  = new HashMap<>();
    
    private Date campaignDate;

    
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        campaignDate = DateUtils.getDateYYYYMMDD("19420420");
        Mockito.when(campaign.getDate()).thenReturn(campaignDate);
        Mockito.when(campaign.determinePlayerSquadrons()).thenReturn(squad);
        Mockito.when(pwcgMissionData.getMissionHeader()).thenReturn(missionHeader);
        
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
        Mockito.when(squad.determineSquadronCountry(campaign.getDate())).thenReturn(country);
        Mockito.when(country.getSide()).thenReturn(Side.AXIS);

        Mockito.when(missionHeader.getMapName()).thenReturn(PWCGMap.STALINGRAD_MAP_NAME);

        AARClaimPanelEventTabulator claimPanelEventTabulator = new AARClaimPanelEventTabulator(campaign, aarPreliminarytData);
        AARClaimPanelData claimPanelData = claimPanelEventTabulator.tabulateForAARClaimPanel();
        assert (claimPanelData.getEnemyPlaneTypesInMission().size() == 2);
        assert (claimPanelData.getMapId() == FrontMapIdentifier.STALINGRAD_MAP);
        
    }
    
    @Test
    public void russianMission () throws PWCGException
    {             
        Mockito.when(squad.determineSquadronCountry(campaign.getDate())).thenReturn(country);
        Mockito.when(country.getSide()).thenReturn(Side.ALLIED);

        Mockito.when(pwcgMissionData.getMissionHeader()).thenReturn(missionHeader);
        Mockito.when(missionHeader.getMapName()).thenReturn(PWCGMap.STALINGRAD_MAP_NAME);

        AARClaimPanelEventTabulator claimPanelEventTabulator = new AARClaimPanelEventTabulator(campaign, aarPreliminarytData);
        AARClaimPanelData claimPanelData = claimPanelEventTabulator.tabulateForAARClaimPanel();
        assert (claimPanelData.getEnemyPlaneTypesInMission().size() == 1);
        assert (claimPanelData.getMapId() == FrontMapIdentifier.STALINGRAD_MAP);
        
    }
}
