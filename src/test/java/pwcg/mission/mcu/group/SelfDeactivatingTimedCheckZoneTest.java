package pwcg.mission.mcu.group;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.validate.IndexLinkValidator;
import pwcg.mission.mcu.group.virtual.VirtualWaypoint;

@RunWith(MockitoJUnitRunner.class)
public class SelfDeactivatingTimedCheckZoneTest
{
    @Test
    public void validateVwpBuildProcess() throws PWCGException
    {
        SelfDeactivatingTimedCheckZone checkZone = new SelfDeactivatingTimedCheckZone(
                VirtualWaypoint.VWP_TRIGGGER_DISTANCE, 
                1000,
                new Coordinate(), 
                5);
        
        checkZone.build();
        
        checkZone.addAdditionalTime(500);
        checkZone.setCheckZoneTimedOutTarget(99);
        checkZone.setCheckZoneTriggeredTarget(98);;
        checkZone.setCheckZoneTriggerObject(5555555);
        checkZone.setCheckZoneTriggerObject(6666666);

        assert(IndexLinkValidator.isIndexInTargetList(checkZone.getActivateCZTimer().getTargets(), checkZone.getCheckZone().getIndex()));
        assert(IndexLinkValidator.isIndexInTargetList(checkZone.getActivateCZTimer().getTargets(), checkZone.getWaitTimer().getIndex()));
        assert(IndexLinkValidator.isIndexInTargetList(checkZone.getCheckZone().getTargets(), checkZone.getCheckZoneTriggeredTimer().getIndex()));
        assert(IndexLinkValidator.isIndexInTargetList(checkZone.getCheckZoneTriggeredTimer().getTargets(), checkZone.getCheckZoneTriggeredExternalTimer().getIndex()));
        assert(IndexLinkValidator.isIndexInTargetList(checkZone.getCheckZoneTriggeredTimer().getTargets(), checkZone.getDeactivateCZTimer().getIndex()));

        assert(IndexLinkValidator.isIndexInTargetList(checkZone.getWaitTimer().getTargets(), checkZone.getWaitTriggeredTimer().getIndex()));
        assert(IndexLinkValidator.isIndexInTargetList(checkZone.getWaitTriggeredTimer().getTargets(), checkZone.getWaitTriggeredExternalTimer().getIndex()));
        
        assert(IndexLinkValidator.isIndexInTargetList(checkZone.getDeactivateCZTimer().getTargets(), checkZone.getDeactivateCZ().getIndex()));
        assert(IndexLinkValidator.isIndexInTargetList(checkZone.getDeactivateCZ().getTargets(), checkZone.getCheckZone().getIndex()));
        assert(IndexLinkValidator.isIndexInTargetList(checkZone.getDeactivateCZ().getTargets(), checkZone.getWaitTimer().getIndex()));
        
        assert(IndexLinkValidator.isIndexInTargetList(checkZone.getCheckZone().getObjects(), 5555555));
        assert(IndexLinkValidator.isIndexInTargetList(checkZone.getCheckZone().getObjects(), 6666666));
        assert(checkZone.getCheckZone().getZone() == VirtualWaypoint.VWP_TRIGGGER_DISTANCE);
        assert(checkZone.getWaitTimer().getTime() > 1499.0);
        assert(checkZone.getWaitTimer().getTime() < 1501.0);

    }
}
