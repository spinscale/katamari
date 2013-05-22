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

import static io.katamari.settings.types.ByteSizeUnit.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

/**
 * copied & slightly modified from elasticsearch
 * https://github.com/elasticsearch/elasticsearch/tree/master/src/test/java/org/elasticsearch/test/unit/common
 */
public class ByteSizeUnitTest {

  @Test
  public void testBytes() {
    assertThat(BYTES.toBytes(1), is(1L));
    assertThat(BYTES.toKB(1024), is(1L));
    assertThat(BYTES.toMB(1024 * 1024), is(1L));
    assertThat(BYTES.toGB(1024 * 1024 * 1024), is(1L));
  }

  @Test
  public void testKB() {
    assertThat(KB.toBytes(1), is(1024l));
    assertThat(KB.toKB(1), is(1l));
    assertThat(KB.toMB(1024), is(1l));
    assertThat(KB.toGB(1024 * 1024), is(1l));
  }

  @Test
  public void testMB() {
    assertThat(MB.toBytes(1), is(1024l * 1024));
    assertThat(MB.toKB(1), is(1024l));
    assertThat(MB.toMB(1), is(1l));
    assertThat(MB.toGB(1024), is(1l));
  }

  @Test
  public void testGB() {
    assertThat(GB.toBytes(1), is(1024l * 1024 * 1024));
    assertThat(GB.toKB(1), is(1024l * 1024));
    assertThat(GB.toMB(1), is(1024l));
    assertThat(GB.toGB(1), is(1l));
  }
}