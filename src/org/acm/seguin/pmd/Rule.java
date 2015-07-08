package org.acm.seguin.pmd;

import java.util.List;

public interface Rule {
    public static final int LOWEST_PRIORITY = 5;
    public static final String[] PRIORITIES = {"High", "Medium High", "Medium", "Medium Low", "Low"};

    String getName();

    String getMessage();

    String getDescription();

    String getExample();

    void setName(String name);

    void setMessage(String message);

    void setDescription(String description);

    void setExample(String example);

    void apply(List astCompilationUnits, RuleContext ctx);

    boolean hasProperty(String name);

    void addProperty(String name, String property);

    int getIntProperty(String name);

    boolean getBooleanProperty(String name);

    String getStringProperty(String name);

    double getDoubleProperty(String name);

    RuleProperties getProperties();

    boolean include();

    void setInclude(boolean include);

    int getPriority();

    String getPriorityName();

    void setPriority(int priority);
}
