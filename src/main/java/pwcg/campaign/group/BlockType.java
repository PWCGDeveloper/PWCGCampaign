package pwcg.campaign.group;

public enum BlockType {
    AIRFIELD(true),
    BATTLEFIELD(true),
    BRIDGE(true),
    CIVILIAN(false),
    INDUSTRIAL(true),
    RAILWAY(true),
    SCENERY(false),
    STATIC_PLANE(true),
    STATIC_VEHICLE(true);

    private boolean isTarget;

    private BlockType (boolean isTarget)
    {
        this.isTarget = isTarget;
    }

    public boolean getIsTarget() {
        return isTarget;
    }
}
