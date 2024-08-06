package Ecommerce_Online.Ecommerce.util;

public enum OrderStatus {

    IN_PROGRESS(1, "En Progreso"), ORDER_RECIVED(2, "Orden Recivida"), PRODUCT_PACKED(3, "Producto Empaquetado"),
    OUT_FOR_DELIVERY(4, "Fuera de Entrega"), DELIVERED(5, "Entregado"), CANCEL(6, "Cancelado"),SUCCESS(7,"Su Orden Correctamente");

    private Integer id;

    private String name;

    private OrderStatus(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
