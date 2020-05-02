package pwcg.core.location;

import pwcg.campaign.api.Side;
import pwcg.mission.mcu.McuIcon;
import pwcg.mission.mcu.McuIconFactory;

public class McuIconToLocationConverter
{
	public static McuIcon locationToIcon(PWCGLocation location, String description, Side side)
	{
		McuIcon icon = McuIconFactory.buildLocationToIcon(description, description, side);
		icon.setName(location.getName());
		icon.setDesc(location.getName());
		icon.setPosition(location.getPosition());
		icon.setOrientation(location.getOrientation());
		return icon;
	}
	
	public static PWCGLocation iconToLocation(McuIcon icon)
	{
		PWCGLocation location = new PWCGLocation();
		location.setName(icon.getName());
		location.setPosition(icon.getPosition());
		location.setOrientation(icon.getOrientation());
		return location;
	}
}
