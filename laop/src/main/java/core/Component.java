package core;


/**
 * Класс служит для хранения компонента системы
 * @author Balanovsky Danil
 */
public class Component {
    /**
     * Поле- обозначение компонента в системе
     */
    private String component;

    /**
     * Конструктор, создание нофого объекта
     * @param component- обозначение компоненты системы
     */
    public Component(String component) {
        this.component = component;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }
}
