package com.github.skozlov.algorithms.sort

import scala.annotation.tailrec
import scala.collection.mutable
import scala.math.Ordered.orderingToOrdered

object InsertionSort {

  /** Sorts the input sequence putting elements to the output sequence.
    *
    * The sequences may be independent (then the input sequence isn't modified)
    * or be the same sequence (then in-place sort is performed).
    *
    * This sort is stable.
    *
    * Time consumption: O(n<sup>2</sup>).
    *
    * Memory consumption: O(1).
    * @param in
    *   input sequence which contains elements to sort
    * @param out
    *   output sequence to put sorted elements into
    * @throws IllegalArgumentException
    *   if the input and output sequences have different sizes
    */
  def sort[A: Ordering](
      in: collection.IndexedSeq[A],
      out: mutable.IndexedSeq[A],
  ): Unit = {
    require(
      in.size == out.size,
      s"in and out have different sizes: ${in.size} and ${out.size}",
    )

    if (in.nonEmpty) {

      /** Assuming that out[0:index-1] contains sorted in[0:index-1], inserts
        * in[index] into out[0:index-1] so that out[0:index] contains sorted
        * in[0:index].
        */
      def insert(index: Int): Unit = {
        val elementToInsert = in(index)

        /** In out sequence, shifts elements which are greater than
          * elementToInsert one position to the right, going right to left
          * starting from startIndex.
          * @return
          *   the index to insert elementToInsert into after the shift
          */
        @tailrec
        def shiftGreaterReturningTargetIndex(startIndex: Int): Int = {
          val elementToCompare = out(startIndex)
          if (elementToCompare > elementToInsert) {
            out(startIndex + 1) = elementToCompare
            if (startIndex == 0) {
              0
            } else {
              shiftGreaterReturningTargetIndex(startIndex - 1)
            }
          } else startIndex + 1
        }

        val targetIndex =
          shiftGreaterReturningTargetIndex(startIndex = index - 1)
        out(targetIndex) = elementToInsert
      }

      out(0) = in(0)
      for (i <- 1 until in.size) {
        insert(i)
      }
    }
  }
}
