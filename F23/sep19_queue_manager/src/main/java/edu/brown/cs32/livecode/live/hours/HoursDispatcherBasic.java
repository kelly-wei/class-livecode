package edu.brown.cs32.livecode.live.hours;

import java.util.*;

// Live code things that are wrong/worrisome about the code

/**
 * Basic version of the hours dispatcher. Accepts an
 * Iterator of Students and follows that ordering as it assigns TAs.
 */

//no private or public modifiers - private can be used to protect from adversary but also sending a msg that i want to control access to this data
    // side stepping getters/setters --> unknown behavior --> a lot of fieds should be private

    //final field - can't change the object inside the field/reference (trying to re-assign = error)
    //does not offer protection against mutation of context of variables
public class HoursDispatcherBasic implements Dispatcher {
    Iterator<Student> queue;
    Map<TA, Integer> minutesLeft;
    // what if the TA's have the same names -> requires the manufacturing of TA objects - not an abstraction caller wants to work with
    String statusMessage;

    HoursDispatcherBasic(Iterator<Student> signups, String statusMessage) {
        this.queue = signups;
        this.statusMessage = statusMessage;
        this.minutesLeft = new HashMap<>();
    }

    /**
     * @return the current minutes that each active TA has left
     *
     */
    // This data structure could be modified
    // to prevent: defensive copying
    public Map<TA, Integer> getMinutesLeft() {
        return minutesLeft; --> return new HashMap<>(minutesLeft) //providing a copy of the referenced map --> removed the threat of modifying data structure from underneath developer
        // deep copy vs shallow copy --> shallow = not a copy of all TAs but protects hashmap
        // prevented caller from changing TA queue but the data will not be updated/synched with other copies
    }

    /**
     * Add a TA to the pool, with "minutes" time remaining. If the TA is already
     * in the pool, add that amount of time to their time remaining.
     * @param ta the TA who is joining the queue
     * @param minutes the number of minutes this TA will hold hours
     * @throws IllegalArgumentException if the time remaining is invalid
     */
    void addTA(TA ta, int minutes) throws IllegalArgumentException {
        if(minutes <= 0) {
            throw new IllegalArgumentException("minutes must be greater than 0");
        }
        if(!minutesLeft.containsKey(ta))
            minutesLeft.put(ta, minutes);
        else
            minutesLeft.put(ta, minutes + minutesLeft.get(ta));
    }

}
