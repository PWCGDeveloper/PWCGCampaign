package pwcg.gui.campaign.crewmember;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.CrewMemberVictories;
import pwcg.campaign.crewmember.Victory;
import pwcg.campaign.crewmember.VictoryDescription;
import pwcg.core.exception.PWCGException;

public class CrewMemberLogPages
{
    private CrewMember crewMember;
    private PageSizeCalculator pageSizeCalculator = new PageSizeCalculator();
    private Map<Integer, StringBuffer> pages = new TreeMap<Integer, StringBuffer>();
    private Campaign campaign;

    public CrewMemberLogPages(Campaign campaign, CrewMember crewMember)
    {
        this.campaign = campaign;
        this.crewMember = crewMember;
    }

    public void makePages() throws PWCGException
    {
        makePageOne();
        
        int pageCount = 2;
        CrewMemberVictories squadronMemberVictories = crewMember.getCrewMemberVictories();
        if (squadronMemberVictories.getAirToAirVictoryCount() > 0)
        {
            pageCount = addVictoriesToPage(pageCount, squadronMemberVictories.getAirToAirVictories());
        }

        if (squadronMemberVictories.getTankVictoryCount() > 0)
        {
            pageCount = addVictoriesToPage(pageCount, squadronMemberVictories.getTankVictories());
        }

        if (squadronMemberVictories.getTrainVictoryCount() > 0)
        {
            pageCount = addVictoriesToPage(pageCount, squadronMemberVictories.getTrainVictories());
        }

        if (squadronMemberVictories.getGroundVictoryCount() > 0)
        {
            pageCount = addVictoriesToPage(pageCount, squadronMemberVictories.getGroundVictories());
        }
        
        return;
    }

    private void makePageOne() throws PWCGException
    {
        StringBuffer pageOneBuffer = new StringBuffer("CrewMember Dossier:\n");
        
        String crewMemberName = crewMember.getNameAndRank() + "\n";
        pageOneBuffer.append("\n" + crewMemberName);

        String crewMemberStatus = "CrewMember Status: " + CrewMemberStatus.crewMemberStatusToStatusDescription(crewMember.getCrewMemberActiveStatus()) + "\n";
        pageOneBuffer.append(crewMemberStatus);

        pageOneBuffer.append(crewMember.skillAsString() + "\n");

        CrewMemberVictories squadronMemberVictories = crewMember.getCrewMemberVictories();
        
        String crewMemberAirVictories = "Air Victories: " + squadronMemberVictories.getAirToAirVictoryCount() + "\n";
        pageOneBuffer.append(crewMemberAirVictories);

        String crewMemberTankVictories = "Tank Victories: " + squadronMemberVictories.getTankVictoryCount() + "\n";
        pageOneBuffer.append(crewMemberTankVictories);

        String crewMemberTraniVictories = "Train Victories: " + squadronMemberVictories.getTrainVictoryCount() + "\n";
        pageOneBuffer.append(crewMemberTraniVictories);        
        
        String crewMemberGroundVictories = "Ground Victories: " + squadronMemberVictories.getGroundVictoryCount() + "\n";
        pageOneBuffer.append(crewMemberGroundVictories);
        pageOneBuffer.append("\n");

        for (String playerAircraftName : squadronMemberVictories.getAirVictoriesdInType().keySet())
        {
            List<Victory> airVictoriesdInType = squadronMemberVictories.getAirVictoriesdInType().get(playerAircraftName);
            String victoriesInType = "Air Victories in " + playerAircraftName + ": " + airVictoriesdInType.size() + "\n";
            pageOneBuffer.append(victoriesInType);
        }
        pageOneBuffer.append("\n");
        
        for (String playerAircraftName : squadronMemberVictories.getGroundVictoriesdInType().keySet())
        {
            List<Victory> groundVictoriesdInType = squadronMemberVictories.getGroundVictoriesdInType().get(playerAircraftName);
            String victoriesInType = "Ground Victories in " + playerAircraftName + ": " + groundVictoriesdInType.size() + "\n";
            pageOneBuffer.append(victoriesInType);
        }
        pageOneBuffer.append("\n");
        
        for (String victimAircraftName : squadronMemberVictories.getAirVictoriesType().keySet())
        {
            List<Victory> groundVictoriesdOverType = squadronMemberVictories.getAirVictoriesType().get(victimAircraftName);
            String victoriesInType = "Air Victories over " + victimAircraftName + ": " + groundVictoriesdOverType.size() + "\n";
            pageOneBuffer.append(victoriesInType);
        }
        pageOneBuffer.append("\n");
        
        for (String victimTankName : squadronMemberVictories.getTankVictoriesType().keySet())
        {
            List<Victory> airVictoriesdOverType = squadronMemberVictories.getTankVictoriesType().get(victimTankName);
            String victoriesInType = "Air Victories over " + victimTankName + ": " + airVictoriesdOverType.size() + "\n";
            pageOneBuffer.append(victoriesInType);
        }
        pageOneBuffer.append("\n");

        pages.put(1, pageOneBuffer);
    }
    
    private int addVictoriesToPage(int pageCount, List<Victory> victories) throws PWCGException
    {
        StringBuffer page = new StringBuffer("");

        for (Victory victory : victories)
        {
            VictoryDescription victoryDescription = new VictoryDescription(campaign, victory);
            String victoryDescriptionText = victoryDescription.createVictoryDescription();
            String logEntry = victoryDescriptionText  + "\n\n";

            if ( (countLines(page.toString()) + countLines(logEntry)) >= pageSizeCalculator.getLinesPerPage())
            {
                pages.put(pageCount, page);
                ++pageCount;
                page = new StringBuffer("");
            }

            page.append(logEntry);
        }
        
        pages.put(pageCount, page);
        
        ++pageCount;
        return pageCount;
    }

    private int countLines(String str)
    {
        String[] lines = str.split("\r\n|\r|\n");
        int calculatedLines = lines.length;
        
        for (String line : lines)
        {
            calculatedLines += calculateLinesNeededForThisString(line);
        }
        
        
        return  calculatedLines;
    }

    private int calculateLinesNeededForThisString(String line)
    {
        int extraLines = 0;
        if (line.length() > pageSizeCalculator.getCharsPerLine())
        {
            ++extraLines;
        }
        return extraLines;
    }

    public int getPageCount()
    {
        return pages.size();
    }

    public StringBuffer getPage(int pageNum)
    {
        return pages.get(pageNum);
    }

    
}
