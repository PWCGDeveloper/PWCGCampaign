package pwcg.gui.campaign.skins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PlaneSorter;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.skin.Skin;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class PilotSkinInfo
{
    private SquadronMember pilot = null;
    private Map <String, Skin> planeSkinMap = new HashMap <String, Skin>();

	/**
	 * @param pilot
	 * @param squadron
	 */
	public PilotSkinInfo(SquadronMember pilot)
	{
	    this.pilot = pilot;
	}

    /**
     * @throws PWCGException 
     * 
     */
    public void initialize() throws PWCGException
    {
        Campaign campaign = PWCGContext.getInstance().getCampaign();
        SquadronMember referencePlayer = PWCGContext.getInstance().getReferencePlayer();
        Squadron squadron = referencePlayer.determineSquadron();
        
        // Make an entry for each plane initialized to "No Skin"
        List<PlaneType> squadronPlanes = squadron.determineCurrentAircraftList(campaign.getDate());
        List<PlaneType> squadronPlanesByBest = PlaneSorter.sortPlanesByGoodness(squadronPlanes);
        for (PlaneType plane : squadronPlanesByBest)
        {
            updateSkinForPlane(plane.getType(), null);
        }        
        
        // Now add the skins that are actually assigned
        Map <String, Skin> pilotSkinsByPlane  = pilot.determineSkinsByPlane();
        for (String planeName : pilotSkinsByPlane.keySet())
        {
            Skin skin = pilotSkinsByPlane.get(planeName);
            updateSkinForPlane(planeName, skin);
        }

    }
        
    /**
     * @param plane
     * @param skin
     */
    public void updateSkinForPlane(String plane, Skin skin)
    {
        planeSkinMap.put(plane, skin);
    }
    
    /**
     * @param plane
     * @return
     */
    public Skin getSkinForPlane(String plane)
    {
        return planeSkinMap.get(plane);
    }
    
    /**
     * @return
     */
    public Map <String, Skin> getAllSkins()
    {
        return planeSkinMap;
    }

    /**
     * @return the pilot
     */
    public SquadronMember getPilot()
    {
        return pilot;
    }

    /**
     * @param pilot the pilot to set
     */
    public void setPilot(SquadronMember pilot)
    {
        this.pilot = pilot;
    }

    public void setSkinAssignments()
    {
        List<Skin> skinsToSet = new ArrayList<Skin>();
        for (Skin skin : planeSkinMap.values())
        {
            if (skin != null)
            {
                skinsToSet.add(skin);
            }
        }
        
        pilot.setSkins(skinsToSet);
    }
    
    
}
