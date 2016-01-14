package com.git.cs309.mmoserver.util;

import java.io.IOException;

public class EndOfStreamReachedException extends IOException {

    /**
     * 
     */
    private static final long serialVersionUID = 763958728706051858L;

    public EndOfStreamReachedException(final String message) {
	super(message);
    }
}
