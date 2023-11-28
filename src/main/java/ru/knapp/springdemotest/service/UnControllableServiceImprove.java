package ru.knapp.springdemotest.service;

public class UnControllableServiceImprove extends UnControllableServiceImpl implements  UnControllableService{
    @Override
    public String getSomeUnControllableValue() {
        String retVal = super.getSomeUnControllableValue();

        return "Я жидкий";
    }
}
