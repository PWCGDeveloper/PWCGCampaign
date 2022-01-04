package pwcg.aar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.plane.TankType;
import pwcg.campaign.plane.TankTypeFactory;
import pwcg.campaign.plane.SquadronPlaneAssignment;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.logfiles.LogEventData;
import pwcg.core.logfiles.event.AType12;
import pwcg.core.logfiles.event.AType3;
import pwcg.core.logfiles.event.IAType12;
import pwcg.core.utils.RandomNumberGenerator;

public class MissionLogEventsBuilder
{
    private Campaign campaign;
    private AARPreliminaryData preliminaryData;
    private LogEventData logEventData = new LogEventData();
    private int nextMissionLogId = 400000;
    private Map<Integer, String> serialNumberToPlaneId = new HashMap<>();
    private List<String> destroyedPlanes = new ArrayList<>();
    private CrewMember squadronMate;
    private CrewMember friendlyCrewMemberFromDifferentSquadron;
    private ExpectedResults expectedResults;

    public MissionLogEventsBuilder(Campaign campaign, AARPreliminaryData preliminaryData, ExpectedResults expectedResults)
    {
        this.campaign = campaign;
        this.preliminaryData = preliminaryData;
        this.expectedResults = expectedResults;
    }

    public LogEventData makeLogEvents() throws PWCGException
    {
        makeCrewMembersForVictories();
        makePlanes();
        makeTrucks();
        makewaypointEvents();
        makePlayerDestroyedEvents();
        makeOtherDestroyedEvents();
        makeSquadronMateLostEvents();
        makeDamagedEvents();
        makeLandingEvents();
        return logEventData;
    }

    private void makeCrewMembersForVictories() throws PWCGException
    {
        CrewMember player = campaign.getPersonnelManager().getPlayersInMission().getCrewMemberList().get(0);
        for (CrewMember crewMember : preliminaryData.getCampaignMembersInMission().getCrewMemberCollection().values())
        {
            if (crewMember.getCompanyId() != player.getCompanyId())
            {
                Side playerSide = player.determineCountry(campaign.getDate()).getSide();
                Side crewMemberSide = crewMember.determineCountry(campaign.getDate()).getSide();
                if (playerSide == crewMemberSide)
                {
                    friendlyCrewMemberFromDifferentSquadron = crewMember;
                    expectedResults.setCrewMemberCrewMemberSerialNumber(friendlyCrewMemberFromDifferentSquadron.getSerialNumber());
                }
            }
            else
            {
                if (!crewMember.isPlayer())
                {
                    squadronMate = crewMember;
                }
            }
        }
    }

    private void makePlanes() throws PWCGException
    {
        for (CrewMember crewMember : preliminaryData.getCampaignMembersInMission().getCrewMemberCollection().values())
        {
            CompanyTankAssignment planeAssignment = AARCoordinatorInMissionTest.getPlaneForSquadron(crewMember.getCompanyId());
            TankTypeFactory planeTypeFactory = PWCGContext.getInstance().getTankTypeFactory();
            List<TankType> planeTypesForSquadron = planeTypeFactory.createActiveTankTypesForArchType(planeAssignment.getArchType(), campaign.getDate());
            int index = RandomNumberGenerator.getRandom(planeTypesForSquadron.size());
            TankType planeType = planeTypesForSquadron.get(index);
            AType12 planeSpawn = new AType12(
                    makeNextId(),
                    planeType.getDisplayName(),
                    crewMember.getNameAndRank(),
                    crewMember.determineCountry(campaign.getDate()),
                    "-1",
                    new Coordinate(500000, 0, 50000));
            
            logEventData.addVehicle(planeSpawn.getId(), planeSpawn);
            serialNumberToPlaneId.put(crewMember.getSerialNumber(), planeSpawn.getId());

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
                    "-1",
                    new Coordinate(500000, 0, 50000));
            
            logEventData.addVehicle(truckSpawn.getId(), truckSpawn);
        }
    }
    
    private void makeBot(AType12 planeSpawn) throws PWCGException
    {
        AType12 botSpawn = new AType12(
                makeNextId(),
                "BotCrewMember",
                planeSpawn.getName(),
                planeSpawn.getCountry(),
                planeSpawn.getId(),
                new Coordinate(500000, 0, 50000));
        
        logEventData.addBot(botSpawn.getId(), botSpawn);
    }

    private void makewaypointEvents()
    {
    }

    private void makePlayerDestroyedEvents() throws PWCGException
    {
        Coordinate crashLocation = new Coordinate(100000, 0, 100000);

        CrewMember player = campaign.getPersonnelManager().getPlayersInMission().getCrewMemberList().get(0);
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

        String friendlyCrewMemberPlaneId = serialNumberToPlaneId.get(friendlyCrewMemberFromDifferentSquadron.getSerialNumber());
        IAType12 friendlyCrewMemberPlane = logEventData.getVehicle(friendlyCrewMemberPlaneId);
        
        IAType12 enemyPlaneLost = getPlaneVictimByType("Il-2 mod.1941");
        AType3 enemyPlaneLostEvent = new AType3(friendlyCrewMemberPlane.getId(), enemyPlaneLost.getId(), crashLocation);

        IAType12 enemyVehicleLost = getGroundVictim();
        AType3 enemyVehicleLostEvent = new AType3(friendlyCrewMemberPlane.getId(), enemyVehicleLost.getId(), crashLocation);

        logEventData.addDestroyedEvent(enemyPlaneLostEvent);
        expectedResults.addCrewMemberAirVictories();

        logEventData.addDestroyedEvent(enemyVehicleLostEvent);
        expectedResults.addCrewMemberGroundVictories();
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
                    killCrewMember(planeSpawn.getId());
                    
                    return planeSpawn;
                }
            }
        }
        
        throw new PWCGException("failed to find spawn for type " + type);
    }
    
    private void killCrewMember(String planeId) throws PWCGException
    {
        Coordinate crashLocation = new Coordinate(100000, 0, 100000);
        for (IAType12 botSpawn : logEventData.getBots())
        {
            if (botSpawn.getPid().equals(planeId))
            {
                AType3 crewMemberKilled = new AType3("-1", botSpawn.getId(), crashLocation);
                logEventData.addDestroyedEvent(crewMemberKilled);
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

    public CrewMember getCrewMemberFromDifferentSquadron()
    {
        return friendlyCrewMemberFromDifferentSquadron;
    }
    
    
}
