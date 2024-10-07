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

        // 使用 StringAdapter 来创建适配器
        StringAdapter sa = new StringAdapter(line);

        // 创建 Casbin Model
        Model md = new Model();
        md.loadModelFromText(conf);

        // 创建 Enforcer 并加载模型和策略
        Enforcer e = new Enforcer(md, sa);

        // 测试 Enforce 规则
        String sub = "alice"; // 用户
        String obj = "/alice_data/resource1"; // 资源
        String act = "POST"; // 操作

        boolean result = e.enforce(sub, obj, act);

        // 断言结果应该为 true
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

        // 使用 StringAdapter 来创建适配器
        StringAdapter sa = new StringAdapter(line);

        // 创建 Casbin Model
        Model md = new Model();
        md.loadModelFromText(conf);

        // 创建 Enforcer 并加载模型和策略
        Enforcer e = new Enforcer(md, sa);

        // 测试 Enforce 规则
        String sub = "alice"; // 用户
        String obj = "data1"; // 资源
        String act = "read";  // 操作

        boolean result = e.enforce(sub, obj, act);

        // 断言结果应该为 true
        Assert.assertTrue(result);
    }
}
