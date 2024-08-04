package com.github.skozlov.commons.collection

import com.github.skozlov.commons.collection.SliceOps.checkBounds

import scala.collection.{View, mutable}

trait WritableSliceOps[
    A,
    Underlying <: SomeMutableIndexedSeqOps[A],
    +SubSlice <: WritableSliceOps[A, Underlying, SubSlice],
] extends SliceOps[A, Underlying, SubSlice]
    with mutable.IndexedSeqOps[A, View, View[A]] {
  this: SubSlice =>

  override def update(idx: Int, elem: A): Unit = {
    underlying(indexToUnderlying(idx)) = elem
  }
}

type WritableSlice[A] = WritableSliceOps[
  A,
  _ <: SomeMutableIndexedSeqOps[A],
  _ <: WritableSliceOps[A, _, _],
]

object WritableSlice {
  class Impl[A](
      override val underlying: SomeMutableIndexedSeqOps[A],
      override val from: Int,
      override val until: Int,
  ) extends WritableSliceOps[A, SomeMutableIndexedSeqOps[A], Impl[A]] {

    checkBounds(underlying, from, until)

    override protected def newSubSlice(
        underlying: SomeMutableIndexedSeqOps[A],
        from: Int,
        until: Int,
    ): Impl[A] = {
      Impl(underlying, from, until)
    }
  }

  def apply[A](
      underlying: SomeMutableIndexedSeqOps[A]
  )(from: Int = 0, until: Int = underlying.length): WritableSlice[A] = {
    Impl(underlying, from, until)
  }
}
