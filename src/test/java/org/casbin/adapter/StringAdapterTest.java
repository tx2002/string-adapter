// Copyright 2024 The string-adapter Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.casbin.adapter;

import org.casbin.jcasbin.main.Enforcer;
import org.casbin.jcasbin.model.Model;
import org.junit.Assert;
import org.junit.Test;

public class StringAdapterTest {

    @Test
    public void testKeyMatchRbac() {
        String conf = "[request_definition]\n" +
                "r = sub, obj, act\n\n" +
                "[policy_definition]\n" +
                "p = sub, obj, act\n\n" +
                "[role_definition]\n" +
                "g = _ , _\n\n" +
                "[policy_effect]\n" +
                "e = some(where (p.eft == allow))\n\n" +
                "[matchers]\n" +
                "m = g(r.sub, p.sub)  && keyMatch(r.obj, p.obj) && regexMatch(r.act, p.act)";

        String line = "p, alice, /alice_data/*, (GET)|(POST)\n" +
                "p, alice, /alice_data/resource1, POST\n" +
                "p, data_group_admin, /admin/*, POST\n" +
                "p, data_group_admin, /bob_data/*, POST\n" +
                "g, alice, data_group_admin";

        // Use StringAdapter to create an adapter
        StringAdapter sa = new StringAdapter(line);

        // create Model
        Model md = new Model();
        md.loadModelFromText(conf);

        // Create an Enforcer and load the model and policy
        Enforcer e = new Enforcer(md, sa);

        // Test the Enforce rules
        String sub = "alice";
        String obj = "/alice_data/resource1";
        String act = "POST";

        boolean result = e.enforce(sub, obj, act);

        Assert.assertTrue(result);
    }

    @Test
    public void testStringRbac() {
        String conf = "[request_definition]\n" +
                "r = sub, obj, act\n\n" +
                "[policy_definition]\n" +
                "p = sub, obj, act\n\n" +
                "[role_definition]\n" +
                "g = _ , _\n\n" +
                "[policy_effect]\n" +
                "e = some(where (p.eft == allow))\n\n" +
                "[matchers]\n" +
                "m = g(r.sub, p.sub) && r.obj == p.obj && r.act == p.act";

        String line = "p, alice, data1, read\n" +
                "p, data_group_admin, data3, read\n" +
                "p, data_group_admin, data3, write\n" +
                "g, alice, data_group_admin";

        // Use StringAdapter to create an adapter
        StringAdapter sa = new StringAdapter(line);

        // create Model
        Model md = new Model();
        md.loadModelFromText(conf);

        // Create an Enforcer and load the model and policy
        Enforcer e = new Enforcer(md, sa);

        // Test the Enforce rules
        String sub = "alice";
        String obj = "data1";
        String act = "read";

        boolean result = e.enforce(sub, obj, act);

        Assert.assertTrue(result);
    }
}
