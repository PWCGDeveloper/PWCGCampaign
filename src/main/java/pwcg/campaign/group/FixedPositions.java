package pwcg.campaign.group;

import java.util.HashMap;
import java.util.Map;

public class FixedPositions
{
	private Map<String, FixedPosition> map = new HashMap<>();

	public Map<String, FixedPosition> getFixedPositions()
	{
		return map;
	}

	public void setgetFixedPositions(Map<String, FixedPosition> map)
	{
		this.map = map;
	}

	public void addFixedPosition(String itemName, FixedPosition item)
	{
		this.map.put(itemName, item);
	}	

	public FixedPosition getFixedPosition(String itemName)
	{
		return map.get(itemName);
	}	

}
