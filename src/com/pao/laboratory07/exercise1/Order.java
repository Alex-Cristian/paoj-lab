package com.pao.laboratory07.exercise1;

import com.pao.laboratory07.exercise1.exceptions.CannotCancelFinalOrderException;
import com.pao.laboratory07.exercise1.exceptions.CannotRevertInitialOrderStateException;
import com.pao.laboratory07.exercise1.exceptions.OrderIsAlreadyFinalException;

import java.util.ArrayDeque;
import java.util.Deque;

public class Order {
    private OrderState state;
    private final Deque<OrderState> history;

    public Order(OrderState initialState) {
        this.state = initialState;
        this.history = new ArrayDeque<>();
    }

    public OrderState getState() {
        return state;
    }

    public void nextState() {
        if (state.isFinalState()) {
            throw new OrderIsAlreadyFinalException();
        }
        history.push(state);
        state = state.next();
    }

    public void cancel() {
        if (state.isFinalState()) {
            throw new CannotCancelFinalOrderException();
        }
        history.push(state);
        state = OrderState.CANCELED;
    }

    public void undoState() {
        if (history.isEmpty()) {
            throw new CannotRevertInitialOrderStateException();
        }
        state = history.pop();
    }
}
