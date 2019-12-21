package pwcg.aar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.inmission.phase1.parse.AARLogEventData;
import pwcg.aar.inmission.phase1.parse.AARMissionLogRawData;
import pwcg.aar.inmission.phase1.parse.event.AType12;
import pwcg.aar.inmission.phase1.parse.event.AType3;
import pwcg.aar.inmission.phase1.parse.event.IAType12;
import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.PlaneTypeFactory;
import pwcg.campaign.plane.SquadronPlaneAssignment;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;

public class MissionLogEventsBuilder
{
    private Campaign campaign;
    private AARPreliminaryData preliminaryData;
    private AARMissionLogRawData missionLogRawData = new AARMissionLogRawData(); 
    private AARLogEventData logEventData = new AARLogEventData();
    private int nextMissionLogId = 400000;
    private Map<Integer, String> serialNumberToPlaneId = new HashMap<>();
    private List<String> destroyedPlanes = new ArrayList<>();
    private SquadronMember squadronMate;
    private SquadronMember friendlyPilotFromDifferentSquadron;
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
        makeSquadronMateLostEvents();
        makeDamagedEvents();
        makeLandingEvents();
        return missionLogRawData;
    }

    private void makePilotsForVictories() throws PWCGException
    {
        SquadronMember player = campaign.getPersonnelManager().getFlyingPlayers().getSquadronMemberList().get(0);
        for (SquadronMember pilot : preliminaryData.getCampaignMembersInMission().getSquadronMemberCollection().values())
        {
            if (pilot.getSquadronId() != player.getSquadronId())
            {
                Side playerSide = player.determineCountry(campaign.getDate()).getSide();
                Side pilotSide = pilot.determineCountry(campaign.getDate()).getSide();
                if (playerSide == pilotSide)
                {
                    friendlyPilotFromDifferentSquadron = pilot;
                    expectedResults.setSquadronMemberPilotSerialNumber(friendlyPilotFromDifferentSquadron.getSerialNumber());
                }
            }
            else
            {
                if (!pilot.isPlayer())
                {
                    squadronMate = pilot;
                }
            }
        }
    }

    private void makePlanes() throws PWCGException
    {
        for (SquadronMember pilot : preliminaryData.getCampaignMembersInMission().getSquadronMemberCollection().values())
        {
            SquadronPlaneAssignment planeAssignment = AARCoordinatorInMissionTest.getPlaneForSquadron(pilot.getSquadronId());
            PlaneTypeFactory planeTypeFactory = PWCGContext.getInstance().getPlaneTypeFactory();
            List<PlaneType> planeTypesForSquadron = planeTypeFactory.createActivePlaneTypesForArchType(planeAssignment.getArchType(), campaign.getDate());
            int index = RandomNumberGenerator.getRandom(planeTypesForSquadron.size());
            PlaneType planeType = planeTypesForSquadron.get(index);
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

        SquadronMember player = campaign.getPersonnelManager().getFlyingPlayers().getSquadronMemberList().get(0);
        String playerPlaneId = serialNumberToPlaneId.get(player.getSerialNumber());
        IAType12 playerPlane = logEventData.getVehicle(playerPlaneId);

        IAType12 playerVictoryPlane1 = getPlaneVictimByType("LaGG-3 ser.29");
        AType3 playerVictoryPlaneEvent1 = new AType3(playerPlane.getId(), playerVictoryPlane1.getId(), crashLocation);

        IAType12 playerVictoryPlane2 = getPlaneVictimByType("Il-2 mod.1941");
        AType3 playerVictoryPlaneEvent2 = new AType3(playerPlane.getId(), playerVictoryPlane2.getId(), crashLocation);

        IAType12 playerVictoryVehicle = getGroundVictim();
        AType3 playerVictoryVehicleEvent = new AType3(playerPlane.getId(), playerVictoryVehicle.getId(), crashLocation);

        logEventData.addDestroyedEvent(playerVictoryPlaneEvent1);
        expectedResults.addPlayerAirVictories();
        
        logEventData.addDestroyedEvent(playerVictoryPlaneEvent2);
        expectedResults.addPlayerAirVictories();

        logEventData.addDestroyedEvent(playerVictoryVehicleEvent);
        expectedResults.addPlayerGroundVictories();
    }

    private void makeOtherDestroyedEvents() throws PWCGException
    {
        Coordinate crashLocation = new Coordinate(100000, 0, 100000);

        String friendlyPilotPlaneId = serialNumberToPlaneId.get(friendlyPilotFromDifferentSquadron.getSerialNumber());
        IAType12 friendlyPilotPlane = logEventData.getVehicle(friendlyPilotPlaneId);
        
        IAType12 enemyPlaneLost = getPlaneVictimByType("Il-2 mod.1941");
        AType3 enemyPlaneLostEvent = new AType3(friendlyPilotPlane.getId(), enemyPlaneLost.getId(), crashLocation);

        IAType12 enemyVehicleLost = getGroundVictim();
        AType3 enemyVehicleLostEvent = new AType3(friendlyPilotPlane.getId(), enemyVehicleLost.getId(), crashLocation);

        logEventData.addDestroyedEvent(enemyPlaneLostEvent);
        expectedResults.addSquadronMemberAirVictories();

        logEventData.addDestroyedEvent(enemyVehicleLostEvent);
        expectedResults.addSquadronMemberGroundVictories();
    }

    private void makeSquadronMateLostEvents() throws PWCGException
    {
        Coordinate crashLocation = new Coordinate(100000, 0, 100000);

        String squadronMatePlaneId = serialNumberToPlaneId.get(squadronMate.getSerialNumber());
        IAType12 squadronMatePlane = logEventData.getVehicle(squadronMatePlaneId);

        IAType12 enemyVictoryPlane = getPlaneVictimByType("LaGG-3 ser.29");
        AType3 squadronMatePlaneDestroyedEvent = new AType3(enemyVictoryPlane.getId(), squadronMatePlane.getId(), crashLocation);

        logEventData.addDestroyedEvent(squadronMatePlaneDestroyedEvent);
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
        String nextId = Integer.valueOf(nextMissionLogId).toString();
        ++nextMissionLogId;
        return nextId;
    }

    public SquadronMember getPilotFromDifferentSquadron()
    {
        return friendlyPilotFromDifferentSquadron;
    }
    
    
}
