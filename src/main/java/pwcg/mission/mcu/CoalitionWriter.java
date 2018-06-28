package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class CoalitionWriter
{
	public static void writeGenericCoalition(BufferedWriter writer, List<Coalition> coalitions) throws IOException
	{
		writeCoalition(writer, coalitions, "Coalitions");
	}

	public static void writePlaneCoalition(BufferedWriter writer, List<Coalition> coalitions) throws IOException
	{
		writeCoalition(writer, coalitions, "PlaneCoalitions");
	}

	public static void writeVehicleCoalition(BufferedWriter writer, List<Coalition> coalitions) throws IOException
	{
		writeCoalition(writer, coalitions, "VehicleCoalitions");
	}

	private static void writeCoalition(BufferedWriter writer, List<Coalition> coalitions, String coalitionType) throws IOException
	{
	    if (coalitions.size() > 0)
	    {
	    	StringBuffer coalitionBuffer = new StringBuffer("");
	    	int i = 0;
	    	for (Coalition planeCoalition : coalitions)
	    	{
	    		String coalitionValue = new String("" + planeCoalition.getCoalitionValue());
	    		coalitionBuffer.append(coalitionValue);
	    		if (i < coalitions.size() - 1)
	    		{
	    			coalitionBuffer.append(", ");				
	    		}
	    		
	    		++i;
	    	}
	
	    	writer.write("  " + coalitionType + " = [" +  coalitionBuffer.toString() + "];");
	        writer.newLine();
	    }
	}
}
