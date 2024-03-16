package com.github.skozlov.algorithms.sort
import scala.annotation.tailrec
import scala.collection.mutable
import scala.math.Ordered.orderingToOrdered

object InsertionSort extends InPlaceSort {
  override def sortInPlace[A: Ordering](
      elements: mutable.IndexedSeq[A]
  ): Unit = {
    for (indexFrom <- 1 until elements.length) {
      val elementToInsert = elements(indexFrom)

      @tailrec
      def shiftBiggerReturningIndexTo(maxIndexToShift: Int): Int = {
        if (maxIndexToShift < 0) {
          0
        } else {
          val elementToShift = elements(maxIndexToShift)
          if (elementToInsert >= elementToShift) {
            maxIndexToShift + 1
          } else {
            shiftBiggerReturningIndexTo(maxIndexToShift - 1)
          }
        }
      }

      val indexTo = shiftBiggerReturningIndexTo(maxIndexToShift = indexFrom - 1)
      elements(indexTo) = elementToInsert
    }
  }
}
