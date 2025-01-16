package br.com.rplbasicmessages.commons.utils;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClassUtils {

    @SuppressWarnings("unchecked")
    public static <T> Class<List<T>> getClassList() {
        return (Class<List<T>>) (Class<?>) List.class;
    }
}
