package pwcg.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.skin.Skin;
import pwcg.campaign.skin.SkinsForPlane;
import pwcg.core.exception.PWCGException;

public class SkinAnalyzer 
{
    private Map<String, List<MissingSkin>> missingSkinListMap = new HashMap<String, List<MissingSkin>>();

	public void analyze() throws PWCGException 
	{
	    initMissingSkinMap();
	    
		// Do the analysis
		findMissingConfiguredSkins();
		findMissingSquadronSkins();
		findMissingAceSkins();
	}

	private void initMissingSkinMap() throws PWCGException
	{
        List<PlaneType> allPlanes = PWCGContext.getInstance().getPlaneTypeFactory().getAllPlanes();
        
        missingSkinListMap.clear();
        
        for (PlaneType plane : allPlanes)
        {
            List<MissingSkin> missingSkinList = new ArrayList<MissingSkin>();
            missingSkinListMap.put(plane.getType(), missingSkinList);
        }
	}

	private void findMissingConfiguredSkins() throws PWCGException 
	{
		for (PlaneType plane : PWCGContext.getInstance().getPlaneTypeFactory().getAllPlanes())
		{
			SkinsForPlane skinsForPlane = PWCGContext.getInstance().getSkinManager().getSkinsForPlane(plane.getType());
		
			for (Skin skin : skinsForPlane.getConfiguredSkins().getSkins().values())
			{
				if (!skin.skinExists(Skin.PRODUCT_SKIN_DIR))
				{		
				    addMissingSkin(plane, skin.getSkinName(), "Configured");
				}
			}
		}
	}
	
	private void findMissingSquadronSkins() throws PWCGException 
	{
		for (PlaneType plane : PWCGContext.getInstance().getPlaneTypeFactory().getAllPlanes())
		{
			SkinsForPlane skinsForPlane = PWCGContext.getInstance().getSkinManager().getSkinsForPlane(plane.getType());
		
			for (Skin skin : skinsForPlane.getSquadronSkins().getSkins().values())
			{
				if (!skin.skinExists(Skin.PRODUCT_SKIN_DIR))
				{		
                    addMissingSkin(plane, skin.getSkinName(), "Squadron");
				}
			}
		}
	}
	private void findMissingAceSkins() throws PWCGException 
	{
		for (PlaneType plane : PWCGContext.getInstance().getPlaneTypeFactory().getAllPlanes())
		{
			SkinsForPlane skinsForPlane = PWCGContext.getInstance().getSkinManager().getSkinsForPlane(plane.getType());
		
			for (Skin skin : skinsForPlane.getAceSkins().getSkins().values())
			{
				if (skin.getTemplate() == null && !skin.skinExists(Skin.PRODUCT_SKIN_DIR))
				{		
                    addMissingSkin(plane, skin.getSkinName(), "Ace");
				}
			}
		}
	}
    private void addMissingSkin(PlaneType plane, String skinName, String category)
    {
        MissingSkin missingSkin = new MissingSkin();
        
        missingSkin.setPlaneType(plane.getType());
        missingSkin.setSkinName(skinName);
        missingSkin.setCategory(category);
        
        List<MissingSkin> missingSkinList = missingSkinListMap.get(plane.getType());
        missingSkinList.add(missingSkin);
    }

    public List<MissingSkin> getMissingSkinsForPlane(String planeType)
    {
        List<MissingSkin> missingSkinList = missingSkinListMap.get(planeType);

        return missingSkinList;
    }
}
