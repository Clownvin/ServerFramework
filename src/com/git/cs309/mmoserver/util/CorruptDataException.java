package com.git.cs309.mmoserver.util;

import java.io.IOException;

public class CorruptDataException extends IOException {

    /**
     * 
     */
    private static final long serialVersionUID = 2198643892554975280L;
    
    public CorruptDataException(final String message) {
	super(message);
    }
}
