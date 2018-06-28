package pwcg.campaign.skin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SkinsForPlaneOld
{
	private String planeType;
	private Map<String, Skin> configuredSkins = new TreeMap<String, Skin>();
	private Map<String, Skin> doNotUse = new TreeMap<String, Skin>();
	private Map<String, Skin> aceSkins = new TreeMap<String, Skin>();
	private Map<String, String> looseSkins = new TreeMap<String, String>();
	private List<String> skinsInUse = new ArrayList<String>();
	private List<Skin> squadronSkins = new ArrayList<Skin>();

	public SkinsForPlaneOld()
	{
	}

	public String getPlaneType()
	{
		return planeType;
	}

	public void setPlaneType(String planeTpe)
	{
		this.planeType = planeTpe;
	}

	public List<String> getSkinsInUse()
	{
		return skinsInUse;
	}

	public void setSkinsInUse(List<String> skinsInUse)
	{
		this.skinsInUse = skinsInUse;
	}

	public void setLooseSkins(Map<String, String> looseSkins)
	{
		this.looseSkins = looseSkins;
	}

	public void setConfiguredSkins(Map<String, Skin> configuredSkins)
	{
		this.configuredSkins = configuredSkins;
	}

	public void setDoNotUse(Map<String, Skin> doNotUse)
	{
		this.doNotUse = doNotUse;
	}

	public void setAceSkins(Map<String, Skin> aceSkins)
	{
		this.aceSkins = aceSkins;
	}

	public void setSquadronSkins(List<Skin> squadronSkins)
	{
		this.squadronSkins = squadronSkins;
	}

	public void addSquadronSkin(Skin skin)
	{
		skin.setCategory("Squadron");
		squadronSkins.add(skin);

		removeLooseSkin(skin.getSkinName());
	}

	public void addAceSkin(Skin skin)
	{
		skin.setCategory("Ace");
		aceSkins.put(skin.getSkinName(), skin);

		removeLooseSkin(skin.getSkinName());
	}

	public void addDoNotUseSkin(Skin skin)
	{
		skin.setCategory("Do Not Use");
		doNotUse.put(skin.getSkinName(), skin);

		removeLooseSkin(skin.getSkinName());
	}

	public void addConfiguredSkins(Skin skin)
	{
		skin.setCategory("Configured");
		configuredSkins.put(skin.getSkinName(), skin);

		removeLooseSkin(skin.getSkinName());
	}

	private void removeLooseSkin(String skinName)
	{
		// If it's a configured skin remove it from the loose skin set
		if (looseSkins.containsKey(skinName))
		{
			looseSkins.remove(skinName);
		}
	}

	/**
	 * Loose skins are skins that are present in the graphics directory but have
	 * no configuration associated with them. Loose skins should be converted to
	 * configured skins over time
	 */
	public void addLooseSkin(String looseSkin)
	{
		String skinNameUpper = looseSkin.toUpperCase();
		// If it's a configured skin then it's not a loose skin
		if (!configuredSkins.containsKey(skinNameUpper))
		{
			// If it's a squadron skin then it's not a loose skin
			if (!squadronSkins.contains(skinNameUpper))
			{
				// If it's a ace skin then it's not a loose skin
				if (!aceSkins.containsKey(skinNameUpper))
				{
					looseSkins.put(skinNameUpper, skinNameUpper);
				}
			}
		}

	}

	/**
	 * @param squadName
	 * @param planeId
	 * @return
	 */
	public List<Skin> filterSkinsInUse(List<Skin> skinSet)
	{
		List<Skin> filteredSkinSet = new ArrayList<Skin>();
		for (Skin skin : skinSet)
		{
			if (!isSkinInUse(skin))
			{
				filteredSkinSet.add(skin);
			}
		}

		return filteredSkinSet;
	}

	/**
	 * @param skin
	 */
	public void addSkinInUse(Skin skin)
	{
		skinsInUse.add(skin.getSkinName());
	}

	/**
	 * @param skin
	 * @return
	 */
	public boolean isSkinInUse(Skin skin)
	{
		return (skinsInUse.contains(skin.getSkinName()));
	}

	/**
	 * 
	 */
	public void clearSkinsInUse()
	{
		skinsInUse.clear();
	}

	public List<Skin> getAllUsedByPWCG()
	{
		List<Skin> allConfiguredSkins = new ArrayList<Skin>();
		allConfiguredSkins.addAll(squadronSkins);
		allConfiguredSkins.addAll(aceSkins.values());
		allConfiguredSkins.addAll(configuredSkins.values());

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

	/**
	 * Get a description of the category into which the skin falls
	 * 
	 * @param skinName
	 */
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

	public List<Skin> getDoNotUse()
	{
		return new ArrayList<Skin>(doNotUse.values());
	}

	public List<String> getLooseSkins()
	{
		return new ArrayList<String>(looseSkins.values());
	}

	public List<Skin> getConfiguredSkins()
	{
		return new ArrayList<Skin>(configuredSkins.values());
	}

	public List<Skin> getSquadronSkins()
	{
		return new ArrayList<Skin>(squadronSkins);
	}

	public List<Skin> getAceSkins()
	{
		return new ArrayList<Skin>(aceSkins.values());
	}

	public boolean isConfiguredSkin(String skinName)
	{
		return configuredSkins.containsKey(skinName);
	}

	public boolean isDoNotUse(String skinName)
	{
		return doNotUse.containsKey(skinName);
	}

	public boolean isAceUse(String skinName)
	{
		return aceSkins.containsKey(skinName);
	}
}
