package com.github.skozlov.algorithms.sort

import scala.collection.mutable

trait InPlaceSort {
  def sortInPlace[A: Ordering](elements: mutable.IndexedSeq[A]): Unit
}
