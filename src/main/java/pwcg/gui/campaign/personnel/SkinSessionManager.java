package pwcg.gui.campaign.personnel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.skin.Skin;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

/**
 * Manages state for a skin editing session.
 * During the edit process an overall view of skin assignment must be maintained in memory.
 * By maintaining state in memory a cancel allows the use to revert to the previous state.
 * Because assignment to one pilot impacts availability to another, assignments to all pilots must be maintained.
 * 
 * @author Patrick Wilson
 *
 */
public class SkinSessionManager
{
    private Campaign campaign;
    private SquadronMember pilot = null;
    private Map <Integer, PilotSkinInfo> pilotSkinSets = new HashMap <>();
    
    private boolean squadronSkinsSelected = false;
    private boolean nonSquadronSkinsSelected = false;
    private boolean looseSkinsSelected = false;

    public SkinSessionManager(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public List<Skin> getSquadronSkins(String selectedPlane) throws PWCGException
    {
        List<Skin> skinNames = new ArrayList<>();

        Squadron squad = pilot.determineSquadron();

        List<Skin> squadronSkins = PWCGContext.getInstance().getSkinManager().getSkinsBySquadronPlaneDate(selectedPlane, squad.getSquadronId(), campaign.getDate());
        skinNames = getConfiguredSkins(squadronSkins);

        return skinNames;
    }

    public List<Skin> getNonSquadronSkins(String selectedPlane) throws PWCGException
    {
        List<Skin> skinNames = new ArrayList<>();

        List<Skin> unaffiliatedSkins = PWCGContext.getInstance().getSkinManager().getSkinsBySquadronPlaneDate(selectedPlane, Skin.PERSONAL_SKIN, campaign.getDate());

        skinNames = getConfiguredSkins(unaffiliatedSkins);

        return skinNames;
    }

    public List<Skin> getLooseSkins(String selectedPlane) throws PWCGException
    {
        List<Skin> looseSkins = new ArrayList<>();

        // Get what we already have for the squadron or as nonSquadron
        List<Skin> squadronSkins = getSquadronSkins(selectedPlane);
        List<Skin> nonSquadronSkins = getNonSquadronSkins(selectedPlane);
        
        List<Skin> knownSkinsForPlane = new ArrayList<>();
        knownSkinsForPlane.addAll(squadronSkins);
        knownSkinsForPlane.addAll(nonSquadronSkins);

        // Exclude known squadron and nonSquadrons
        List<Skin> allLooseSkins = PWCGContext.getInstance().getSkinManager().getLooseSkinByPlane(selectedPlane);
        for (Skin looseSkin : allLooseSkins)
        {
            if (!isKnownSkin(knownSkinsForPlane, looseSkin.getSkinName()))
            {
                looseSkins.add(looseSkin);
            }
        }

        return looseSkins;
    }

    private boolean isKnownSkin(List<Skin> knownSkinsForPlane, String skinName)
    {
        for (Skin knownSkin : knownSkinsForPlane)
        {
            if (knownSkin.getSkinName().equals(skinName))
            {
                return true;
            }
        }
        return false;
    }

    private List<Skin> getConfiguredSkins(List<Skin> skins) throws PWCGException
    {
        List<Skin> skinNames = new ArrayList<>();

        Date campaignDate = campaign.getDate();

        for (Skin skin : skins)
        {
            if (!(campaignDate.before(skin.getStartDate())) && !(campaignDate.after(skin.getEndDate())))
            {
                if (!isSkinInUseByAnotherPilot(skin))
                {
                    if (skin.getSkinName() != null)
                    {
                        skinNames.add(skin);
                    }
                    else
                    {
                        PWCGLogger.log(LogLevel.DEBUG, "Null skin in list");
                    }
                }
            }
        }

        return skinNames;
    }

    private boolean isSkinInUseByAnotherPilot(Skin skinToCheck) throws PWCGException
    {
        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(pilot.getSquadronId());
        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        for (SquadronMember squadMember : squadronMembers.getSquadronMemberList())
        {
            if (!(squadMember.getSerialNumber() == pilot.getSerialNumber()))
            {
                List<Skin> skinsForPilot = squadMember.getSkins();
                for (Skin skin : skinsForPilot)
                {
                    if (skinToCheck.getSkinName().equalsIgnoreCase(skin.getSkinName()))
                    {
                        return true;
                    }
                }
            }
        }
        
        // Then look for skins assigned this session
        for (PilotSkinInfo pilotSkinSet: pilotSkinSets.values())
        {
            // Exclude skins assigned to other pilots during this session
            if (!(pilotSkinSet.getPilot().getSerialNumber() == pilot.getSerialNumber()))
            {
                for (Skin skinAssignedThisSession : pilotSkinSet.getAllSkins().values())
                {
                    if (skinAssignedThisSession != null)
                    {
                        if (skinAssignedThisSession.getSkinName().equalsIgnoreCase(skinToCheck.getSkinName()))
                        {
                            return true;
                        }
                    }
                }
            }
        }
        
        return false;
    }

    private void addSkinSetForPilot (SquadronMember pilot) throws PWCGException
    {
        if (!pilotSkinSets.containsKey(pilot.getSerialNumber()))
        {
            PilotSkinInfo pilotSkinSet = new PilotSkinInfo(campaign, pilot);
            pilotSkinSet.initialize();
            pilotSkinSets.put(pilot.getSerialNumber(), pilotSkinSet);
        }
    }

    public void finalizeSkinAssignments ()
    {
        for (PilotSkinInfo pilotSkinSet : pilotSkinSets.values())
        {
            if (pilotSkinSet != null)
            {
                pilotSkinSet.setSkinAssignments();
            }
        }
    }

    public SquadronMember getPilot()
    {
        return pilot;
    }

    public void setPilot(SquadronMember pilot) throws PWCGException
    {
        this.pilot = pilot;
        addSkinSetForPilot (pilot);
    }

    public Skin getSkinForPilotAndPlane(String planeName)
    {
        Skin skin = null;
        
        if (pilot != null)
        {
            PilotSkinInfo pilotSkinSet = pilotSkinSets.get(pilot.getSerialNumber());
            if (pilotSkinSet != null)
            {
                skin = pilotSkinSet.getSkinForPlane(planeName);
            }
        }
        
        return skin;
    }

     public void updateSkinForPlane(String plane, Skin skin)
     {
         if (pilot != null)
         {
             PilotSkinInfo pilotSkinSet = pilotSkinSets.get(pilot.getSerialNumber());
             if (pilotSkinSet != null)
             {
                 pilotSkinSet.updateSkinForPlane(plane, skin);
             }
         }
     }

     public PilotSkinInfo getPilotSkinSet()
     {
         return pilotSkinSets.get(pilot.getSerialNumber());
     }

    public boolean isSquadronSkinsSelected()
    {
        return squadronSkinsSelected;
    }

    public void setSquadronSkinsSelected(boolean squadronSkinsSelected)
    {
        this.squadronSkinsSelected = squadronSkinsSelected;
    }

    public boolean isNonSquadronSkinsSelected()
    {
        return nonSquadronSkinsSelected;
    }

    public void setNonSquadronSkinsSelected(boolean nonSquadronSkinsSelected)
    {
        this.nonSquadronSkinsSelected = nonSquadronSkinsSelected;
    }

    public boolean isLooseSkinsSelected()
    {
        return looseSkinsSelected;
    }

    public void setLooseSkinsSelected(boolean looseSkinsSelected)
    {
        this.looseSkinsSelected = looseSkinsSelected;
    }
    

    public void clearSkinCategorySelectedFlags()
    {
        this.looseSkinsSelected = false;
        this.squadronSkinsSelected = false;
        this.nonSquadronSkinsSelected = false;
    }

    
}
