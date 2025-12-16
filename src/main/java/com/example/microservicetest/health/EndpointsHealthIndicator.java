package com.example.microservicetest.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EndpointsHealthIndicator implements HealthIndicator {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    public EndpointsHealthIndicator(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    @Override
    public Health health() {
        List<Map<String, String>> endpointsList = new ArrayList<>();
        
        requestMappingHandlerMapping.getHandlerMethods().forEach((mapping, handlerMethod) -> {
            Set<String> patterns = mapping.getPatternValues();
            Set<String> methods = mapping.getMethodsCondition().getMethods().stream()
                .map(m -> m.name())
                .collect(Collectors.toSet());
            
            if (methods.isEmpty()) {
                methods.add("GET"); // Default if no method specified
            }
            
            String methodsStr = String.join(", ", methods);
            
            patterns.forEach(pattern -> {
                Map<String, String> endpointInfo = new HashMap<>();
                endpointInfo.put("path", pattern);
                endpointInfo.put("methods", methodsStr);
                endpointInfo.put("handler", handlerMethod.getBean().getClass().getSimpleName() + "." + handlerMethod.getMethod().getName());
                endpointsList.add(endpointInfo);
            });
        });
        
        Map<String, Object> details = new HashMap<>();
        details.put("status", "UP");
        details.put("availableEndpoints", endpointsList);
        details.put("totalEndpoints", endpointsList.size());
        
        return Health.up()
            .withDetails(details)
            .build();
    }
}

