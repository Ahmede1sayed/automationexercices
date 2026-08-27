package drivers;

import automationexercices.drivers.AbstractDriver;
import automationexercices.drivers.chromeFactory;
import automationexercices.drivers.edgeFactory;

public enum Browser {
    CHROME {
        @Override
        public AbstractDriver getDriverFactory() {
            return new chromeFactory();
        }
    },
    EDGE {
        @Override
        public AbstractDriver getDriverFactory() {
            return new edgeFactory();
        }
    };
    public abstract AbstractDriver getDriverFactory();
}