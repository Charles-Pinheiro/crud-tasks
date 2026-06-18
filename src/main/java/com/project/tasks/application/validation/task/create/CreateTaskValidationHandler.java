package com.project.tasks.application.validation.task.create;

public abstract class CreateTaskValidationHandler {

    private CreateTaskValidationHandler next;

    public CreateTaskValidationHandler linkWith(CreateTaskValidationHandler next) {
        this.next = next;
        return next;
    }

    public final void validate(CreateTaskValidationContext context) {
        validateCurrent(context);

        if (next != null) {
            next.validate(context);
        }
    }

    protected abstract void validateCurrent(CreateTaskValidationContext context);
}
