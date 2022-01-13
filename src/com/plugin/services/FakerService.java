package com.plugin.services;

import com.github.javafaker.Faker;
import com.plugin.utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakerService {

    private final Faker faker = new Faker();

    public Object getFakeForField(String field) {
        HashMap<String, List<Object>> config = new HashMap(){{
            put("city", List.of(faker.address().city()));
            put("address", List.of(faker.address().fullAddress()));
            put("street", List.of(faker.address().streetAddress()));
            put("zipCode", List.of(faker.address().	zipCode()));
            put("email", List.of(faker.internet().safeEmailAddress()));
            put("firstName", List.of(faker.name().firstName()));
            put("lastName", List.of(faker.name().lastName()));
            put("age", List.of(faker.number().numberBetween(0, 100)));
        }};
        Map.Entry<String, List<Object>> stringStringEntry = config.entrySet()
                                                            .stream()
                                                            .filter(o -> StringUtils.hammingDist(o.getKey(), field) < 3)
                                                            .findFirst()
                                                            .orElse(null);
        return stringStringEntry == null ? field : stringStringEntry.getValue().get(0);
    }


}
