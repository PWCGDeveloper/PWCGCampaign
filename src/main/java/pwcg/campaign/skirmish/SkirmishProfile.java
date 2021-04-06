package pwcg.campaign.skirmish;

import java.util.ArrayList;
import java.util.List;

public class SkirmishProfile
{
    private SkirmishProfileType profileType;
    private List<SkirmishProfileElement> skirmishProfileElements = new ArrayList<>();

    public SkirmishProfileType getProfileType()
    {
        return profileType;
    }

    public void setProfileType(SkirmishProfileType profileType)
    {
        this.profileType = profileType;
    }

    public List<SkirmishProfileElement> getSkirmishProfileElements()
    {
        return skirmishProfileElements;
    }

    public void setSkirmishProfileElements(List<SkirmishProfileElement> skirmishProfileElements)
    {
        this.skirmishProfileElements = skirmishProfileElements;
    }

}
