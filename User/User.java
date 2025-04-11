package MyFitness.User;

/*Note this will be helpful when wanting to have a trainer look at users in the class
*  we can just make a list of users */
/*Note 2: have this as a super class so trainer and admin can have extra abilities when overloaded*/
public class User {
    /*Note can make a settings for example and then have a function that can be overloaded that makes the settings
    *  this can be implemented in each type of user/trainer/Admin*/
    String userName;
    String password;

    public User () {
        this.userName = "";
        this.password = "";
    }

    public User (String userName, String passWord) {
        this.userName = userName;
        this.password = passWord;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getUserName() {
        return userName;
    }
    public String getPassword() {
        return password;
    }
}
