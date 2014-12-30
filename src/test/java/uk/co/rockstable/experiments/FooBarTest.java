package uk.co.rockstable.experiments;

import org.junit.Before;
import org.junit.Test;
import uk.co.rockstable.experiments.asm.Bar;
import uk.co.rockstable.experiments.asm.BarFactory;
import uk.co.rockstable.experiments.asm.Foo;
import uk.co.rockstable.experiments.asm.FooBarFactory;
import uk.co.rockstable.experiments.asm.FooFactory;

import static org.junit.Assert.*;

public class FooBarTest {

    private Object factory;

    @Before
    public void setUpFactory() {
        factory = FooBarFactory.newInstance();
    }

    @Test
    public void factoryCreatesFoo() {
        Object foo = ((FooFactory) factory).create();
        assertEquals(Foo.class, foo.getClass());
    }

    @Test
    public void factoryCreatesBar() {
        Object bar = ((BarFactory) factory).create();
        assertEquals(Bar.class, bar.getClass());
    }
}
