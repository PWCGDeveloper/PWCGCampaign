package pwcg.aar.integration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.inmission.phase1.parse.AARLogEventData;
import pwcg.aar.inmission.phase1.parse.AARMissionLogRawData;
import pwcg.aar.inmission.phase1.parse.event.IAType12;
import pwcg.aar.inmission.phase1.parse.event.bos.AType12;
import pwcg.aar.inmission.phase1.parse.event.bos.AType3;
import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.PlaneTypeFactory;
import pwcg.campaign.plane.SquadronPlaneAssignment;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

public class MissionLogEventsBuilder
{
    private Campaign campaign;
    private AARPreliminaryData preliminaryData;
    private AARMissionLogRawData missionLogRawData = new AARMissionLogRawData(); 
    private AARLogEventData logEventData = new AARLogEventData();
    private int nextMissionLogId = 400000;
    private Map<Integer, String> serialNumberToPlaneId = new HashMap<>();
    private List<String> destroyedPlanes = new ArrayList<>();
    private SquadronMember player;
    private SquadronMember pilotFromDifferentSquadron;
    private ExpectedResults expectedResults;

    public MissionLogEventsBuilder(Campaign campaign, AARPreliminaryData preliminaryData, ExpectedResults expectedResults)
    {
        this.campaign = campaign;
        this.preliminaryData = preliminaryData;
        this.expectedResults = expectedResults;
        this.missionLogRawData.setLogEventData(logEventData);
    }

    public AARMissionLogRawData makeLogEvents() throws PWCGException
    {
        makePilotsForVictories();
        makePlanes();
        makeTrucks();
        makewaypointEvents();
        makePlayerDestroyedEvents();
        makeOtherDestroyedEvents();
        makeDamagedEvents();
        makeLandingEvents();
        return missionLogRawData;
    }

    private void makePilotsForVictories() throws PWCGException
    {
        player = campaign.getPlayer();
        for (SquadronMember pilot : preliminaryData.getCampaignMembersInMission().getSquadronMembers().values())
        {
            if (pilot.getSquadronId() != player.getSquadronId())
            {
                Side playerSide = player.determineCountry(campaign.getDate()).getSide();
                Side pilotSide = pilot.determineCountry(campaign.getDate()).getSide();
                if (playerSide == pilotSide)
                {
                    pilotFromDifferentSquadron = pilot;
                    expectedResults.setOtherPilotSerialNumber(pilotFromDifferentSquadron.getSerialNumber());
                    break;
                }
            }
        }
    }

    private void makePlanes() throws PWCGException
    {
        for (SquadronMember pilot : preliminaryData.getCampaignMembersInMission().getSquadronMembers().values())
        {
            SquadronPlaneAssignment planeAssignment = AARCoordinatorMissionTest.getPlaneForSquadron(pilot.getSquadronId());
            
            PlaneTypeFactory planeTypeFactory = PWCGContextManager.getInstance().getPlaneTypeFactory();
            PlaneType planeType = planeTypeFactory.getPlaneById(planeAssignment.getType());
            AType12 planeSpawn = new AType12(
                    makeNextId(),
                    planeType.getDisplayName(),
                    pilot.getNameAndRank(),
                    pilot.determineCountry(campaign.getDate()),
                    "-1");
            
            logEventData.addVehicle(planeSpawn.getId(), planeSpawn);
            serialNumberToPlaneId.put(pilot.getSerialNumber(), planeSpawn.getId());

            makeBot(planeSpawn);
        }
    }

    private void makeTrucks() throws PWCGException
    {
        ICountry russia = CountryFactory.makeCountryByCountry(Country.RUSSIA);
        for (int i = 0; i < 10; ++i)
        {
            AType12 truckSpawn = new AType12(
                    makeNextId(),
                    "Truck",
                    "",
                    russia,
                    "-1");
            
            logEventData.addVehicle(truckSpawn.getId(), truckSpawn);
        }
    }
    
    private void makeBot(AType12 planeSpawn) throws PWCGException
    {
        AType12 botSpawn = new AType12(
                makeNextId(),
                "BotPilot",
                planeSpawn.getName(),
                planeSpawn.getCountry(),
                planeSpawn.getId());
        
        logEventData.addBot(botSpawn.getId(), botSpawn);
    }

    private void makewaypointEvents()
    {
    }

    private void makePlayerDestroyedEvents() throws PWCGException
    {
        Coordinate crashLocation = new Coordinate(100000, 0, 100000);

        String playerPlaneId = serialNumberToPlaneId.get(player.getSerialNumber());
        IAType12 playerPlane = logEventData.getVehicle(playerPlaneId);

        IAType12 destroyedPlane1 = getPlaneVictimByType("Yak-1 ser.69");
        AType3 destroyed1 = new AType3(playerPlane.getId(), destroyedPlane1.getId(), crashLocation);

        IAType12 destroyedPlane2 = getPlaneVictimByType("Il-2 mod.1941");
        AType3 destroyed2 = new AType3(playerPlane.getId(), destroyedPlane2.getId(), crashLocation);

        IAType12 destroyedGroundUnit1 = getGroundVictim();
        AType3 destroyedGround1 = new AType3(playerPlane.getId(), destroyedGroundUnit1.getId(), crashLocation);

        logEventData.addDestroyedEvent(destroyed1);
        expectedResults.addPlayerAirVictories();
        
        logEventData.addDestroyedEvent(destroyed2);
        expectedResults.addPlayerAirVictories();

        logEventData.addDestroyedEvent(destroyedGround1);
        expectedResults.addPlayerGroundVictories();
    }

    private void makeOtherDestroyedEvents() throws PWCGException
    {
        Coordinate crashLocation = new Coordinate(100000, 0, 100000);

        String otherPilotPlaneId = serialNumberToPlaneId.get(pilotFromDifferentSquadron.getSerialNumber());
        IAType12 otherPilotPlane = logEventData.getVehicle(otherPilotPlaneId);
        
        IAType12 destroyedPlane3 = getPlaneVictimByType("Il-2 mod.1941");
        AType3 destroyed3 = new AType3(otherPilotPlane.getId(), destroyedPlane3.getId(), crashLocation);

        IAType12 destroyedGroundUnit2 = getGroundVictim();
        AType3 destroyedGround2 = new AType3(otherPilotPlane.getId(), destroyedGroundUnit2.getId(), crashLocation);

        logEventData.addDestroyedEvent(destroyed3);
        expectedResults.addOtherAirVictories();

        logEventData.addDestroyedEvent(destroyedGround2);
        expectedResults.addOtherGroundVictories();
    }
    
    private IAType12 getPlaneVictimByType(String type) throws PWCGException
    {
        for (IAType12 planeSpawn : logEventData.getVehicles())
        {
            if (planeSpawn.getType().equals(type))
            {
                if (!destroyedPlanes.contains(planeSpawn.getId()))
                {
                    destroyedPlanes.add(planeSpawn.getId());
                    killPilot(planeSpawn.getId());
                    
                    return planeSpawn;
                }
            }
        }
        
        throw new PWCGException("failed to find spawn for type " + type);
    }
    
    private void killPilot(String planeId) throws PWCGException
    {
        Coordinate crashLocation = new Coordinate(100000, 0, 100000);
        for (IAType12 botSpawn : logEventData.getBots())
        {
            if (botSpawn.getPid().equals(planeId))
            {
                AType3 pilotKilled = new AType3("-1", botSpawn.getId(), crashLocation);
                logEventData.addDestroyedEvent(pilotKilled);
            }
        }
    }

    private IAType12 getGroundVictim() throws PWCGException
    {
        for (IAType12 spawn : logEventData.getVehicles())
        {
            if (spawn.getType().equals("Truck"))
            {
                if (!destroyedPlanes.contains(spawn.getId()))
                {
                    destroyedPlanes.add(spawn.getId());
                    return spawn;
                }
            }
        }
        
        throw new PWCGException("failed to find spawn for truck ");
    }

    private void makeDamagedEvents()
    {
    }

    private void makeLandingEvents()
    {
    }

    private String makeNextId()
    {
        String nextId = new Integer(nextMissionLogId).toString();
        ++nextMissionLogId;
        return nextId;
    }

    public SquadronMember getPilotFromDifferentSquadron()
    {
        return pilotFromDifferentSquadron;
    }
    
    
}
