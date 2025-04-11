package MyFitness.User;

public class Admin extends User {

    public Admin() {
        super();
    }
    public Admin(String username, String password) {
        super(username, password);
    }
    @Override
    public String getUserName() {
        return super.getUserName();
    }
    @Override
    public void setUserName(String userName) {
        super.setUserName(userName);
    }
    @Override
    public String getPassword() {
        return super.getPassword();
    }
    @Override
    public void setPassword(String password) {
        super.setPassword(password);
    }

}
