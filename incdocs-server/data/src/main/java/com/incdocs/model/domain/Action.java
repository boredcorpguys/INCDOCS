package com.incdocs.model.domain;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Action {
    private final int actionID;
    private String actionName;
    private String description;

    public Action(int actionID) {
        this.actionID = actionID;
    }

    public String getDescription() {
        return description;
    }

    public Action setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getActionID() {
        return actionID;
    }

    public String getActionName() {
        return actionName;
    }

    public Action setActionName(String actionName) {
        this.actionName = actionName;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Action)) return false;

        Action action = (Action) o;

        if (actionID != action.actionID) return false;
        return actionName.equals(actionName);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(actionID).toHashCode();
    }
}
