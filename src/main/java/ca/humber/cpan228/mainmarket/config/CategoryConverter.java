package ca.humber.cpan228.mainmarket.config;

import ca.humber.cpan228.mainmarket.repository.CategoryRepository;
import ca.humber.cpan228.mainmarket.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryConverter implements Converter<String, Category> {

    private final CategoryRepository categoryRepository;

    @Override
    public Category convert(String id) {
        if (id == null || id.isBlank()) return null;
        return categoryRepository.findById(Long.parseLong(id)).orElse(null);
    }
}