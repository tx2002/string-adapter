package org.casbin.adapter;

import org.casbin.jcasbin.model.Model;
import org.casbin.jcasbin.persist.Adapter;
import org.casbin.jcasbin.persist.Helper;
import java.util.List;

/**
 * StringAdapter is the string adapter for jCasbin.
 * It can load policy from a string or save policy to it.
 */
public class StringAdapter implements Adapter {
    private String line;

    public StringAdapter(String line) {
        this.line = line;
    }

    @Override
    public void loadPolicy(Model model) {
        if (line == null || line.isEmpty()) {
            throw new IllegalArgumentException("Invalid line, line cannot be empty");
        }

        String[] lines = line.split("\n");
        for (String policyLine : lines) {
            if (policyLine == null || policyLine.trim().isEmpty()) {
                continue;
            }
            Helper.loadPolicyLine(policyLine, model);
        }
    }

    @Override
    public void savePolicy(Model model) {
        StringBuilder policyBuffer = new StringBuilder();

        // Save "p" (policy rules)
        model.model.get("p").forEach((ptype, ast) -> {
            ast.policy.forEach(rule -> {
                policyBuffer.append(ptype).append(", ");
                policyBuffer.append(String.join(", ", rule));
                policyBuffer.append("\n");
            });
        });

        // Save "g" (grouping policy rules)
        model.model.get("g").forEach((ptype, ast) -> {
            ast.policy.forEach(rule -> {
                policyBuffer.append(ptype).append(", ");
                policyBuffer.append(String.join(", ", rule));
                policyBuffer.append("\n");
            });
        });

        // Remove trailing newline character
        this.line = policyBuffer.toString().trim();
    }

    @Override
    public void addPolicy(String sec, String ptype, List<String> rule) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void removePolicy(String sec, String ptype, List<String> rule) {
        // clear the policy
        this.line = "";
    }

    @Override
    public void removeFilteredPolicy(String sec, String ptype, int fieldIndex, String... fieldValues) {
        throw new UnsupportedOperationException("not implemented");
    }

    // Getters and Setters for 'line'
    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }
}
