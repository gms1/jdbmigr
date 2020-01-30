/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.cmdline;

import java.util.Locale;

//


/**
 * The Class OptionalFlag.
 */
public class OptionalFlag extends OptionBase {

    /**
     * constructor.
     *
     * @param shortNames  the short names
     * @param description the description
     * @param longNames   the long names
     */
    public OptionalFlag(final String shortNames, final String longNames,
        final String description) {

        super(shortNames, longNames, description, false);
    }

    /**
     * constructor.
     *
     * @param shortNames  the short names
     * @param parser      the parser
     * @param description the description
     * @param longNames   the long names
     */
    public OptionalFlag(final CmdLineParser parser, final String shortNames,
        final String longNames, final String description) {

        super(parser, shortNames, longNames, description, false);
    }

    /**
     * set the argument for this type of option.
     *
     * @param locale the locale
     * @param arg    the arg
     * @throws CmdLineException the cmd line exception
     */
    @Override
    protected void setArgument(final String arg, final Locale locale)
        throws CmdLineException {

        if (arg != null)
            throw new CmdLineException.IllegalOptionArgumentException(this, arg);
        setArgument(true);
    }

    /**
     * get the option value.
     *
     * @return the value
     */
    public boolean getValue() {

        return getArgument() != null ? ((Boolean) getArgument()).booleanValue()
            : false;
    }

    /**
     * get the option value.
     *
     * @param defValue the def value
     * @return the value
     */
    public boolean getValue(final boolean defValue) {

        return getArgument() != null ? getValue() : defValue;
    }
}
