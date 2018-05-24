import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class guiDK {

    DefaultTableModel rtb;
    Object[][]massRule;
    JTable table;
    JTextField component;
    JTextField subsystem;
    JTextField value;
    JTextField ratio;
    AbstractSystem as;
    ButtonGroup group;
    JRadioButton trueButton;
    JRadioButton falseButton;
    int keyRule;

    public guiDK(Object[][] massRule){

        if(massRule==null){
            throw new RuntimeException();
        }
        as= AbstractSystem.getInstance();

        JFrame jfrm = new JFrame("guiDK");
        jfrm.setLayout(new FlowLayout());
        jfrm.setSize(450, 400);

        ////////
        rtb = new DefaultTableModel(
                massRule,
                new String[]{
                        "Компонент", "Подсистема", "Значение", "Отношение", "Вклад", "ИД"
                }
        );
        table = new JTable(rtb);
        JScrollPane jscrlp1 = new JScrollPane(table);
        table.setPreferredScrollableViewportSize(new Dimension(400,200));

        jfrm.add(jscrlp1);

        JLabel jlb1= new JLabel("Компонент");
        JLabel jlb2= new JLabel("Подсистема");
        JLabel jlb3= new JLabel("Значение");
        JLabel jlb4= new JLabel("Отношение");
        JLabel jlb5= new JLabel("Положительный вклад");
        JLabel jlb6= new JLabel("Отрицательный вклад");

        component = new JTextField(10);
        component.setAlignmentX(1);
        value = new JTextField(10);
        subsystem= new JTextField(10);
        value= new JTextField(10);
        ratio = new JTextField(10);

        trueButton= new JRadioButton();
        falseButton= new JRadioButton();
        group= new ButtonGroup();

        group.add(trueButton);
        group.add(falseButton);


        JButton btn1= new JButton("Добавить правило");

        btn1.addActionListener(e -> {

            try {
                boolean flag=false;
                if (trueButton.isSelected()){
                    flag=true;
                }
                else if(falseButton.isSelected()){
                    flag=false;
                }
                keyRule= as.addRule(component.getText(), subsystem.getText(), Double.parseDouble(value.getText()), Integer.parseInt(ratio.getText()), flag, 0, 0);
                rtb.addRow(new String[]{component.getText(), subsystem.getText(), value.getText(), ratio.getText(), Boolean.toString(flag), Integer.toString(keyRule)});
            }
            catch (Exception exp)
            {
                JOptionPane.showMessageDialog(jfrm,"Правило не задано");
            }
        });

        JButton btn2 = new JButton("Удалить правило");
        btn2.addActionListener(e->{

           try {
                as.deleteRule(Integer.parseInt(table.getModel().getValueAt(table.getSelectedRow(),5).toString()));
                System.out.println(as.ruletoObj().length);
                rtb.removeRow(table.getSelectedRow());
           }
           catch (Exception exp)
           {
               JOptionPane.showMessageDialog(jfrm,"Правило не выбрано");
            }
        });



        jfrm.add(jlb1);
        jfrm.add(component);
        jfrm.add(jlb2);
        jfrm.add(subsystem);
        jfrm.add(jlb3);
        jfrm.add(value);
        jfrm.add(jlb4);
        jfrm.add(ratio);
        jfrm.add(jlb5);
        jfrm.add(trueButton);
        jfrm.add(jlb6);
        jfrm.add(falseButton);

        jfrm.add(btn1);
        jfrm.add(btn2);

        jfrm.setVisible(true);


    }
}
