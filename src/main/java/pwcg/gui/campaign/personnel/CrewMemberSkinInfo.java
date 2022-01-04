package pwcg.gui.campaign.personnel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.skin.Skin;
import pwcg.campaign.tank.TankSorter;
import pwcg.campaign.tank.TankType;
import pwcg.core.exception.PWCGException;

public class CrewMemberSkinInfo
{
    private CrewMember crewMember = null;
    private Map <String, Skin> planeSkinMap = new HashMap <String, Skin>();

	/**
	 * @param crewMember
	 * @param squadron
	 */
	public CrewMemberSkinInfo(CrewMember crewMember)
	{
	    this.crewMember = crewMember;
	}

    /**
     * @throws PWCGException 
     * 
     */
    public void initialize() throws PWCGException
    {
        Campaign campaign = PWCGContext.getInstance().getCampaign();
        CrewMember referencePlayer = campaign.findReferencePlayer();
        Company squadron = referencePlayer.determineSquadron();
        
        // Make an entry for each plane initialized to "No Skin"
        List<TankType> squadronPlanes = squadron.determineCurrentAircraftList(campaign.getDate());
        List<TankType> squadronPlanesByBest = TankSorter.sortTanksByGoodness(squadronPlanes);
        for (TankType plane : squadronPlanesByBest)
        {
            updateSkinForPlane(plane.getType(), null);
        }        
        
        // Now add the skins that are actually assigned
        Map <String, Skin> crewMemberSkinsByPlane  = crewMember.determineSkinsByPlane();
        for (String planeName : crewMemberSkinsByPlane.keySet())
        {
            Skin skin = crewMemberSkinsByPlane.get(planeName);
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
     * @return the crewMember
     */
    public CrewMember getCrewMember()
    {
        return crewMember;
    }

    /**
     * @param crewMember the crewMember to set
     */
    public void setCrewMember(CrewMember crewMember)
    {
        this.crewMember = crewMember;
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
        
        crewMember.setSkins(skinsToSet);
    }
    
    
}
