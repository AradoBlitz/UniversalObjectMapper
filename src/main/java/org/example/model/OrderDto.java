package org.example.model;

public class OrderDto {

    private String id;
    private double sum;
    private UserDto userDto;

    public OrderDto(String id, double sum, UserDto userDto) {
        this.id = id;
        this.sum = sum;
        this.userDto = userDto;
    }

    public String getId() {
        return id;
    }

    public double getSum() {
        return sum;
    }

    public UserDto getUserDto() {
        return userDto;
    }
}
