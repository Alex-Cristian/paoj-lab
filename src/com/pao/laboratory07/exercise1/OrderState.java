package com.pao.laboratory07.exercise1;

public enum OrderState {
    PLACED {
        @Override
        public OrderState next() {
            return PROCESSED;
        }
    },
    PROCESSED {
        @Override
        public OrderState next() {
            return SHIPPED;
        }
    },
    SHIPPED {
        @Override
        public OrderState next() {
            return DELIVERED;
        }
    },
    DELIVERED {
        @Override
        public OrderState next() {
            throw new IllegalStateException("Final state");
        }

        @Override
        public boolean isFinalState() {
            return true;
        }
    },
    CANCELED {
        @Override
        public OrderState next() {
            throw new IllegalStateException("Final state");
        }

        @Override
        public boolean isFinalState() {
            return true;
        }
    };

    public abstract OrderState next();

    public boolean isFinalState() {
        return false;
    }
}
