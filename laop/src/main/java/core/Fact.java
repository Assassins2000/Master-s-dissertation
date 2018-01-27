package core;
/**
 * Класс служит для хранения факта в базе фактов
 * Является потомком класса Component
 * @author Balanovsky Danil
 */
public class Fact extends Component {
    /**
     * Поле- хранит значение убыли эффективности компонента системы
     */
    private double value;

    /**
     * Конструктор- создание нового объекта
     * @param component обоначение компонента системы
     * @param value значение убыли эффективности компонента системы
     */
    public Fact(String component, double value) {
        super(component);//вызов конуструктора предка
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
