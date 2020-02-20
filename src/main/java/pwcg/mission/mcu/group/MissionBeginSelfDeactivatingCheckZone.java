package pwcg.mission.mcu.group;

import pwcg.core.location.Coordinate;

public class MissionBeginSelfDeactivatingCheckZone extends MissionBeginCheckZoneBase
{
    public MissionBeginSelfDeactivatingCheckZone(String name, Coordinate position, int triggerZone)
    {
        super(position, new SelfDeactivatingCheckZone(name, position, triggerZone));
    }
}
