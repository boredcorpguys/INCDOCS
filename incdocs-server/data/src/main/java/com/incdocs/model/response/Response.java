package com.incdocs.model.response;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Response<T> {
    private final List<Metadata> metadata = new ArrayList<>();
    private final List<T> data = new ArrayList<>();

    public List<Metadata> getMetadata() {
        return metadata;
    }

    public List<T> getData() {
        return data;
    }

    public Response addMetadata(Metadata m) {
        if (m != null && StringUtils.isNotEmpty(m.colName) && StringUtils.isNotEmpty(m.colType))
            metadata.add(m);
        return this;
    }

    public Response addDataRow(T row) {
        data.add(row);
        return this;
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
