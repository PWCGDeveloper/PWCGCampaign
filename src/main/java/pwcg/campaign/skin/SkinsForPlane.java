package pwcg.campaign.skin;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;

public class SkinsForPlane
{
	private String planeType;
    private SkinSet configuredSkins = new SkinSet(SkinSetType.SKIN_CONFIGURED);
    private SkinSet doNotUse = new SkinSet(SkinSetType.SKIN_DO_NOT_USE);
    private SkinSet aceSkins = new SkinSet(SkinSetType.SKIN_ACE);
    private SkinSet looseSkins = new SkinSet(SkinSetType.SKIN_LOOSE);
    private SkinSet squadronSkins = new SkinSet(SkinSetType.SKIN_SQUADRON);


	public SkinsForPlane()
	{
	}

	public String getTankType()
	{
		return planeType;
	}

	public void setTankType(String planeTpe)
	{
		this.planeType = planeTpe;
	}

	public void addSquadronSkin(Skin skin)
	{
	    addSkin (squadronSkins, skin);
	}

	public void addAceSkin(Skin skin)
	{
        addSkin (aceSkins, skin);
	}

	public void addDoNotUseSkin(Skin skin)
	{
        addSkin (doNotUse, skin);
	}

	public void addConfiguredSkins(Skin skin)
	{
        addSkin (configuredSkins, skin);
	}

    public void addSkin(SkinSet skinSet, Skin skin)
    {
        skinSet.addSkin(skin);
    }

	public void addLooseSkin(String looseSkinName) throws PWCGException
	{
	    Skin skin = new Skin();
	    skin.setSkinName(looseSkinName);
	    
		// If it's a configured skin then it's not a loose skin
		if (!configuredSkins.hasSkin(skin))
		{
			// If it's a squadron skin then it's not a loose skin
			if (!squadronSkins.hasSkin(skin))
			{
				// If it's a ace skin then it's not a loose skin
				if (!aceSkins.hasSkin(skin))
				{
			        looseSkins.addSkin(skin);
				}
			}
		}

	}

	public List<Skin> getAllUsedByPWCG()
	{
		List<Skin> allConfiguredSkins = new ArrayList<Skin>();
		allConfiguredSkins.addAll(squadronSkins.getSkins().values());
		allConfiguredSkins.addAll(aceSkins.getSkins().values());
		allConfiguredSkins.addAll(configuredSkins.getSkins().values());

		return allConfiguredSkins;
	}

	public boolean isUsedByPWCG(String skinName)
	{
		boolean isConfiguerd = false;

		for (Skin skin : getAllUsedByPWCG())
		{
			if (skin.getSkinName().equalsIgnoreCase(skinName))
			{
				isConfiguerd = true;
				break;
			}
		}

		return isConfiguerd;
	}

	public String getSkinCategory(String skinName)
	{
		for (Skin skin : getAllUsedByPWCG())
		{
			if (skin.getSkinName().equalsIgnoreCase(skinName))
			{
				if (skin.getSquadId() > 0)
				{
					return "Squadron";
				}
				else
				{
					return "Generic";
				}
			}
		}

		return "Loose";
	}

	public boolean isConfiguredSkin(String skinName)
	{
		return configuredSkins.getSkins().containsKey(skinName);
	}

	public boolean isDoNotUse(String skinName)
	{
		return doNotUse.getSkins().containsKey(skinName);
	}

	public boolean isAceUse(String skinName)
	{
		return aceSkins.getSkins().containsKey(skinName);
	}

	public SkinSet getConfiguredSkins() 
	{
		return configuredSkins;
	}

	public SkinSet getDoNotUse() 
	{
		return doNotUse;
	}

	public SkinSet getAceSkins() 
	{
		return aceSkins;
	}

	public SkinSet getLooseSkins() 
	{
		return looseSkins;
	}

	public SkinSet getSquadronSkins() 
	{
		return squadronSkins;
	}

	public void setConfiguredSkins(SkinSet configuredSkins) 
	{
		this.configuredSkins = configuredSkins;
	}

	public void setDoNotUse(SkinSet doNotUse) 
	{
		this.doNotUse = doNotUse;
	}
	
	
}
