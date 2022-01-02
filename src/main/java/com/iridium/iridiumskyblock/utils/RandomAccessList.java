package com.iridium.iridiumskyblock.utils;

import org.apache.commons.lang.math.IntRange;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * List which holds elements with certain possibilities and can randomly generate a random one via
 * {@link RandomAccessList#nextElement()}.
 *
 * @param <E> The type of elements in this list
 */
public class RandomAccessList<E> {

    private final List<RandomAccessElement<E>> elements = new ArrayList<>();
    private int size = 0;

    /**
     * Adds an element with a certain possibility to this list.
     * The possibility depends on the possibilities of the other elements, it is relative.
     *
     * @param element The element which should be added
     * @param possibility The possibility of this element being selected
     */
    public void add(E element, int possibility) {
        elements.add(new RandomAccessElement<>(element, new IntRange(size, size + possibility - 1)));
        size += possibility;
    }

    /**
     * Adds a Map of elements to the list where the key is an element and the value its possibility.
     * The possibility depends on the possibilities of the other elements, it is relative.
     *
     * @param elementsWithPossibilities Map of the elements with their possibilities
     */
    public void addAll(Map<E, Integer> elementsWithPossibilities) {
        elementsWithPossibilities.forEach(this::add);
    }

    /**
     * Generates a random element from this list, considering the specified possibilities.
     *
     * @return A random element
     */
    public Optional<E> nextElement() {
        if (elements.isEmpty()) return Optional.empty();
        int randomIndex = ThreadLocalRandom.current().nextInt(size);

        return elements.stream()
                .filter(element -> element.isInsideRange(randomIndex))
                .map(RandomAccessElement::getElement)
                .findAny();
    }

    /**
     * Returns the internal size of this list.
     * This is equivalent to all the probabilities of this list combined.
     *
     * @return The internal size
     */
    public int getSize() {
        return size;
    }

    /**
     * Internal element of a {@link RandomAccessList}.
     *
     * @param <E> The type represented by this element.
     */
    private static class RandomAccessElement<E> {

        private final E element;
        private final IntRange range;

        /**
         * The default constructor.
         *
         * @param element The element represented by this element
         * @param range The range which represents all indexes this element belongs to (inclusive)
         */
        public RandomAccessElement(E element, IntRange range) {
            this.element = element;
            this.range = range;
        }

        /**
         * Returns the element of the {@link RandomAccessList} represented by this element.
         *
         * @return The represented element
         */
        public E getElement() {
            return element;
        }

        /**
         * Returns if the provided integer is inside the range for this element.
         *
         * @param index The index which should be checked
         * @return true if the provided integer is inside the range, false otherwise
         */
        public boolean isInsideRange(int index) {
            return range.containsInteger(index);
        }

    }

}
