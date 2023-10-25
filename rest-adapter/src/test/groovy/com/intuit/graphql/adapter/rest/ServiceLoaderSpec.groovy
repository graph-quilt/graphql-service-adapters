package com.intuit.graphql.adapter.rest

import com.intuit.dsl.service.Service
import spock.lang.Specification

class ServiceLoaderSpec extends Specification {

    def "can load service from dsl"() {
        given:
        ServiceLoader loader = new ServiceLoader(AddBookServicePostSpec.addBookDSL)
        when:
        Service addBooks = loader.getService("addBooks");
        Service addBooksWithBody = loader.getService("addBooksWithBody");
        Service addBooksNoKeyBody = loader.getService("addBooksNoKeyBody");
        Service addBooksPUT = loader.getService("addBooksPUT");
        then:
        assert addBooks != null
        assert addBooksWithBody != null
        assert addBooksNoKeyBody != null
        assert addBooksPUT != null
    }
}
