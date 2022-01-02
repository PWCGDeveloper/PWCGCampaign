package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogBase;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogDamage;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogWaypoint;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class AARChronologicalEventListBuilder
{
    private AAREvaluator evaluator;
    private AARWaypointBuilder aarWaypointBuilder;
    private TreeMap<Integer, LogBase> chronologicalEvents = new TreeMap<Integer, LogBase>();

    public AARChronologicalEventListBuilder (AAREvaluator evaluator, AARWaypointBuilder aarWaypointBuilder)
    {
        this.evaluator = evaluator;
        this.aarWaypointBuilder = aarWaypointBuilder;
    }
    
    public void buildChronologicalList() 
    {
        addWaypointEventsToChronologicalList();
        addPlaneSpawnEventsToChronologicalList();
        addBalloonSpawnEventsToChronologicalList();
        addConfirmedVictoriesEventsToChronologicalList();
        addDamageEventsToChronologicalList();
    }

    private void addWaypointEventsToChronologicalList()
    {
        List<LogWaypoint> waypointEventList =  aarWaypointBuilder.buildWaypointEvents();
        for (LogWaypoint resultWP : waypointEventList)
        {
            PWCGLogger.log(LogLevel.DEBUG, "" + resultWP.getSequenceNum() + ": " + "WP");
            chronologicalEvents.put(resultWP.getSequenceNum(), resultWP);
        }
    }

    private void addPlaneSpawnEventsToChronologicalList()
    {
        for (LogAIEntity logCrewMember : evaluator.getAarVehicleBuilder().getLogPlanes().values())
        {
            PWCGLogger.log(LogLevel.DEBUG, "" + logCrewMember.getSequenceNum() + ": " + "CrewMember: " + logCrewMember.getId());
            chronologicalEvents.put(logCrewMember.getSequenceNum(), logCrewMember);
        }
    }

    private void addBalloonSpawnEventsToChronologicalList()
    {
        for (LogAIEntity logBalloon : evaluator.getAarVehicleBuilder().getLogBalloons().values())
        {
            PWCGLogger.log(LogLevel.DEBUG, "" + logBalloon.getSequenceNum() + ": " + "Balloon: " + logBalloon.getId());
            chronologicalEvents.put(logBalloon.getSequenceNum(), logBalloon);
        }
    }

    private void addConfirmedVictoriesEventsToChronologicalList()
    {
        for (LogVictory logVictory : evaluator.getAarVictoryEvaluator().getVictoryResults())
        {
            PWCGLogger.log(LogLevel.DEBUG, "" + logVictory.getSequenceNum() + ": " + "Victory: " + logVictory.getVictor().getId() + " over " + logVictory.getVictim().getId());
            chronologicalEvents.put(logVictory.getSequenceNum(), logVictory);
        }
    }

    private void addDamageEventsToChronologicalList()
    {
        for (LogDamage logDamage : evaluator.getAarDamageStatusEvaluator().getAllDamageEvents())
        {
            PWCGLogger.log(LogLevel.DEBUG, "" + logDamage.getSequenceNum() + ": " + "Damage: " + logDamage.getVictor().getId() + " over " + logDamage.getVictim().getId());
            chronologicalEvents.put(logDamage.getSequenceNum(), logDamage);
        }
    }

    public List<LogBase> getChronologicalEvents()
    {
        return new ArrayList<LogBase>(chronologicalEvents.values());
    }
}
