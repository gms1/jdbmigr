/*******************************************************************
 * Copyright (c) 2006, All rights reserved
 *
 * This software is licensed under the terms of the MIT License,
 * see the LICENSE file for details.
 *
 ******************************************************************/
package net.sf.gm.core.cmdline;

//


/**
 * base class for exceptions thrown by cmdline.Parser
 */
public class CmdLineException extends Exception {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 4166565000255483244L;

    /**
     * constructor.
     *
     * @param message the message
     */
    public CmdLineException(final String message) {
        super(message);
    }

    /**
     * exception indicating a missing argument.
     */
    public static class DuplicateOptionNameException extends CmdLineException {

        /**
         * The Constant serialVersionUID.
         */
        private static final long serialVersionUID = -4933016825376514856L;

        /**
         * constructor.
         *
         * @param name the name
         */
        public DuplicateOptionNameException(final String name) {

            super("duplicate option with same name found: '" + name + "'");
        }
    }


    /**
     * exception indicating a missing argument.
     */
    public static class MissingOptionArgumentException extends CmdLineException {

        /**
         * The Constant serialVersionUID.
         */
        private static final long serialVersionUID = 5100819954298230450L;

        /**
         * constructor.
         *
         * @param option the option
         */
        public MissingOptionArgumentException(final OptionBase option) {

            super("Missing argument for option '" + option.getDisplayName() + "'");
        }
    }


    /**
     * exception indicating a wrong argument.
     */
    public static class IllegalOptionArgumentException extends CmdLineException {

        /**
         * The Constant serialVersionUID.
         */
        private static final long serialVersionUID = -5441322931691052424L;

        /**
         * constructor.
         *
         * @param argument the argument
         * @param option   the option
         */
        public IllegalOptionArgumentException(final OptionBase option,
            final String argument) {

            super("Illegal argument '" + argument + "' for option '" +
                option.getDisplayName() + "'");
        }
    }


    /**
     * exception indicating a unknown option.
     */
    public static class UnknownOptionException extends CmdLineException {

        /**
         * The Constant serialVersionUID.
         */
        private static final long serialVersionUID = 8869723387039199974L;

        /**
         * constructor.
         *
         * @param name the name
         */
        public UnknownOptionException(final String name) {

            super("unknown option '" + name + "'");
        }
    }


    /**
     * exception indicating not enough missing arguments.
     */
    public static class NotEnoughArgumentsException extends CmdLineException {

        /**
         * The Constant serialVersionUID.
         */
        private static final long serialVersionUID = 225646663603160590L;

        /**
         * constructor.
         */
        public NotEnoughArgumentsException() {
            super("not enough arguments");
        }
    }


    /**
     * exception indicating too many arguments.
     */
    public static class TooManyArguments extends CmdLineException {

        /**
         * The Constant serialVersionUID.
         */
        private static final long serialVersionUID = -6882166339064053032L;

        /**
         * constructor.
         */
        public TooManyArguments() {
            super("to many arguments");
        }
    }


    /**
     * exception indicating an argument which is too long.
     */
    public static class ArgumentTooLong extends CmdLineException {

        /**
         * The Constant serialVersionUID.
         */
        private static final long serialVersionUID = -1753879257905632531L;

        /**
         * constructor.
         *
         * @param arg    the arg
         * @param option the option
         */
        public ArgumentTooLong(final OptionBase option, final String arg) {

            super("argument is to long: '" + arg + "'");
        }
    }


    /**
     * exception indicating an invalid file argument.
     */
    public static class InvalidFileArgument extends CmdLineException {

        /**
         * The Constant serialVersionUID.
         */
        private static final long serialVersionUID = -35129684826377750L;

        /**
         * constructor.
         *
         * @param arg    the arg
         * @param option the option
         * @param cause  the cause
         */
        public InvalidFileArgument(final OptionBase option, final String arg,
            final String cause) {

            super("invalid file/directory argument: '" + arg + "' " + cause);
        }

        /**
         * The Constructor.
         *
         * @param arg   the arg
         * @param cause the cause
         */
        public InvalidFileArgument(final String arg, final String cause) {

            super("invalid file/directory argument: '" + arg + "' " + cause);
        }

        /**
         * The Constructor.
         *
         * @param cause the cause
         */
        public InvalidFileArgument(final String cause) {

            super("invalid file/directory argument: " + cause);
        }
    }


    /**
     * exception indicating a missing argument.
     */
    public static class HelpOptionSelected extends CmdLineException {

        /**
         * The Constant serialVersionUID.
         */
        private static final long serialVersionUID = -9205225678066998417L;

        /**
         * constructor.
         */
        public HelpOptionSelected() {
            super("help option selected");
        }
    }
}
