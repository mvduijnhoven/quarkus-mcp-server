package io.quarkiverse.mcp.server.runtime.jsonschema.jakarta;

import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.module.jakarta.validation.JakartaValidationModule;
import com.github.victools.jsonschema.module.jakarta.validation.JakartaValidationOption;
import io.quarkiverse.mcp.server.runtime.jsonschema.jakarta.config.JakartaValidationBuildTimeConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JakartaValidationSchemaGeneratorConfigCustomizerUnitTest {

    @Mock
    JakartaValidationBuildTimeConfig buildTimeConfigMock;

    @Mock
    SchemaGeneratorConfigBuilder builderMock;

    @Captor
    ArgumentCaptor<JakartaValidationModule> moduleCaptor;

    @InjectMocks
    JakartaValidationSchemaGeneratorConfigCustomizer customizer;

    // @BeforeEach: Handled by MockitoExtension and @InjectMocks

    private EnumSet<JakartaValidationOption> getOptionsFromModule(JakartaValidationModule module) throws NoSuchFieldException, IllegalAccessException {
        Field optionsField = JakartaValidationModule.class.getDeclaredField("options");
        optionsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        EnumSet<JakartaValidationOption> actualOptions = (EnumSet<JakartaValidationOption>) optionsField.get(module);
        return actualOptions;
    }

    @Test
    void testCustomize_allOptionsTrue() throws Exception {
        // Arrange
        when(buildTimeConfigMock.notNullableFieldIsRequired).thenReturn(true);
        when(buildTimeConfigMock.notNullableMethodIsRequired).thenReturn(true);
        when(buildTimeConfigMock.includePatternProperties).thenReturn(true);
        when(buildTimeConfigMock.includeValidationMessages).thenReturn(true);

        EnumSet<JakartaValidationOption> expectedOptions = EnumSet.of(
                JakartaValidationOption.NOT_NULLABLE_FIELD_IS_REQUIRED,
                JakartaValidationOption.NOT_NULLABLE_METHOD_IS_REQUIRED,
                JakartaValidationOption.INCLUDE_PATTERN_PROPERTIES,
                JakartaValidationOption.INCLUDE_VALIDATION_MESSAGES
        );

        // Act
        customizer.customize(builderMock);

        // Assert
        verify(builderMock).with(moduleCaptor.capture());
        JakartaValidationModule capturedModule = moduleCaptor.getValue();
        assertNotNull(capturedModule);

        EnumSet<JakartaValidationOption> actualOptions = getOptionsFromModule(capturedModule);
        assertEquals(expectedOptions, actualOptions);
    }

    @Test
    void testCustomize_allOptionsFalse() throws Exception {
        // Arrange
        when(buildTimeConfigMock.notNullableFieldIsRequired).thenReturn(false);
        when(buildTimeConfigMock.notNullableMethodIsRequired).thenReturn(false);
        when(buildTimeConfigMock.includePatternProperties).thenReturn(false);
        when(buildTimeConfigMock.includeValidationMessages).thenReturn(false);

        // Act
        customizer.customize(builderMock);

        // Assert
        // The module is always added, but it might be configured with an empty set of options.
        // The JavaxValidationModule constructor (and presumably JakartaValidationModule)
        // called without arguments still adds all options.
        // schemaGeneratorConfigBuilder.with(new JavaxValidationModule(options.toArray(new JavaxValidationOption[0])));
        // If options list is empty, it becomes new JavaxValidationModule()
        // Let's check the source for JavaxValidationModule(). It adds all options by default.
        // The code in JakartaValidationSchemaGeneratorConfigCustomizer is:
        // if (!options.isEmpty()) {
        //    builder.with(new JakartaValidationModule(options.toArray(new JakartaValidationOption[0])));
        // } else {
        //    // If no specific options are set due to config, it might add the module with its defaults.
        //    // Or it might not add it if we change the logic.
        //    // Current logic:
        //    // builder.with(new JavaxValidationModule()); // (referring to similar Javax code)
        //    // This means it WILL add the module, and that module will have its own defaults.
        //    // The default constructor of JakartaValidationModule enables all options.
        // }
        // The actual code for Jakarta is:
        // if (!options.isEmpty()) {
        // builder.with(new JakartaValidationModule(options.toArray(new JakartaValidationOption[0])));
        // } else {
        // builder.with(new JakartaValidationModule()); // This line is NOT in the actual code.
        // }
        // The actual code is:
        //  if (!options.isEmpty()) {
        //      builder.with(new JavaxValidationModule(options.toArray(new JavaxValidationOption[0])));
        //  } // No else block. So if options is empty, the module is NOT added.
        // This was for JAVAX. Let me re-check JAKARTA's customizer.
        // Jakarta customizer:
        // public void customize(SchemaGeneratorConfigBuilder builder) {
        //    List<JakartaValidationOption> options = new ArrayList<>();
        //    ...
        //    if (!options.isEmpty()) { // This is key!
        //        builder.with(new JakartaValidationModule(options.toArray(new JakartaValidationOption[0])));
        //    }
        // }
        // So, if all config flags are false, the options list IS empty, and builder.with() is NOT called.

        customizer.customize(builderMock); // Re-act if necessary, though state shouldn't change.

        // Assert
        verify(builderMock, never()).with(any(JakartaValidationModule.class));
    }


    @Test
    void testCustomize_mixedOptions() throws Exception {
        // Arrange
        when(buildTimeConfigMock.notNullableFieldIsRequired).thenReturn(true);  // Default true
        when(buildTimeConfigMock.notNullableMethodIsRequired).thenReturn(false); // Default true
        when(buildTimeConfigMock.includePatternProperties).thenReturn(true);    // Default true
        when(buildTimeConfigMock.includeValidationMessages).thenReturn(false); // Default true

        EnumSet<JakartaValidationOption> expectedOptions = EnumSet.noneOf(JakartaValidationOption.class);
        expectedOptions.add(JakartaValidationOption.NOT_NULLABLE_FIELD_IS_REQUIRED);
        expectedOptions.add(JakartaValidationOption.INCLUDE_PATTERN_PROPERTIES);

        // Act
        customizer.customize(builderMock);

        // Assert
        verify(builderMock).with(moduleCaptor.capture());
        JakartaValidationModule capturedModule = moduleCaptor.getValue();
        assertNotNull(capturedModule);

        EnumSet<JakartaValidationOption> actualOptions = getOptionsFromModule(capturedModule);
        assertEquals(expectedOptions, actualOptions);
    }

    @Test
    void testCustomize_oneOptionTrue_othersFalse() throws Exception {
        // Arrange
        when(buildTimeConfigMock.notNullableFieldIsRequired).thenReturn(false);
        when(buildTimeConfigMock.notNullableMethodIsRequired).thenReturn(false);
        when(buildTimeConfigMock.includePatternProperties).thenReturn(true); // Only this one true
        when(buildTimeConfigMock.includeValidationMessages).thenReturn(false);

        EnumSet<JakartaValidationOption> expectedOptions = EnumSet.of(
                JakartaValidationOption.INCLUDE_PATTERN_PROPERTIES
        );

        // Act
        customizer.customize(builderMock);

        // Assert
        verify(builderMock).with(moduleCaptor.capture());
        JakartaValidationModule capturedModule = moduleCaptor.getValue();
        assertNotNull(capturedModule);

        EnumSet<JakartaValidationOption> actualOptions = getOptionsFromModule(capturedModule);
        assertEquals(expectedOptions, actualOptions);
    }
}
