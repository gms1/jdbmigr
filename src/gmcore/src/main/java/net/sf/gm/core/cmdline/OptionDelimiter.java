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
 * The Class OptionDelimiter.
 */
public class OptionDelimiter extends OptionBase {

    /**
     * constructor.
     */
    public OptionDelimiter() {
        super(null, null, "", false);
    }

    /**
     * constructor.
     *
     * @param description the description
     */
    public OptionDelimiter(final String description) {

        super(null, null, description, false);
    }

    /**
     * constructor.
     *
     * @param parser the parser
     */
    public OptionDelimiter(final CmdLineParser parser) {

        super(parser, null, null, "", false);
    }

    /**
     * constructor.
     *
     * @param parser      the parser
     * @param description the description
     */
    public OptionDelimiter(final CmdLineParser parser, final String description) {

        super(parser, null, null, description, false);
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

        throw new CmdLineException.IllegalOptionArgumentException(this, arg);
    }
}
