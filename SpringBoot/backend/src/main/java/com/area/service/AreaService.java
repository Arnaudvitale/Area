package com.area.service;

import java.util.Map;

public interface AreaService {
    String getName();
    Object execute(String action, Map<String, String> params);
}
