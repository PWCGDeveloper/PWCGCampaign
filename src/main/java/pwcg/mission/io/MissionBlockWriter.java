package pwcg.mission.io;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.campaign.group.FixedPosition;
import pwcg.core.exception.PWCGException;
import pwcg.mission.MissionBlockBuilder;

public class MissionBlockWriter
{    
    protected MissionBlockBuilder missionBlockBuilder = null;
	
	public MissionBlockWriter (MissionBlockBuilder missionBlockBuilder)
	{
		this.missionBlockBuilder = missionBlockBuilder;
	}

	public void writeFixedPositions(BufferedWriter writer, List<FixedPosition> fixedPositions) throws PWCGException
    {
        for (FixedPosition fixedPosition : fixedPositions)
        {
            fixedPosition.write(writer);
        }
    }
}
