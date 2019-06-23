package pwcg.campaign.skin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SkinsForPlaneOld
{
	private Map<String, Skin> configuredSkins = new TreeMap<>();
	private Map<String, Skin> doNotUse = new TreeMap<>();
	private Map<String, Skin> aceSkins = new TreeMap<>();
	private Map<String, String> looseSkins = new TreeMap<>();
    private Map<String, Skin> squadronSkins = new TreeMap<>();
	private List<String> skinsInUse = new ArrayList<String>();

	public SkinsForPlaneOld()
	{
	}

	public void addSquadronSkin(Skin skin)
	{
		skin.setCategory("Squadron");
		squadronSkins.put(skin.getSkinName(), skin);

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

	public void addLooseSkin(String looseSkin)
	{
		String skinNameUpper = looseSkin.toUpperCase();
		// If it's a configured skin then it's not a loose skin
		if (!configuredSkins.containsKey(skinNameUpper))
		{
			// If it's a squadron skin then it's not a loose skin
			if (!squadronSkins.containsKey(skinNameUpper))
			{
				// If it's a ace skin then it's not a loose skin
				if (!aceSkins.containsKey(skinNameUpper))
				{
					looseSkins.put(skinNameUpper, skinNameUpper);
				}
			}
		}

	}

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

	public boolean isSkinInUse(Skin skin)
	{
		return (skinsInUse.contains(skin.getSkinName()));
	}

	public void clearSkinsInUse()
	{
		skinsInUse.clear();
	}

	public List<Skin> getAllUsedByPWCG()
	{
		List<Skin> allConfiguredSkins = new ArrayList<Skin>();
		allConfiguredSkins.addAll(squadronSkins.values());
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
}
