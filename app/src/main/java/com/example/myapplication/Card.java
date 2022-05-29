package com.example.myapplication;

public class Card {
    private String name;
    private String pin;

    public Card(String name, String pin) {
        this.name = name;
        this.pin = pin;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

