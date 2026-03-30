package ca.humber.cpan228.mainmarket.config;

import ca.humber.cpan228.mainmarket.repository.BrandRepository;
import ca.humber.cpan228.mainmarket.entity.Brand;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BrandConverter implements Converter<String, Brand> {

    private final BrandRepository brandRepository;

    @Override
    public Brand convert(String id) {
        if (id == null || id.isBlank()) return null;
        return brandRepository.findById(Long.parseLong(id)).orElse(null);
    }
}