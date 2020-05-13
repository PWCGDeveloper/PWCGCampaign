package pwcg.mission.mcu.group;

import pwcg.core.location.Coordinate;

public class MissionBeginInOutCheckZone extends MissionBeginCheckZoneBase
{
    public MissionBeginInOutCheckZone(String name, Coordinate position, int triggerZone)
    {
        super(position, new InOutCheckZone(name, position, triggerZone));
    }

    public void linkCheckZoneExitTarget(int outTarget)
    {
        InOutCheckZone inOut = (InOutCheckZone)checkZone;
        inOut.setOutTarget(outTarget);
    }
}
