import core.Fact;

import javax.swing.table.AbstractTableModel;
import java.util.Hashtable;

public class FactTableModel extends AbstractTableModel {

    Hashtable<Integer, Fact> facts;

    public FactTableModel( Hashtable<Integer, Fact> facts){
        this.facts= facts;
    }

    @Override
    public int getRowCount() {
        return facts.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        switch (columnIndex) {
            case 0:
                return facts.get(rowIndex + 1).getComponent();
            case 1:
                return facts.get(rowIndex + 1).getValue();
            default:
                return "";
        }

    }
    public String getColumnName(int c) {
        switch (c) {
            case 0:
                return "Компонент";
            case 1:
                return "Значение";

            default:
                return "Other";
        }
    }

}
