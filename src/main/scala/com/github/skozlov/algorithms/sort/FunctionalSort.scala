package com.github.skozlov.algorithms.sort

import com.github.skozlov.commons.collection.{Slice, WritableSlice}

import scala.reflect.ClassTag

/** Sort which puts ordered elements into the output sequence and does not
  * modify the input sequence.
  */
trait FunctionalSort {

  /** Sorts the input sequence putting elements into the output sequence. Input
    * sequence will not be modified.
    *
    * @param in
    *   input slice which contains elements to sort
    * @param out
    *   output slice to put sorted elements into
    * @throws IllegalArgumentException
    *   if the input and output sequences have different sizes
    */
  def sortFunctionally[A: Ordering](in: Slice[A], out: WritableSlice[A]): Unit

  /** Sorts the input sequence putting elements into the new array. Input
    * sequence will not be modified.
    * @param in
    *   input slice which contains elements to sort
    * @return
    *   array which contains sorted elements
    */
  def sortFunctionally[A: Ordering: ClassTag](in: Slice[A]): Array[A] = {
    val out = Array.ofDim[A](in.size)
    sortFunctionally(in, WritableSlice(out)())
    out
  }
}
