package MyFitness.User;

import MyFitness.ExerciseClass;

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
        /*Note for now I am adding this just so i can implement chase's code
        *  but below I could add this to the navbar only if its a trainer and
        *  call it edit classes so the trainer can add more classes or edit existing ones inside of that*/
        JButton editClass = new JButton("Edit Class");
        editClass.addActionListener(e -> {
            ExerciseClass exerciseClass = new ExerciseClass();
            exerciseClass.setVisible(true);

                });
        settings.add(editClass);
    }
}
