package pwcg.coop.model;

public class CoopUser
{
    private String username;
    private String password;
    private boolean approved;
    private String note;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public boolean isApproved()
    {
        return approved;
    }

    public void setApproved(boolean approved)
    {
        this.approved = approved;
    }

    public String getNote()
    {
        return note;
    }

    public void setNote(String note)
    {
        this.note = note;
    }
}
