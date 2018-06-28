package pwcg.campaign.skin;

public enum SkinSetType
{
    SKIN_CONFIGURED("Configured"),
    SKIN_SQUADRON("Squadron"),
    SKIN_ACE("Ace"),
    SKIN_USER_ASSIGNED("UserAssigned"),
    SKIN_LOOSE("Loose"),
    SKIN_DO_NOT_USE("DoNotUse");
    
    private String skinSetName;
    
    private SkinSetType(String skinSetName)
    {
        this.skinSetName = skinSetName;
    }

    public String getSkinSetName()
    {
        return skinSetName;
    }
}
