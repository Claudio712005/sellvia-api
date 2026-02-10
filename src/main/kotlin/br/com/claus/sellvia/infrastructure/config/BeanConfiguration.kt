package br.com.claus.sellvia.infrastructure.config

import br.com.claus.sellvia.domain.annotation.UseCase
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType

@Configuration
@ComponentScan(
    basePackages = ["br.com.claus.sellvia"],
    includeFilters = [
        ComponentScan.Filter(type = FilterType.ANNOTATION, classes = [UseCase::class])
    ]
)
class BeanConfiguration