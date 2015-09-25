package com.sumologic.cs.omus.commandline;

import com.sumologic.cs.omus.test.BaseOMUSTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class OMUSCommandLineTest extends BaseOMUSTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testNoErrorOccursOnHelp() {
        OMUSCommandLine.main(new String[] {"h"});
    }

}