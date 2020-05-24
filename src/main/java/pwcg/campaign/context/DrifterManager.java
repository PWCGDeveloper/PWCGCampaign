package pwcg.campaign.context;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.io.json.LocationIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.LocationSet;
import pwcg.core.location.PWCGLocation;

public class DrifterManager
{
	public static String BARGE_FILE_NAME = "BargePositions";
	
    private LocationSet bargePositions = new LocationSet(BARGE_FILE_NAME);

    public DrifterManager()
	{
	}

    public void configure(String mapName) throws PWCGException
	{
		String pwcgInputDir = PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\";
    	bargePositions = LocationIOJson.readJson(pwcgInputDir, BARGE_FILE_NAME);
	}

    public LocationSet getBargePositions()
    {
        return bargePositions;
    }

    public List<PWCGLocation> getBargePositionsForSide(Campaign campaign, Side side) throws PWCGException
    {
        List<PWCGLocation> bargePositionsForSide = new ArrayList<>();
        for (PWCGLocation location : bargePositions.getLocations())
        {
            if (location.getCountry(campaign.getDate()).getSide() == side)
            {
                bargePositionsForSide.add(location);
            }
        }
        return bargePositionsForSide;
    }
}
