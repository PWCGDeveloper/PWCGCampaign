package pwcg.aar.inmission.phase1.parse;

import java.util.List;

import pwcg.aar.inmission.phase1.parse.event.AType;
import pwcg.aar.inmission.phase1.parse.event.IAType12;
import pwcg.aar.inmission.phase1.parse.event.IAType18;
import pwcg.aar.inmission.phase1.parse.event.IAType2;
import pwcg.aar.inmission.phase1.parse.event.IAType3;
import pwcg.aar.inmission.phase1.parse.event.IAType6;
import pwcg.aar.inmission.phase1.parse.event.LogEventFactory;
import pwcg.core.exception.PWCGException;

public class AARLogLineParser
{
    private AARLogEventData logEventData = new AARLogEventData();

    public AARLogLineParser()
    {
    }

    public AARLogEventData parseLogLinesForMission(List<String> logLines) throws PWCGException 
    {
        for (String line : logLines)
        {
            parseLogLine(line);
        }
        return logEventData;
    }

    public void parseLogLine(String line) throws PWCGException
    {
        if (line.contains(AType.ATYPE2.getAtypeLogIdentifier()))
        {
            parseDamagedEvent(line);
        }
        else if (line.contains(AType.ATYPE3.getAtypeLogIdentifier()))
        {
            parseDestroyedEvent(line);
        }
        else if (line.contains(AType.ATYPE6.getAtypeLogIdentifier()))
        {
            parseLandingEvent(line);
        }
        else if (line.contains(AType.ATYPE10.getAtypeLogIdentifier()))
        {
            parsePlayerSpawnEvent(line);
        }
        else if (line.contains(AType.ATYPE12.getAtypeLogIdentifier()))
        {
            parseSpawnEvent(line);
        }
        else if (line.contains(AType.ATYPE17.getAtypeLogIdentifier()))
        {
            parseWaypointEvent(line);
        }
        else if (line.contains(AType.ATYPE18.getAtypeLogIdentifier()))
        {
            parseBailoutEvent(line);
        }
    }

    private void parseDamagedEvent(String line) throws PWCGException
    {
        IAType2 atype2 = LogEventFactory.createAType2(line);
        logEventData.addDamageEvent(atype2);
    }

    private void parseDestroyedEvent(String line) throws PWCGException
    {
        IAType3 atype3 = LogEventFactory.createAType3(line);
        logEventData.addDestroyedEvent(atype3);
    }

    private void parseLandingEvent(String line) throws PWCGException
    {
        IAType6 atype6 = LogEventFactory.createAType6(line);
        logEventData.addLandingEvent(atype6);
    }

    private void parsePlayerSpawnEvent(String line) throws PWCGException
    {
        // AType 10 currently triggers for any plane, not just player plane
        //IAType10 atype10 = LogEventFactory.createAType10(line);
        //IAType17 atype17 = new AType17(atype10);   
        //logEventData.addWaypointEvent(atype17);
    }

    private void parseSpawnEvent(String line) throws PWCGException
    {
        IAType12 atype12 = LogEventFactory.createAType12(line);
        mapSpawnToMissionArtifactType(atype12);
    }

    private void mapSpawnToMissionArtifactType(IAType12 atype12)
    {
        if (!isValidSpawnPoint(atype12))
        {
            return;
        }

        if(atype12.getType().contains("Bot"))
        {
            logEventData.addBot(atype12.getId(), atype12);
        }
        else if (atype12.getPid().contains(AARLogParser.UNKNOWN_MISSION_LOG_ENTITY))
        {
            logEventData.addVehicle(atype12.getId(), atype12);
        }
        else
        {
            logEventData.addTurret(atype12.getId(), atype12);
        }
    }

    private boolean isValidSpawnPoint(IAType12 atype12)
    {
        if (atype12.getPosition().getXPos() < 1.0 && atype12.getPosition().getZPos() < 1.0)
        {
            return false;
        }
        return true;
    }

    private void parseWaypointEvent(String line) throws PWCGException
    {
        // AType 17 is no longer logged
        //IAType17 atype17 = LogEventFactory.createAType17(line);
        //logEventData.addWaypointEvent(atype17);
    }

    private void parseBailoutEvent(String line) throws PWCGException
    {
        IAType18 atype18 = LogEventFactory.createAType18(line);
        logEventData.addBailoutEvent(atype18);
    }
}

