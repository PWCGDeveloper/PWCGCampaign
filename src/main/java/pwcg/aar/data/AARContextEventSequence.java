package pwcg.aar.data;

public class AARContextEventSequence
{
    private static int outOfMissionEventSequenceNumber = 100000;

    public static void reset()
    {
        outOfMissionEventSequenceNumber = 100000;
    }

    public static int getNextOutOfMissionEventSequenceNumber()
    {
        ++outOfMissionEventSequenceNumber;
        return outOfMissionEventSequenceNumber;
    }
}