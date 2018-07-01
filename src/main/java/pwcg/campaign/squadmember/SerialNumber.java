package pwcg.campaign.squadmember;

public class SerialNumber
{
    public static final int NO_SERIAL_NUMBER = 0;
    public static final int ACE_STARTING_SERIAL_NUMBER = 100000;
    public static final int PLAYER_SERIAL_NUMBER = 1000000;
    public static final int AI_STARTING_SERIAL_NUMBER = 3000000;
    public static final int PLANE_STARTING_SERIAL_NUMBER = 5000000;
    
    private int nextPilotSerialNumber = SerialNumber.AI_STARTING_SERIAL_NUMBER;
    private int nextPlaneSerialNumber = SerialNumber.PLANE_STARTING_SERIAL_NUMBER;

    public enum SerialNumberClassification
    {
        NONE,
        PLAYER,
        ACE,
        AI, 
        PLANE
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
        else if (serialNumber < PLANE_STARTING_SERIAL_NUMBER)
        {
            return SerialNumberClassification.AI;
        }
        else
        {
            return SerialNumberClassification.PLANE;
        }
    }

    public int getNextPilotSerialNumber()
    {
        ++nextPilotSerialNumber;
        return nextPilotSerialNumber;
    }

    public void setLastPilotSerialNumber(int possibleNextSerialNumber)
    {
        if (possibleNextSerialNumber >= nextPilotSerialNumber)
        {
            this.nextPilotSerialNumber = possibleNextSerialNumber + 1;
        }
    }

    public int getNextPlaneSerialNumber()
    {
        ++nextPlaneSerialNumber;
        return nextPlaneSerialNumber;
    }

    public void setLastPlaneSerialNumber(int possibleNextSerialNumber)
    {
        if (possibleNextSerialNumber >= nextPlaneSerialNumber)
        {
            this.nextPlaneSerialNumber = possibleNextSerialNumber + 1;
        }
    }
}
