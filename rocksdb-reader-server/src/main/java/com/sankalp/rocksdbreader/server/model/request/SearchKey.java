package com.sankalp.rocksdbreader.server.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchKey {
    private String columnFamilyName;
    private String key;
}
