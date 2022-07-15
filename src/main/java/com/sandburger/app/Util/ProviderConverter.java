package com.sandburger.app.Util;

import com.sandburger.app.model.Provider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

public class ProviderConverter implements Converter<String, Provider> {
    @Override
    public Provider convert(String source) {
        return Provider.valueOf(source.toUpperCase());
    }
}
