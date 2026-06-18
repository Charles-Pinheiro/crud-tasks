package com.project.tasks.application.validation.task.create;

import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CreateTaskValidationChain {

    private final CreateTaskValidationHandler firstHandler;

    public CreateTaskValidationChain(List<CreateTaskValidationHandler> handlers) {
        List<CreateTaskValidationHandler> orderedHandlers = new ArrayList<>(handlers);
        orderedHandlers.sort(AnnotationAwareOrderComparator.INSTANCE);

        this.firstHandler = buildChain(orderedHandlers);
    }

    public void validate(CreateTaskValidationContext context) {
        if (firstHandler != null) {
            firstHandler.validate(context);
        }
    }

    private CreateTaskValidationHandler buildChain(List<CreateTaskValidationHandler> handlers) {
        if (handlers.isEmpty()) {
            return null;
        }

        CreateTaskValidationHandler first = handlers.getFirst();
        CreateTaskValidationHandler current = first;

        for (int i = 1; i < handlers.size(); i++) {
            current = current.linkWith(handlers.get(i));
        }

        return first;
    }
}
