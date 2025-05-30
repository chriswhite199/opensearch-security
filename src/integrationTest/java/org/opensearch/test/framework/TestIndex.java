/*
* Copyright 2021-2022 floragunn GmbH
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
*/

/*
* SPDX-License-Identifier: Apache-2.0
*
* The OpenSearch Contributors require contributions made to
* this file be licensed under the Apache-2.0 license or a
* compatible open source license.
*
* Modifications Copyright OpenSearch Contributors. See
* GitHub history for details.
*/

package org.opensearch.test.framework;

import org.opensearch.action.admin.indices.create.CreateIndexRequest;
import org.opensearch.common.settings.Settings;
import org.opensearch.transport.client.Client;

public class TestIndex {

    private final String name;
    private final Settings settings;
    private final TestData testData;

    public TestIndex(String name, Settings settings, TestData testData) {
        this.name = name;
        this.settings = settings;
        this.testData = testData;
    }

    public void create(Client client) {
        if (testData != null) {
            testData.createIndex(client, name, settings);
        } else {
            client.admin().indices().create(new CreateIndexRequest(name).settings(settings)).actionGet();
        }
    }

    public String name() {
        return name;
    }

    public static Builder name(String name) {
        return new Builder().name(name);
    }

    public static class Builder {
        private String name;
        private Settings.Builder settings = Settings.builder();
        private TestData testData;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder setting(String name, int value) {
            settings.put(name, value);
            return this;
        }

        public Builder shards(int value) {
            settings.put("index.number_of_shards", 5);
            return this;
        }

        public Builder data(TestData testData) {
            this.testData = testData;
            return this;
        }

        public TestIndex build() {
            return new TestIndex(name, settings.build(), testData);
        }

    }

}
