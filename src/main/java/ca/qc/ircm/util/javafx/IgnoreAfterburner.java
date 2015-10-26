package ca.qc.ircm.util.javafx;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Use on classes to be ignore by afterburner detection of injectable types.
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface IgnoreAfterburner {
}
