package pwcg.mission.io;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.campaign.group.NonScriptedBlock;
import pwcg.campaign.group.ScriptedFixedPosition;
import pwcg.core.exception.PWCGException;

public class MissionBlockWriter
{    
	public static void writeFixedPositions(BufferedWriter writer, List<ScriptedFixedPosition> missionBlocks) throws PWCGException
    {
        for (ScriptedFixedPosition fixedPosition : missionBlocks)
        {
            fixedPosition.write(writer);
        }
    }

	public static void writeNonScriptedFixedPositions(BufferedWriter writer, List<NonScriptedBlock> nonScriptedMissionBlocks) throws PWCGException
    {
        for (NonScriptedBlock nonScriptedBlock : nonScriptedMissionBlocks)
        {
            nonScriptedBlock.write(writer);
        }
    }
}
