package pwcg.mission.io;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.campaign.group.FixedPosition;
import pwcg.core.exception.PWCGException;

public class MissionBlockWriter
{    
	public static void writeFixedPositions(BufferedWriter writer, List<FixedPosition> missionBlocks) throws PWCGException
    {
        for (FixedPosition fixedPosition : missionBlocks)
        {
            fixedPosition.write(writer);
        }
    }
}
