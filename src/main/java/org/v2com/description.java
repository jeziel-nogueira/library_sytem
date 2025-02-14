package org.v2com;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@OpenAPIDefinition(
        tags = {
                @Tag(name = "API V2Com")
        },
        info = @Info(
                title = " API Title",
                version = "1.0.2"
        )
)

public class description {
}
