package pwcg.campaign.group;

import java.util.ArrayList;
import java.util.List;

public class NonScriptedBlockPositions
{
	private List<NonScriptedBlock> groundObjects = new ArrayList<>();

	public List<NonScriptedBlock> getNonScriptedGroundPositions()
	{
		return groundObjects;
	}

	public void addNonScriptedGround(NonScriptedBlock item)
	{
		this.groundObjects.add(item);
	}	
}
