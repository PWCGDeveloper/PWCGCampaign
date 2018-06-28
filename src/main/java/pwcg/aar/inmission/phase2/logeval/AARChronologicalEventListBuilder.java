package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogAIEntity;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogBase;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogDamage;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogWaypoint;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;

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
            Logger.log(LogLevel.DEBUG, "" + resultWP.getSequenceNum() + ": " + "WP");
            chronologicalEvents.put(resultWP.getSequenceNum(), resultWP);
        }
    }

    private void addPlaneSpawnEventsToChronologicalList()
    {
        for (LogAIEntity logPilot : evaluator.getAarVehicleBuilder().getLogPlanes().values())
        {
            Logger.log(LogLevel.DEBUG, "" + logPilot.getSequenceNum() + ": " + "Pilot: " + logPilot.getId());
            chronologicalEvents.put(logPilot.getSequenceNum(), logPilot);
        }
    }

    private void addBalloonSpawnEventsToChronologicalList()
    {
        for (LogAIEntity logBalloon : evaluator.getAarVehicleBuilder().getLogBalloons().values())
        {
            Logger.log(LogLevel.DEBUG, "" + logBalloon.getSequenceNum() + ": " + "Balloon: " + logBalloon.getId());
            chronologicalEvents.put(logBalloon.getSequenceNum(), logBalloon);
        }
    }

    private void addConfirmedVictoriesEventsToChronologicalList()
    {
        for (LogVictory logVictory : evaluator.getAarVictoryEvaluator().getVictoryResults())
        {
            Logger.log(LogLevel.DEBUG, "" + logVictory.getSequenceNum() + ": " + "Victory: " + logVictory.getVictor().getId() + " over " + logVictory.getVictim().getId());
            chronologicalEvents.put(logVictory.getSequenceNum(), logVictory);
        }
    }

    private void addDamageEventsToChronologicalList()
    {
        for (LogDamage logDamage : evaluator.getAarDamageStatusEvaluator().getVehiclesDamagedByPlayer())
        {
            Logger.log(LogLevel.DEBUG, "" + logDamage.getSequenceNum() + ": " + "Damage: " + logDamage.getVictor().getId() + " over " + logDamage.getVictim().getId());
            chronologicalEvents.put(logDamage.getSequenceNum(), logDamage);
        }
    }

    public List<LogBase> getChronologicalEvents()
    {
        return new ArrayList<LogBase>(chronologicalEvents.values());
    }
}
