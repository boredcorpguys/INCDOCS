package com.incdocs.model.response;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Response {
    private List<Metadata> metadata = new ArrayList<>();
    private Map<String, String> data = new HashMap<>();

    public List<Metadata> getMetadata() {
        return metadata;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void addMetadata(Metadata m) {
        if (m != null && StringUtils.isNotEmpty(m.colName) && StringUtils.isNotEmpty(m.colType))
            metadata.add(m);
    }

    public void addData(String colName, String colValue) {
        if (StringUtils.isNotEmpty(colName) && StringUtils.isNotEmpty(colValue))
            data.put(colName, colValue);
    }

    public static class Metadata {
        private String colDisplayName;
        private String colName;
        private String colType;
        private Boolean visibility;

        public String getColName() {
            return colName;
        }

        public Metadata setColName(String colName) {
            this.colName = colName;
            return this;
        }

        public String getColDisplayName() {
            return colDisplayName;
        }

        public Metadata setColDisplayName(String colDisplayName) {
            this.colDisplayName = colDisplayName;
            return this;
        }

        public String getColType() {
            return colType;
        }

        public Metadata setColType(String colType) {
            this.colType = colType;
            return this;
        }

        public Boolean getVisibility() {
            return visibility;
        }

        public Metadata setVisibility(Boolean visibility) {
            this.visibility = visibility;
            return this;
        }
    }
}
