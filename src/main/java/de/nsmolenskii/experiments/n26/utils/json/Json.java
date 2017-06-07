
package de.nsmolenskii.experiments.n26.utils.json;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.immutables.value.Value;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.immutables.value.Value.Style.ImplementationVisibility.PACKAGE;
import static org.immutables.value.Value.Style.ValidationMethod.NONE;

@Value.Style(
        jdkOnly = true,
        depluralize = true,
        visibility = PACKAGE,
        validationMethod = NONE,
        forceJacksonPropertyNames = false,
        implementationNestedInBuilder = true
)
@Target(TYPE)
@Retention(RUNTIME)
@JacksonAnnotation
@JacksonAnnotationsInside
@JsonIgnoreProperties(ignoreUnknown = true)
public @interface Json {
}
