package com.beval.server;

import org.springframework.test.context.TestExecutionListeners;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@TestExecutionListeners(mergeMode =
        TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS,
        listeners = { ResetDatabaseTestExecutionListener.class }
)
public @interface ResetDatabase {
}
