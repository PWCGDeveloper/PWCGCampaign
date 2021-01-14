package pwcg.campaign.utils;

import java.util.List;

import pwcg.campaign.plane.Role;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.target.TargetType;

public class TestDriver
{
    private static TestDriver testDriver = new TestDriver();

    private boolean enabled = false;
    private boolean createPlayerOnly = false;
    private boolean writeCampaignFile = true;
    private boolean debugAARLogs = false;

    private TestFlightType testFighterFlightType = new TestFlightType();
    private TestFlightType testReconFlightType = new TestFlightType();
    private TestFlightType testAttackFlightType = new TestFlightType();
    private TestFlightType testDiveBombFlightType = new TestFlightType();
    private TestFlightType testBombFlightType = new TestFlightType();
    private TestFlightType testStrategicBombFlightType = new TestFlightType();

    private TargetType testPlayerTacticalTargetType = TargetType.TARGET_TRANSPORT;
    private TargetType testAITacticalTargetType = TargetType.TARGET_INFANTRY;

    private List<SquadronMember> assignedSquadMembers = null;

    private TestDriver()
    {
        testFighterFlightType.playerFlightType = FlightTypes.OFFENSIVE;
        testFighterFlightType.aiFlightType = FlightTypes.PATROL;

        testReconFlightType.playerFlightType = FlightTypes.RECON;
        testReconFlightType.aiFlightType = FlightTypes.RECON;

        testAttackFlightType.playerFlightType = FlightTypes.GROUND_ATTACK;
        testAttackFlightType.aiFlightType = FlightTypes.GROUND_ATTACK;

        testDiveBombFlightType.playerFlightType = FlightTypes.DIVE_BOMB;
        testDiveBombFlightType.aiFlightType = FlightTypes.DIVE_BOMB;

        testBombFlightType.playerFlightType = FlightTypes.BOMB;
        testBombFlightType.aiFlightType = FlightTypes.BOMB;

        testStrategicBombFlightType.playerFlightType = FlightTypes.STRATEGIC_BOMB;
        testStrategicBombFlightType.aiFlightType = FlightTypes.STRATEGIC_BOMB;
    }

    public static TestDriver getInstance()
    {
        return testDriver;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public boolean isCreatePlayerOnly()
    {
        if (enabled)
        {
            return this.createPlayerOnly;
        }

        return false;
    }

    public boolean isWriteCampaignFile()
    {
        if (enabled)
        {
            return writeCampaignFile;
        }

        return false;
    }

    public boolean isDebugAARLogs()
    {
        return debugAARLogs;
    }

    public TestFlightType getTestFlightTypeForRole(Role role) throws PWCGException
    {
        if (role == Role.ROLE_FIGHTER)
        {
            return testFighterFlightType;
        }
        else if (role == Role.ROLE_BOMB)
        {
            return testBombFlightType;
        }
        else if (role == Role.ROLE_STRAT_BOMB)
        {
            return testStrategicBombFlightType;
        }
        else if (role == Role.ROLE_RECON)
        {
            return testReconFlightType;
        }
        else if (role == Role.ROLE_DIVE_BOMB)
        {
            return testDiveBombFlightType;
        }
        else if (role == Role.ROLE_ATTACK)
        {
            return testAttackFlightType;
        }
        else
        {
            throw new PWCGException("Invalid role for test flight: " + role);
        }
    }

    public TargetType getTestTacticalTargetType(boolean playerFlight) throws PWCGException
    {
        if (enabled)
        {
            if (playerFlight)
            {
                return this.testPlayerTacticalTargetType;
            }
            else
            {
                return this.testAITacticalTargetType;
            }
        }

        return TargetType.TARGET_NONE;
    }

    public List<SquadronMember> getAssignedSquadMembers()
    {
        return this.assignedSquadMembers;
    }

    public void setAssignedSquadMembers(List<SquadronMember> assignedSquadMembers)
    {
        this.assignedSquadMembers = assignedSquadMembers;
    }

    public class TestFlightType
    {
        public FlightTypes playerFlightType = FlightTypes.ANY;
        public FlightTypes aiFlightType = FlightTypes.ANY;
    }
}
