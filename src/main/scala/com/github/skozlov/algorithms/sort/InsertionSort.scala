package com.github.skozlov.algorithms.sort

import com.github.skozlov.commons.collection.{Slice, WritableSlice}

import scala.annotation.tailrec
import scala.math.Ordered.orderingToOrdered

/** Simple sorting algorithm that is efficient for small sequences.
  *
  * Time consumption: O(n<sup>2</sup>).
  *
  * Memory consumption: O(1).
  *
  * @see
  *   [[https://en.wikipedia.org/wiki/Insertion_sort]]
  */
class InsertionSort[A] extends InPlaceSort[A] with FunctionalSort[A] with StableSort {
  private def sort(in: Slice[A], out: WritableSlice[A])(implicit ordering: Ordering[A]): Unit = {
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

  override def sortInPlace(elements: WritableSlice[A])(implicit ordering: Ordering[A]): Unit = {
    sort(in = elements, out = elements)
  }

  override def sortFunctionally(
      in: Slice[A],
      out: WritableSlice[A],
  )(implicit ordering: Ordering[A]): Unit = {
    sort(in, out)
  }
}
