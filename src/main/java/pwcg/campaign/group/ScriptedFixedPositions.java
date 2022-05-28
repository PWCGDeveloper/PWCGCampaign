package pwcg.campaign.group;

import java.util.HashMap;
import java.util.Map;

public class ScriptedFixedPositions
{
	private Map<String, ScriptedFixedPosition> map = new HashMap<>();

	public Map<String, ScriptedFixedPosition> getFixedPositions()
	{
		return map;
	}

	public void setFixedPositions(Map<String, ScriptedFixedPosition> map)
	{
		this.map = map;
	}

	public void addFixedPosition(String itemName, ScriptedFixedPosition item)
	{
		this.map.put(itemName, item);
	}	

	public ScriptedFixedPosition getFixedPosition(String itemName)
	{
		return map.get(itemName);
	}	

}
