package pwcg.aar.inmission.phase2.logeval.pilotstatus;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.inmission.phase1.parse.event.AType2;
import pwcg.aar.inmission.phase1.parse.event.IAType2;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
public class AARPilotStatusWoundedEvaluatorTest
{
    public AARPilotStatusWoundedEvaluatorTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    /**
     * Pilot wound level is below wounded.
     */
    @Test
    public void testCrewMemberNotWounded () throws PWCGException
    {        
        List<IAType2> damageForBot = new ArrayList<>();
        for (int i = 0; i < 1; ++i)
        {
            IAType2 aType2 = new AType2("T:54991 AType:2 DMG:0.220 AID:-1 TID:36863 POS(112185.984,73.297,111706.273)");        
            damageForBot.add(aType2);
        }
        AARPilotStatusWoundedEvaluator aarPilotStatusWoundedEvaluator = new AARPilotStatusWoundedEvaluator();
        int woundLevel = aarPilotStatusWoundedEvaluator.getCrewMemberWoundedLevel(damageForBot);
        assert(woundLevel == SquadronMemberStatus.STATUS_ACTIVE);
    }
    
    /**
     * Pilot wound level is slightly above not wounded.
     */
    @Test
    public void testCrewMemberWoundedLowerBound () throws PWCGException
    {        
        List<IAType2> damageForBot = new ArrayList<>();
        for (int i = 0; i < 1; ++i)
        {
            IAType2 aType2 = new AType2("T:54991 AType:2 DMG:0.270 AID:-1 TID:36863 POS(112185.984,73.297,111706.273)");        
            damageForBot.add(aType2);
        }
        AARPilotStatusWoundedEvaluator aarPilotStatusWoundedEvaluator = new AARPilotStatusWoundedEvaluator();
        int woundLevel = aarPilotStatusWoundedEvaluator.getCrewMemberWoundedLevel(damageForBot);
        assert(woundLevel == SquadronMemberStatus.STATUS_WOUNDED);
    }
    
    
    /**
     * Pilot wound level is almost seriously wounded.
     */
    @Test
    public void testCrewMemberWoundedUpperBound () throws PWCGException
    {        
        List<IAType2> damageForBot = new ArrayList<>();
        for (int i = 0; i < 3; ++i)
        {
            IAType2 aType2 = new AType2("T:54991 AType:2 DMG:0.240 AID:-1 TID:36863 POS(112185.984,73.297,111706.273)");        
            damageForBot.add(aType2);
        }

        AARPilotStatusWoundedEvaluator aarPilotStatusWoundedEvaluator = new AARPilotStatusWoundedEvaluator();
        int woundLevel = aarPilotStatusWoundedEvaluator.getCrewMemberWoundedLevel(damageForBot);
        assert(woundLevel == SquadronMemberStatus.STATUS_WOUNDED);
    }
    
    /**
     * Pilot wound level is seriously wounded.
     */
    @Test
    public void testCrewMemberSeriouslyWounded () throws PWCGException
    {        
        List<IAType2> damageForBot = new ArrayList<>();
        for (int i = 0; i < 3; ++i)
        {
            IAType2 aType2 = new AType2("T:54991 AType:2 DMG:0.260 AID:-1 TID:36863 POS(112185.984,73.297,111706.273)");        
            damageForBot.add(aType2);
        }

        AARPilotStatusWoundedEvaluator aarPilotStatusWoundedEvaluator = new AARPilotStatusWoundedEvaluator();
        int woundLevel = aarPilotStatusWoundedEvaluator.getCrewMemberWoundedLevel(damageForBot);
        assert(woundLevel == SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED);
    }
}
