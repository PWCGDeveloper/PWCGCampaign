package pwcg.aar.inmission.phase2.logeval;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPilot;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class AARCrewBuilderTest
{
    @Mock
    Campaign campaign;
    
    @Mock
    private CampaignPersonnelManager personnelManager;

    @Mock
    private Ace aceInMission1;
    
    @Mock
    private Ace aceInMission2;

    @Mock
    private SquadronMember player;

    @Mock
    private SquadronMember aiInSquadron;

    @Mock
    private SquadronMember aiNotInSquadron;

    private Map <String, LogPlane> planeAiEntities = new HashMap <>();
    
    @Before
    public void setup () throws PWCGException 
    {        
        PWCGContext.setProduct(PWCGProduct.FC);

        planeAiEntities = new HashMap <>();
        addPlane(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        addPlane(SerialNumber.ACE_STARTING_SERIAL_NUMBER+1);
        addPlane(SerialNumber.ACE_STARTING_SERIAL_NUMBER+2);
        addPlane(SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
        addPlane(SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
    }

    private void addPlane(Integer pilotSerialNumber)
    {
        LogPilot pilot = new LogPilot();
        pilot.setSerialNumber(pilotSerialNumber);
        
        LogPlane plane1 = new LogPlane(1);
        plane1.setPilotSerialNumber(pilotSerialNumber);
        plane1.intializePilot(pilotSerialNumber);
        
        String planeId = pilotSerialNumber.toString();
        planeAiEntities.put(planeId, plane1);
    }

    @Test
    public void testSquadronMembers () throws PWCGException
    {        
        AARCrewBuilder crewBuilder = new AARCrewBuilder(planeAiEntities);
        List<LogPilot> inSquad = crewBuilder.buildPilotsFromLogPlanes();
        assert(pilotIsInList(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, inSquad) == true);
        assert(pilotIsInList(SerialNumber.ACE_STARTING_SERIAL_NUMBER+1, inSquad) == true);
        assert(pilotIsInList(SerialNumber.ACE_STARTING_SERIAL_NUMBER+2, inSquad) == true);
        assert(pilotIsInList(SerialNumber.AI_STARTING_SERIAL_NUMBER+1, inSquad) == true);
        assert(pilotIsInList(SerialNumber.AI_STARTING_SERIAL_NUMBER+2, inSquad) == true);
    }

    @Test
    public void testAcesMembers () throws PWCGException
    {        
        AARCrewBuilder crewBuilder = new AARCrewBuilder(planeAiEntities);
        List<LogPilot> aces = crewBuilder.buildAcesFromLogPlanes();
        assert(pilotIsInList(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, aces) == false);
        assert(pilotIsInList(SerialNumber.ACE_STARTING_SERIAL_NUMBER+1, aces) == true);
        assert(pilotIsInList(SerialNumber.ACE_STARTING_SERIAL_NUMBER+2, aces) == true);
        assert(pilotIsInList(SerialNumber.AI_STARTING_SERIAL_NUMBER+1, aces) == false);
        assert(pilotIsInList(SerialNumber.AI_STARTING_SERIAL_NUMBER+2, aces) == false);
    }
    
    public boolean pilotIsInList(Integer serialNumber, List<LogPilot> pilotList)
    {
        for (LogPilot pilot : pilotList)
        {
            if (pilot.getSerialNumber() == serialNumber)
            {
                return true;
            }
        }
        
        return false;
     }

}
