package edu.brown.cs32.live.repl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Example "naive" REPL. This first version uses an if-else statement
 * to decide which command to run, and all commands are hard coded.
 * Error handling is also rather messy and ad-hoc.
 */
public class REPL implements Runnable {
    public REPL() {}

     /*
      EXERCISE 2: how can we make this REPL more extensible?
     */

     /*
      EXERCISE 3: how can we test this REPL?
     */

    Map<String, CommandFunction> registry = new HashMap<>();

    public CommandFunction registerCommand(String cmd, CommandFunction f) {
        return registry.put(cmd.toUpperCase(), f);
    }

    // We could also use lambdas, like this:
    // (args) -> { ... } ...
//    Map<String, Function<List<String>, String>> registry = new HashMap<>();
//    public Function<List<String>, String> registerCommand(String cmd, Function<List<String>, String> f) {
//        return registry.put(cmd.toUpperCase(), f);
//    }

    @Override
    public void run() {
        // This is a "try with resources"; the resource will automatically
        // be closed if necessary. Prefer this over finally blocks.
        // See: https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String input;
            while ((input = br.readLine()) != null) {
                List<String> parsedInput = parseInput(input);

                try {

                    if (input.equalsIgnoreCase("EXIT")) {
                        return;
                    } else if (registry.containsKey(parsedInput.get(0).toUpperCase())) {
                        CommandFunction f = registry.get(parsedInput.get(0).toUpperCase());
                        System.out.println(f.run(parsedInput));
                    } else {
                        // handle "no such command" gracefully; report to the user and continue
                        System.err.println("ERROR: Invalid command name.");
                    }
                } catch(IllegalArgumentException ex) {
                    System.err.println(ex.getMessage());
                    // continue gracefully; the error was just about one command, maybe typo
                }
                // TODO: what happens if a different exception is encountered? What /should/
                // happen? This is worth thinking about, related to the Server sprint.
            }

        /*
          EXERCISE 1: how can we improve this error handling?
         */
        } catch (IOException ex) {
            System.err.println("ERROR reading from input.");

        }

    }

    private void runHi(List<String> parsedInput) {
        if(parsedInput.size() != 2) {
            throw new IllegalArgumentException("'hi' command requires one argument");
        }
        System.out.println("Hi, "+parsedInput.get(1));
    }

    private void runAdd(List<String> parsedInput) {
        if(parsedInput.size() != 3) {
            throw new IllegalArgumentException("'add' command requires two arguments");
        }
        try {
            int arg1 = Integer.parseInt(parsedInput.get(1));
            int arg2 = Integer.parseInt(parsedInput.get(2));
            int result = arg1 + arg2;
            System.out.println(result);
        } catch(NumberFormatException e) {
            throw new IllegalArgumentException("'add' command needs integer arguments");
        }
    }


    /**
     * Parses user input via a regular expression.
     * NOTE: this may be a useful reference example, but don't focus here
     * during class---it's not the main point.
     *
     * @param input String of user input to be parsed.
     * @return list containing the input split input at whitespaces except
     * within quotes or apostrophes.
     */
    public static List<String> parseInput(String input) {
        List<String> parsedInput = new ArrayList<>();
        // regex parsing adapted from: https://stackoverflow.com/q/366202/
        Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"");
        Matcher regexMatcher = regex.matcher(input);
        while (regexMatcher.find()) {
            if (regexMatcher.group(1) != null) {
                // Add double-quoted string with the quotes
                parsedInput.add("\"" + regexMatcher.group(1) + "\"");
            } else {
                // Add unquoted word
                parsedInput.add(regexMatcher.group());
            }
        }
        return parsedInput;
    }
}
