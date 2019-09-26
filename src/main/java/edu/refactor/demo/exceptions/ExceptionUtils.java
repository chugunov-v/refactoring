package edu.refactor.demo.exceptions;

public class ExceptionUtils {
    public static final RuntimeException NOT_FOUND_MONEY_EXCEPTION = new RuntimeException("There is no money in any of billing accounts");
    public static final RuntimeException NOT_FOUND_CUSTOMER_EXCEPTION = new RuntimeException("Customer does not exist");
    public static final RuntimeException NOT_FOUND_VEHICLE_EXCEPTION = new RuntimeException("Vehicle does not exist");
}