package pwcg.coop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.io.json.CoopUserIOJson;
import pwcg.coop.model.CoopUser;
import pwcg.core.exception.PWCGException;

public class CoopUserManager
{
    public static final String HOST_USER_NAME = "Host";
    public static final String DEFAULT_PWCG_HOST_PASSWORD = "PWCGHost";
    
    private static CoopUserManager instance = null;
    private  Map<String, CoopUser> coopUsers = new TreeMap<>();
    
    private CoopUserManager() {};
    
    public static CoopUserManager getIntance() throws PWCGException
    {
        if (instance == null)
        {
            instance = new CoopUserManager();
            instance.init();
        }
        return instance;
    }

    private void init() throws PWCGException
    {
        List<CoopUser> coopUsersList = CoopUserIOJson.readCoopUsers();
        for (CoopUser coopUser : coopUsersList)
        {
            coopUsers.put(coopUser.getUsername(), coopUser);
        }
    }
    
    public List<CoopUser> getAllCoopUsers() throws PWCGException
    {
        return new ArrayList<>(coopUsers.values());
    }
    

    public List<CoopUser> getCoopUsersExceptHost() throws PWCGException 
    {
        List<CoopUser> coopUsersExceptHost = new ArrayList<>(); 
        for (CoopUser coopUser : coopUsers.values())
        {
            if (!(coopUser.getUsername().equals(HOST_USER_NAME)))
            {
                coopUsersExceptHost.add(coopUser);
            }
        }
        return coopUsersExceptHost;
    }


    public void buildCoopUser(String username) throws PWCGException 
    {
        CoopUser addedCoopUser = new CoopUser();
        addedCoopUser.setUsername(username);
        addedCoopUser.setPassword("PWCG");
        addedCoopUser.setNote("Created by host");
        addedCoopUser.setApproved(true);
        
        coopUsers.put(username, addedCoopUser);
        writeUser(addedCoopUser);
    }

    public CoopUser getCoopHost() throws PWCGException 
    {
        CoopUser coopHost = getCoopUser(HOST_USER_NAME);
        if (coopHost == null)
        {
            coopHost = makeHostUser();
        }
        
        return coopHost;
    }

    public CoopUser getCoopUser(String username) throws PWCGException 
    {
        return coopUsers.get(username);
    }
    
    public void removeCoopUser(String username) throws PWCGException
    {
        CoopUser userToBeRemoved = getCoopUser(username);
        if (userToBeRemoved != null)
        {
            CoopPersonaManager.getIntance().removeCoopPersonasForUser(userToBeRemoved);;
            removeUserFile(userToBeRemoved);
            coopUsers.remove(username);
        }
    }

    private void removeUserFile(CoopUser userToBeRemoved) throws PWCGException
    {
        String coopUserDir = PWCGContext.getInstance().getDirectoryManager().getPwcgCoopDir() + "Users\\";                    
        File coopUserFile = new File(coopUserDir + userToBeRemoved.getUsername() + ".json");
        if (coopUserFile.exists())
        {
            coopUserFile.delete();
        }
    }

    public boolean isDuplicateUser(String username) throws PWCGException 
    {
        return coopUsers.containsKey(username);
    }
    
    public void setHostPassword(String password) throws PWCGException
    {
        CoopUser hostUser = getCoopHost();
        if ((password != null) && password.length() > 0)
        {
            hostUser.setPassword(password);
            writeUser(hostUser);
        }
    }
    
    public void setUserAcceptedStatus(List<String> acceptedUsers, List<String> rejectedUsers) throws PWCGException
    {
        for (String acceptedUsername : acceptedUsers)
        {
            CoopUser acceptedUser =  getCoopUser(acceptedUsername);
            if (acceptedUser != null)
            {
                acceptedUser.setApproved(true);
            }
        }
        
        for (String rejectedUsername : rejectedUsers)
        {
            CoopUser rejectedUser =  getCoopUser(rejectedUsername);
            if (rejectedUser != null)
            {
                rejectedUser.setApproved(false);
            }
        }
        
        writeAllUser();
    }

    private CoopUser makeHostUser() throws PWCGException
    {
        CoopUser hostUser = new CoopUser();
        hostUser.setUsername(HOST_USER_NAME);
        hostUser.setPassword(DEFAULT_PWCG_HOST_PASSWORD);
        hostUser.setApproved(true);
        hostUser.setNote("Campaign Host");
        
        coopUsers.put(HOST_USER_NAME, hostUser);
        writeUser(hostUser);

        return hostUser;
    }

    private void writeUser(CoopUser coopUser) throws PWCGException
    {
        CoopUserIOJson.writeJson(coopUser);
    }

    private void writeAllUser() throws PWCGException
    {
        CoopUserIOJson.writeJson(getAllCoopUsers());
    }
}
