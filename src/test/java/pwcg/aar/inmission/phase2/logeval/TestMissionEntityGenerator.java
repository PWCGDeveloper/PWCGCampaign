package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogCrewMember;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.campaign.context.Country;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogEventData;
import pwcg.core.logfiles.event.AType12;
import pwcg.core.logfiles.event.IAType12;
import pwcg.mission.data.PwcgGeneratedMissionVehicleData;
import pwcg.testutils.TestATypeFactory;

public class TestMissionEntityGenerator
{
    private LogEventData logEventData;    
    private List<IAType12> vehicles = new ArrayList<>();
    private Map<String, IAType12> crewMemberBots = new HashMap<>();
    private Map<Integer, PwcgGeneratedMissionVehicleData> missionPlanes = new HashMap<>();
    private Map <String, LogPlane> planeAiEntities = new HashMap<>();

    public void makeMissionArtifacts(
                    int numFrenchPlanes, 
                    int numGermanPlanes,
                    int numFrenchBalloons, 
                    int numGermanBalloons,
                    int numFrenchTrucks, 
                    int numGermanTrucks) throws PWCGException
    {
        String[] frenchCrewMembers = new String[] { "French CrewMemberA","French CrewMemberB"};
        String[] germanCrewMembers = new String[] { "German CrewMemberA","German CrewMemberB"};
        Integer[] frenchCrewMembersSerialNumbers= new Integer[] { SerialNumber.AI_STARTING_SERIAL_NUMBER + 1, SerialNumber.AI_STARTING_SERIAL_NUMBER + 2};
        Integer[] frenchPlaneSerialNumbers= new Integer[] { SerialNumber.PLANE_STARTING_SERIAL_NUMBER + 1, SerialNumber.PLANE_STARTING_SERIAL_NUMBER + 2};
        Integer[] germanCrewMemberSerialNumbers = new Integer[] { SerialNumber.AI_STARTING_SERIAL_NUMBER + 100, SerialNumber.AI_STARTING_SERIAL_NUMBER + 200};
        Integer[] germanPlaneSerialNumbers= new Integer[] { SerialNumber.PLANE_STARTING_SERIAL_NUMBER + 100, SerialNumber.PLANE_STARTING_SERIAL_NUMBER + 200};
        String[] frenchCrewMemberBotId = new String[] { "1001","1002"};
        String[] germanCrewMemberBotId = new String[] { "2001","2002"};

        makeFrenchFighters(numFrenchPlanes, frenchCrewMembersSerialNumbers, frenchPlaneSerialNumbers, frenchCrewMembers, frenchCrewMemberBotId);
        makeGermanFighters(numGermanPlanes, germanCrewMemberSerialNumbers, germanPlaneSerialNumbers, germanCrewMembers, germanCrewMemberBotId);
        makeFrenchBalloons(numFrenchBalloons);
        makeGermanBalloons(numGermanBalloons);
        makeFrenchTrucks(numFrenchTrucks);
        makeGermanTrucks(numGermanTrucks);
        
        formAARLogParser();
    }

    private void formAARLogParser()
    {
        logEventData = new LogEventData();
        logEventData.setBots(crewMemberBots);
    }

    private void makeFrenchFighters(int numFrenchPlanes, Integer[] frenchCrewMembersSerialNumbers, Integer[] frenchPlaneSerialNumbers, String[] frenchCrewMembers, String[] frenchCrewMemberBotId) throws PWCGException
    {
        for (int i = 0; i < numFrenchPlanes; ++i)
        {            
            AType12 frenchPlane  = TestATypeFactory.makeFrenchPlane(frenchCrewMembers[i], frenchCrewMemberBotId[i]);
            vehicles.add(frenchPlane);
            
            makePwcgMissionPlaneFighter(frenchCrewMembers[i], frenchCrewMembersSerialNumbers[i], frenchPlaneSerialNumbers[i], frenchPlane);
        }
    }

    private void makeGermanFighters(int numGermanPlanes, Integer[] germanCrewMemberSerialNumbers, Integer[] germanPlaneSerialNumbers, String[] germanCrewMembers, String[] germanCrewMemberBotId) throws PWCGException
    {
        for (int i = 0; i < numGermanPlanes; ++i)
        {
            AType12 germanPlane  = TestATypeFactory.makeGermanPlane(germanCrewMembers[i], germanCrewMemberBotId[i]);
            vehicles.add(germanPlane);

            makePwcgMissionPlaneFighter(germanCrewMembers[i], germanCrewMemberSerialNumbers[i], germanPlaneSerialNumbers[i], germanPlane);
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

    private void makePwcgMissionPlaneFighter(String crewMemberName, Integer crewMemberSerialNumber, Integer planeSerialNumber, IAType12 plane) throws PWCGException
    {
        PwcgGeneratedMissionVehicleData pwcgMissionPlane = new PwcgGeneratedMissionVehicleData();
        pwcgMissionPlane.setVehicleType(plane.getType());
        pwcgMissionPlane.setCrewMemberSerialNumber(crewMemberSerialNumber);
        pwcgMissionPlane.setVehicleSerialNumber(planeSerialNumber);
        missionPlanes.put(crewMemberSerialNumber, pwcgMissionPlane);
        
        AType12 crewMemberBot = TestATypeFactory.makeCrewMemberBot(plane);
        crewMemberBots.put(crewMemberBot.getId(), crewMemberBot);
        
        makeMissionResultPlaneFighter(crewMemberName, crewMemberSerialNumber, planeSerialNumber, plane);
    }

    private void makeMissionResultPlaneFighter(String crewMemberName, Integer crewMemberSerialNumber, Integer planeSerialNumber, IAType12 plane) throws PWCGException
    {
    	LogPlane missionResultPlane = new LogPlane(1);
        missionResultPlane.setVehicleType(plane.getType());
        missionResultPlane.setId(plane.getId());
        missionResultPlane.setCrewMemberSerialNumber(crewMemberSerialNumber);
        missionResultPlane.setPlaneSerialNumber(planeSerialNumber);

        LogCrewMember crewMemberCrewMember = new LogCrewMember();
        crewMemberCrewMember.setSerialNumber(crewMemberSerialNumber);
        crewMemberCrewMember.setBotId("");
        
        missionResultPlane.intializeCrewMember(crewMemberSerialNumber);;        
        planeAiEntities.put(plane.getId(), missionResultPlane);
    }

    public List<IAType12> getVehicles()
    {
        return vehicles;
    }

    public Map<Integer, PwcgGeneratedMissionVehicleData> getMissionPlanes()
    {
        return missionPlanes;
    }

    public PwcgGeneratedMissionVehicleData getMissionPlane(Integer key)
    {
        return missionPlanes.get(key);
    }

    public Map<String, LogPlane> getPlaneAiEntities()
    {
        return planeAiEntities;
    }

    public LogEventData getAARLogEventData()
    {
        return logEventData;
    }
}
