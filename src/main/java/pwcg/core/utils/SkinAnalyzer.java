package pwcg.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.skin.Skin;
import pwcg.campaign.skin.SkinsForPlane;
import pwcg.campaign.tank.TankType;
import pwcg.core.exception.PWCGException;

public class SkinAnalyzer 
{
    private Map<String, List<MissingSkin>> missingSkinListMap = new HashMap<String, List<MissingSkin>>();

	public void analyze() throws PWCGException 
	{
	    initMissingSkinMap();
	    
		findMissingConfiguredSkins();
		findMissingSquadronSkins();
		findMissingAceSkins();
	}

	private void initMissingSkinMap() throws PWCGException
	{
        List<TankType> allPlanes = PWCGContext.getInstance().getTankTypeFactory().getAllPlanes();
        
        missingSkinListMap.clear();
        
        for (TankType plane : allPlanes)
        {
            List<MissingSkin> missingSkinList = new ArrayList<>();
            missingSkinListMap.put(plane.getType(), missingSkinList);
        }
	}

	private void findMissingConfiguredSkins() throws PWCGException 
	{
		for (TankType plane : PWCGContext.getInstance().getTankTypeFactory().getAllPlanes())
		{
			SkinsForPlane skinsForPlane = PWCGContext.getInstance().getSkinManager().getSkinsForPlane(plane.getType());
		
			for (Skin skin : skinsForPlane.getConfiguredSkins().getSkins().values())
			{
		        if (isSkinMissing(skin))
		        {
	                addMissingSkin(plane, skin.getSkinName(), "Configured");
		        }
			}
		}
	}
	
	private void findMissingSquadronSkins() throws PWCGException 
	{
		for (TankType plane : PWCGContext.getInstance().getTankTypeFactory().getAllPlanes())
		{
			SkinsForPlane skinsForPlane = PWCGContext.getInstance().getSkinManager().getSkinsForPlane(plane.getType());
		
			for (Skin skin : skinsForPlane.getSquadronSkins().getSkins().values())
			{
                if (isSkinMissing(skin))
				{		
                    addMissingSkin(plane, skin.getSkinName(), "Squadron");
				}
			}
		}
	}
	private void findMissingAceSkins() throws PWCGException 
	{
		for (TankType plane : PWCGContext.getInstance().getTankTypeFactory().getAllPlanes())
		{
			SkinsForPlane skinsForPlane = PWCGContext.getInstance().getSkinManager().getSkinsForPlane(plane.getType());
		
			for (Skin skin : skinsForPlane.getAceSkins().getSkins().values())
			{
                if (isSkinMissing(skin))
				{		
                    addMissingSkin(plane, skin.getSkinName(), "Ace");
				}
			}
		}
	}

    private boolean isSkinMissing(Skin skin)
    {
        if (!skin.isDefinedInGame())
        {
            if (!skin.skinExists(Skin.PRODUCT_SKIN_DIR))
            {
                return true;
            }
        }
        
        return false;
    }
    
    private void addMissingSkin(TankType plane, String skinName, String category)
    {
        MissingSkin missingSkin = new MissingSkin();
        
        missingSkin.setTankType(plane.getType());
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
