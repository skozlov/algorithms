package com.github.skozlov.algorithms.sort

import com.github.skozlov.commons.collection.WritableSlice

trait InPlaceSort[A] {
  def sortInPlace(elements: WritableSlice[A])(implicit ordering: Ordering[A]): Unit
}
