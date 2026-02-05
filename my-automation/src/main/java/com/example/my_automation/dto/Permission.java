package com.example.my_automation.dto;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import java.util.List;

public class Permission implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<String> caseIds;
    private List<String> discoveryQueryIds;
    private List<String> poolIds;
    private List<String> monitoredDiscoveryQueryIds;

    public Permission() {
    }

    @JsonAlias({"caseIds", "case_ids"})
    public List<String> getCaseIds() {
        return this.caseIds;
    }

    @JsonAlias({"discoveryQueryIds", "discovery_query_ids"})
    public List<String> getDiscoveryQueryIds() {
        return this.discoveryQueryIds;
    }

    @JsonAlias({"poolIds", "pool_ids"})
    public List<String> getPoolIds() {
        return this.poolIds;
    }

    @JsonProperty("case_ids")
    public void setCaseIds(List<String> caseIds) {
        this.caseIds = caseIds;
    }

    @JsonProperty("discovery_query_ids")
    public void setDiscoveryQueryIds(List<String> discoveryQueryIds) {
        this.discoveryQueryIds = discoveryQueryIds;
    }

    @JsonProperty("pool_ids")
    public void setPoolIds(List<String> poolIds) {
        this.poolIds = poolIds;
    }

    @JsonAlias({"monitoredDiscoveryQueryIds", "monitored_discovery_query_ids"})
    public List<String> getMonitoredDiscoveryQueryIds() {
        return this.monitoredDiscoveryQueryIds;
    }

    @JsonProperty("monitored_discovery_query_ids")
    public void setMonitoredDiscoveryQueryIds(List<String> monitoredDiscoveryQueryIds) {
        this.monitoredDiscoveryQueryIds = monitoredDiscoveryQueryIds;
    }
}
