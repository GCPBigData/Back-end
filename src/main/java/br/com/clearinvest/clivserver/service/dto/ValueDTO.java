package br.com.clearinvest.clivserver.service.dto;

import java.io.Serializable;

public class ValueDTO<T> implements Serializable {

    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
