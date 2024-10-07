# string-adapter
[![codebeat badge](https://codebeat.co/badges/998c8e12-ffdd-4196-b2a2-8979d7f1ee8a)](https://codebeat.co/projects/github-com-jcasbin-string-adapter-master)
[![build](https://github.com/jcasbin/string-adapter/actions/workflows/ci.yml/badge.svg)](https://github.com/jcasbin/string-adapter/actions)
[![codecov](https://codecov.io/github/jcasbin/string-adapter/branch/master/graph/badge.svg?token=4YRFEQY7VK)](https://codecov.io/github/jcasbin/string-adapter)
[![javadoc](https://javadoc.io/badge2/org.casbin/string-adapter/javadoc.svg)](https://javadoc.io/doc/org.casbin/string-adapter)
[![Maven Central](https://img.shields.io/maven-central/v/org.casbin/string-adapter.svg)](https://mvnrepository.com/artifact/org.casbin/string-adapter/latest)
[![Discord](https://img.shields.io/discord/1022748306096537660?logo=discord&label=discord&color=5865F2)](https://discord.gg/S5UjpzGZjN)

String Adapter is the string adapter for jCasbin, which provides interfaces for loading policies from a string and saving policies to it.

## Installation
```xml
<dependency>
    <groupId>org.casbin</groupId>
    <artifactId>string-adapter</artifactId>
    <version>1.0.0</version>
</dependency>
```


## Example
```java
import org.casbin.jcasbin.main.Enforcer;
import org.casbin.jcasbin.model.Model;

public class Example {
    public static void main(String[] args) {

        // Define the Casbin model
        String modelText = "[request_definition]\n" +
                "r = sub, obj, act\n\n" +
                "[policy_definition]\n" +
                "p = sub, obj, act\n\n" +
                "[role_definition]\n" +
                "g = _, _\n" +
                "g2 = _, _\n\n" +
                "[policy_effect]\n" +
                "e = some(where (p.eft == allow))\n\n" +
                "[matchers]\n" +
                "m = g(r.sub, p.sub) && g2(r.obj, p.obj) && r.act == p.act";

        // Load model
        Model m = new Model();
        m.loadModelFromText(modelText);

        // Define policy
        String line = "p, alice, data1, read\n" +
                "p, bob, data2, write\n" +
                "p, data_group_admin, data_group, write\n\n" +
                "g, alice, data_group_admin\n" +
                "g2, data1, data_group\n" +
                "g2, data2, data_group";

        // Create a StringAdapter
        StringAdapter sa = new StringAdapter(line);

        // Create Enforcer, and load model and policy
        Enforcer e = new Enforcer(m, sa);
        
        e.loadPolicy();
        // check permissions
        if (e.enforce("alice", "data1", "read")) {
            System.out.println("permitted");
        } else {
            System.out.println("rejected");
        }

        e.savePolicy();
    }
}
```

## Getting Help

- [jCasbin](https://github.com/casbin/jcasbin)

## License

This project is under Apache 2.0 License. See the [LICENSE](LICENSE) file for the full license text.