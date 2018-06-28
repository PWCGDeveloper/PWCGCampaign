package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.inmission.phase1.parse.AARLogEventData;
import pwcg.aar.inmission.phase1.parse.event.IAType12;
import pwcg.aar.inmission.phase1.parse.event.rof.AType12;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPilot;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.campaign.context.Country;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.core.exception.PWCGException;
import pwcg.mission.data.PwcgGeneratedMissionPlaneData;
import pwcg.testutils.TestATypeFactory;

public class TestMissionEntityGenerator
{
    private AARLogEventData logEventData;    
    private List<IAType12> vehicles = new ArrayList<>();
    private Map<String, IAType12> pilotBots = new HashMap<>();
    private Map<Integer, PwcgGeneratedMissionPlaneData> missionPlanes = new HashMap<>();
    private Map <String, LogPlane> planeAiEntities = new HashMap<>();

    public void makeMissionArtifacts(
                    int numFrenchPlanes, 
                    int numGermanPlanes,
                    int numFrenchBalloons, 
                    int numGermanBalloons,
                    int numFrenchTrucks, 
                    int numGermanTrucks) throws PWCGException
    {
        String[] frenchPilots = new String[] { "French PilotA","French PilotB"};
        String[] germanPilots = new String[] { "German PilotA","German PilotB"};
        Integer[] frenchPilotsSerialNumbers= new Integer[] { SerialNumber.AI_STARTING_SERIAL_NUMBER + 1, SerialNumber.AI_STARTING_SERIAL_NUMBER + 2};
        Integer[] germanPilotSerialNumbers = new Integer[] { SerialNumber.AI_STARTING_SERIAL_NUMBER + 100, SerialNumber.AI_STARTING_SERIAL_NUMBER + 200};
        String[] frenchPilotBotId = new String[] { "1001","1002"};
        String[] germanPilotBotId = new String[] { "2001","2002"};

        makeFrenchFighters(numFrenchPlanes, frenchPilotsSerialNumbers, frenchPilots, frenchPilotBotId);
        makeGermanFighters(numGermanPlanes, germanPilotSerialNumbers, germanPilots, germanPilotBotId);
        makeFrenchBalloons(numFrenchBalloons);
        makeGermanBalloons(numGermanBalloons);
        makeFrenchTrucks(numFrenchTrucks);
        makeGermanTrucks(numGermanTrucks);
        
        formAARLogParser();
    }

    private void formAARLogParser()
    {
        logEventData = new AARLogEventData();
        logEventData.setBots(pilotBots);
    }

    private void makeFrenchFighters(int numFrenchPlanes, Integer[] frenchPilotsSerialNumbers, String[] frenchPilots, String[] frenchPilotBotId) throws PWCGException
    {
        for (int i = 0; i < numFrenchPlanes; ++i)
        {            
            AType12 frenchPlane  = TestATypeFactory.makeFrenchPlane();
            frenchPlane.setName(frenchPilots[i]);
            frenchPlane.setId(frenchPilotBotId[i]);
            vehicles.add(frenchPlane);
            
            makePwcgMissionPlaneFighter(frenchPilots[i], frenchPilotsSerialNumbers[i], frenchPlane);
        }
    }

    private void makeGermanFighters(int numGermanPlanes, Integer[] germanPilotSerialNumbers, String[] germanPilots, String[] germanPilotBotId) throws PWCGException
    {
        for (int i = 0; i < numGermanPlanes; ++i)
        {
            AType12 germanPlane  = TestATypeFactory.makeGermanPlane();
            germanPlane.setName(germanPilots[i]);
            germanPlane.setId(germanPilotBotId[i]);
            vehicles.add(germanPlane);

            makePwcgMissionPlaneFighter(germanPilots[i], germanPilotSerialNumbers[i], germanPlane);
        }
    }

    private void makeFrenchBalloons(int numFrenchBalloons) throws PWCGException
    {
        for (int i = 0; i < numFrenchBalloons; ++i)
        {
            AType12 frenchBalloon  = TestATypeFactory.makeBalloon(CountryFactory.makeCountryByCountry(Country.FRANCE));
            vehicles.add(frenchBalloon);
        }
    }

    private void makeGermanBalloons(int numGermanBalloons) throws PWCGException
    {
        for (int i = 0; i < numGermanBalloons; ++i)
        {
            AType12 germanBalloon  = TestATypeFactory.makeBalloon(CountryFactory.makeCountryByCountry(Country.GERMANY));
            vehicles.add(germanBalloon);
        }
    }

    private void makeFrenchTrucks(int numFrenchTrucks) throws PWCGException
    {
        for (int i = 0; i < numFrenchTrucks; ++i)
        {
            AType12 frenchTruck  = TestATypeFactory.makeTruck(Country.FRANCE);
            vehicles.add(frenchTruck);
        }
    }

    private void makeGermanTrucks(int numGermanTrucks) throws PWCGException
    {
        for (int i = 0; i < numGermanTrucks; ++i)
        {
            AType12 germanTruck  = TestATypeFactory.makeTruck(Country.GERMANY);
            vehicles.add(germanTruck);
        }
    }

    private void makePwcgMissionPlaneFighter(String pilotName, Integer pilotSerialNumber, IAType12 plane) throws PWCGException
    {
        PwcgGeneratedMissionPlaneData pwcgMissionPlane = new PwcgGeneratedMissionPlaneData();
        pwcgMissionPlane.setAircraftType(plane.getType());
        
        pwcgMissionPlane.setPilotSerialNumber(pilotSerialNumber);;
        missionPlanes.put(pilotSerialNumber, pwcgMissionPlane);
        
        AType12 pilotBot = TestATypeFactory.makePilotBot(plane);
        pilotBots.put(pilotBot.getId(), pilotBot);
        
        makeMissionResultPlaneFighter(pilotName, pilotSerialNumber, plane);
    }

    private void makeMissionResultPlaneFighter(String pilotName, Integer pilotSerialNumber, IAType12 plane) throws PWCGException
    {
    	LogPlane missionResultPlane = new LogPlane();
        missionResultPlane.setVehicleType(plane.getType());
        missionResultPlane.setId(plane.getId());
        missionResultPlane.setSerialNumber(pilotSerialNumber);
        
        LogPilot pilotCrewMember = new LogPilot();
        pilotCrewMember.setSerialNumber(pilotSerialNumber);
        pilotCrewMember.setBotId("");
        
        missionResultPlane.intializePilot(pilotSerialNumber);;        
        planeAiEntities.put(plane.getId(), missionResultPlane);
    }

    public List<IAType12> getVehicles()
    {
        return vehicles;
    }

    public Map<Integer, PwcgGeneratedMissionPlaneData> getMissionPlanes()
    {
        return missionPlanes;
    }

    public PwcgGeneratedMissionPlaneData getMissionPlane(Integer key)
    {
        return missionPlanes.get(key);
    }

    public Map<String, LogPlane> getPlaneAiEntities()
    {
        return planeAiEntities;
    }

    public AARLogEventData getAARLogEventData()
    {
        return logEventData;
    }
}
