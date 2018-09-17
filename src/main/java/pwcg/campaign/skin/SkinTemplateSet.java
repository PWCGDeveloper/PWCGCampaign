package pwcg.campaign.skin;

import java.util.Map;
import java.util.TreeMap;

public class SkinTemplateSet {
    private Map<String, SkinTemplate> templates = new TreeMap<String, SkinTemplate>();

    public Map<String, SkinTemplate> getTemplates() {
        return templates;
    }
}
