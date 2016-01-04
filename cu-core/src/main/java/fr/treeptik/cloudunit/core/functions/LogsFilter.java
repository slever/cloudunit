package fr.treeptik.cloudunit.core.functions;

import fr.treeptik.cloudunit.core.dto.LogUnit;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Created by nicolas on 04/01/2016.
 */
public class LogsFilter {

    public static BiFunction<String, List<LogUnit>, List<LogUnit>> bySource =
            (source, messages) -> messages.stream()
                    .filter(m -> source.equals(m.getSource()))
                    .collect(Collectors.toList());

    public static BiFunction<String, List<LogUnit>, List<LogUnit>> byMessage =
            (pattern, messages) -> messages.stream()
                    .filter(m -> m.getMessage().toLowerCase().contains(pattern.toLowerCase()))
                    .collect(Collectors.toList());

}
