package com.flipkart.pibify;

import com.flipkart.pibify.codegen.log.SpecGenLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class is used for sending data from the third party processor to parent class.
 * Author bageshwar.pn
 * Date 31/10/24
 */
public class ThirdPartyProcessorResult {
    private Object data;
    private List<SpecGenLog> logs;

    public ThirdPartyProcessorResult() {
        logs = new ArrayList<>();
        data = null;
    }

    public Optional<Object> getData() {
        if (data == null) {
            return Optional.empty();
        } else {
            return Optional.of(data);
        }
    }

    public void setData(Object data) {
        this.data = data;
    }

    public List<SpecGenLog> getLogs() {
        return logs;
    }

    public void addLog(SpecGenLog log) {
        logs.add(log);
    }
}
