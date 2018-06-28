package pwcg.campaign.squadmember;

public class SerialNumber
{
    public static final int NO_SERIAL_NUMBER = 0;
    public static final int ACE_STARTING_SERIAL_NUMBER = 100000;
    public static final int PLAYER_SERIAL_NUMBER = 1000000;
    public static final int AI_STARTING_SERIAL_NUMBER = 3000000;
    
    private int nextSerialNumber = SerialNumber.AI_STARTING_SERIAL_NUMBER;

    public enum SerialNumberClassification
    {
        NONE,
        PLAYER,
        ACE,
        AI
    }

    public static SerialNumberClassification getSerialNumberClassification (int serialNumber)
    {
        if (serialNumber  == NO_SERIAL_NUMBER)
        {
            return SerialNumberClassification.NONE;
        }
        else if (serialNumber < PLAYER_SERIAL_NUMBER)
        {
            return SerialNumberClassification.ACE;
        }
        else if (serialNumber < AI_STARTING_SERIAL_NUMBER)
        {
            return SerialNumberClassification.PLAYER;
        }
        else
        {
            return SerialNumberClassification.AI;
        }
    }

    public int getNextSerialNumber()
    {
        ++nextSerialNumber;
        return nextSerialNumber;
    }

    public void setLastSerialNumber(int possibleNextSerialNumber)
    {
        if (possibleNextSerialNumber >= nextSerialNumber)
        {
            this.nextSerialNumber = possibleNextSerialNumber + 1;
        }
    }
}
