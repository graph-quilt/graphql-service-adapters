package com.intuit.graphql.adapter.core;

/**
 * Base class for exceptions thrown in Service Adapter
 */
public class ServiceAdapterException extends RuntimeException {

  public ServiceAdapterException(String message, Throwable e) {
    super(message, e);
  }
}