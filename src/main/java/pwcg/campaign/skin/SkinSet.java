package pwcg.campaign.skin;

import java.util.LinkedHashMap;
import java.util.Map;

public class SkinSet
{
    private SkinSetType skinSetType;
    private Map<String, Skin> skins = new LinkedHashMap<>();

    public SkinSet(SkinSetType skinSetType)
    {
        this.skinSetType = skinSetType;
    }

    public SkinSetType getSkinSetType()
    {
        return skinSetType;
    }

    public void setSkinSetType(SkinSetType skinSetType)
    {
        this.skinSetType = skinSetType;
    }
    
    public void addSkin(Skin skin)
    {
        skins.put(skin.getSkinName().toLowerCase(), skin);
    }
    
    public boolean hasSkin(Skin skin)
    {
        return skins.containsKey(skin.getSkinName().toLowerCase());
    }

    public void removeSkin(Skin skin)
    {
        skins.remove(skin.getSkinName());
    }

    public Map<String, Skin> getSkins()
    {
        return skins;
    }
}
