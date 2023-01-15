package com.karimbkb.customerreview.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.fge.jsonpatch.JsonPatch;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class PatchUtil {
    private final ObjectMapper objectMapper = JsonMapper.builder()
            .findAndAddModules()
            .build();

    public <T> T applyPatch(@NonNull JsonPatch patch, @NonNull T data, Class<T> clazz) {
        try {
            JsonNode patched = patch.apply(objectMapper.convertValue(data, JsonNode.class));
            return objectMapper.treeToValue(patched, clazz);
        } catch (Exception e) {
            log.error("Error while creating patch [{}]", patch, e);
        }
        return null;
    }
}
