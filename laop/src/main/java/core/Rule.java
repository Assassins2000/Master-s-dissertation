package core;

/**
 * Класс служит для хранения правила в базе правил
 * Является потомком класса Fact
 * @author Balanovsky Danil
 */
public class Rule extends Fact {

    /**
     * Поле- хранит обозначение подсистемы, в которую входит компонент
     * Любой комопнент может быть подсистемой
     */
    private String  subsystem;

    /**
     * Поле- хранит тип функции объединения при каждом компоненте подсистемы
     *
     */
    private String ratio;

    /**
     * Конструктор- создание нового объекта
     * @param component обозначение компонента системы
     * @param subsystem обозначение подсистемы, в которую входит компонента
     * @param value значение убыли эффективности компонента системы
     * @param ratio тип объединения при каждом компоненте подсистемы
     */
    public Rule(String component, String subsystem, double value, String ratio) {
        super(component, value);//вызов конструктора предка
        this.subsystem = subsystem;
        this.ratio = ratio;
    }

    public String getSubsystem() {
        return subsystem;
    }

    public String getRatio() {
        return ratio;
    }
}
