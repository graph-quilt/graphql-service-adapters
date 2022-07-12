package com.intuit.graphql.adapter.rest;

import com.intuit.dsl.service.Service;

import com.intuit.service.dsl.evaluator.utils.ServiceEvaluatorUtils;
import com.intuit.service.dsl.resource.loader.StringContentResourceLoader;
import org.eclipse.emf.ecore.resource.Resource;

public class ServiceLoader {

  private Resource resource;

  public ServiceLoader(String dslContent) {
    resource = new StringContentResourceLoader(dslContent).getResource();
  }

  public Service getService(String adapterName) {
    return ServiceEvaluatorUtils.getService(resource, adapterName);
  }

}
