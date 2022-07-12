package com.intuit.graphql.adapter.core.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Consumer;
import org.assertj.core.api.AssertionsForClassTypes;

@SuppressWarnings("ALL")
class TestAssertionUtils {

  static final Consumer ASSERT_REQUIREMENT_BIGINT = o -> {
    AssertionsForClassTypes.assertThat(o).isInstanceOf(BigInteger.class);
  };

  static final Consumer ASSERT_REQUIREMENT_BIGDECIMAL = o -> {
    AssertionsForClassTypes.assertThat(o).isInstanceOf(BigDecimal.class);
  };

  static final Consumer ASSERT_REQUIREMENT_STRING = o -> {
    AssertionsForClassTypes.assertThat(o).isInstanceOf(String.class);
  };

  static final Consumer ASSERT_REQUIREMENT_BOOLEAN = o -> {
    AssertionsForClassTypes.assertThat(o).isInstanceOf(Boolean.class);
  };
}
