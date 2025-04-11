package MyFitness.User;

import javax.swing.*;

public class Trainer extends User {
    public Trainer() {
        super();
        SettingGUI();
    }
    public Trainer(String name, String password) {
        super(name, password);
        SettingGUI();
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

    public void SettingGUI(){
        settings.add(new JLabel("Trainer"));
    }
}
