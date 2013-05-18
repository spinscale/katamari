/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.katamari.settings.types;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;

/**
 * copied & slightly modified from elasticsearch
 * https://github.com/elasticsearch/elasticsearch/tree/master/src/test/java/org/elasticsearch/test/unit/common
 */
public class TimeValueTest {

  @Test
  public void testSimple() {
    assertThat(TimeUnit.MILLISECONDS.toMillis(10), is(new TimeValue(10, TimeUnit.MILLISECONDS).millis()));
    assertThat(TimeUnit.MICROSECONDS.toMicros(10), is(new TimeValue(10, TimeUnit.MICROSECONDS).micros()));
    assertThat(TimeUnit.SECONDS.toSeconds(10), is(new TimeValue(10, TimeUnit.SECONDS).seconds()));
    assertThat(TimeUnit.MINUTES.toMinutes(10), is(new TimeValue(10, TimeUnit.MINUTES).minutes()));
    assertThat(TimeUnit.HOURS.toHours(10), is(new TimeValue(10, TimeUnit.HOURS).hours()));
    assertThat(TimeUnit.DAYS.toDays(10), is(new TimeValue(10, TimeUnit.DAYS).days()));
  }

  @Test
  public void testToString() {
    assertThat("10ms", is(new TimeValue(10, TimeUnit.MILLISECONDS).toString()));
    assertThat("1.5s", is(new TimeValue(1533, TimeUnit.MILLISECONDS).toString()));
    assertThat("1.5m", is(new TimeValue(90, TimeUnit.SECONDS).toString()));
    assertThat("1.5h", is(new TimeValue(90, TimeUnit.MINUTES).toString()));
    assertThat("1.5d", is(new TimeValue(36, TimeUnit.HOURS).toString()));
    assertThat("1000d", is(new TimeValue(1000, TimeUnit.DAYS).toString()));
  }

  @Test
  public void testMinusOne() {
    assertThat(new TimeValue(-1).nanos(), lessThan(0l));
  }
  
}
