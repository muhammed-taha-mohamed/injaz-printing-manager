package com.injaz.print;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({ @PropertySource("file:C:/injaz-printing-installer/application.properties")})
public class InjazPropertiesFilesConfig {

}
