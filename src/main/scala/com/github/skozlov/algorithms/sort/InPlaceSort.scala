package com.github.skozlov.algorithms.sort

import com.github.skozlov.commons.collection.WritableSlice

trait InPlaceSort {
  def sortInPlace[A: Ordering](elements: WritableSlice[A]): Unit
}
