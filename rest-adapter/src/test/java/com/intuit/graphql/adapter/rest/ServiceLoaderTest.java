package com.intuit.graphql.adapter.rest;

import static org.assertj.core.api.Assertions.assertThat;

import com.intuit.dsl.service.Service;
import org.junit.Test;

public class ServiceLoaderTest {

  @Test
  public void testCreationWithNull() {
    ServiceLoader loader = new ServiceLoader(AddBookServicePost.addBookDSL);
    Service addBooks = loader.getService("addBooks");
    Service addBooksWithBody = loader.getService("addBooksWithBody");
    Service addBooksNoKeyBody = loader.getService("addBooksNoKeyBody");
    Service addBooksPUT = loader.getService("addBooksPUT");
    assertThat(addBooks).isNotNull();
    assertThat(addBooksWithBody).isNotNull();
    assertThat(addBooksNoKeyBody).isNotNull();
    assertThat(addBooksPUT).isNotNull();
  }


}
