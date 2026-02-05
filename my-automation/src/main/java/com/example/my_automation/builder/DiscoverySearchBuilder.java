package com.example.my_automation.builder;



import com.example.my_automation.dto.BaseRequestDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class DiscoverySearchBuilder {

    public static BaseRequestDto createSearchActorsDTO(List<Long> searchIds, int docs) {
        BaseRequestDto baseRequest = new BaseRequestDto();
        baseRequest.setFormat("minimal");
        baseRequest.setDocs(docs);
        baseRequest.setOffset(0);
        baseRequest.setHighlight(true);
        baseRequest.setFilters(Collections.emptyList());
        baseRequest.setSearchIDs(searchIds);
        return baseRequest;
    }

}
