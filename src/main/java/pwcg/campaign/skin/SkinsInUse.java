package pwcg.campaign.skin;

import java.util.ArrayList;
import java.util.List;

public class SkinsInUse
{
    private List<String> skinsInUse = new ArrayList<String>();

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

    public List<String> getSkinsInUse()
    {
        return skinsInUse;
    }

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
}
