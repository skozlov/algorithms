package com.github.skozlov.commons.collection

import com.github.skozlov.commons.collection.SliceOps.checkBounds

import scala.collection.IndexedSeqView
import scala.collection.IndexedSeqView.SomeIndexedSeqOps

trait SliceOps[
    A,
    Underlying <: SomeIndexedSeqOps[A],
    +SubSlice <: SliceOps[A, Underlying, SubSlice],
] extends IndexedSeqView[A] {
  this: SubSlice =>

  import SliceOps.*

  def underlying: Underlying
  def from: Int
  def until: Int

  override val length: Int = until - from

  // noinspection ScalaWeakerAccess
  @throws[IndexOutOfBoundsException]
  def indexToUnderlying(i: Int): Int = {
    if (i < 0 || i >= length) {
      throw IndexOutOfBoundsException(
        s"$i is out of bounds (min 0, max ${length - 1})"
      )
    }
    from + i
  }

  override def apply(i: Int): A = underlying(indexToUnderlying(i))

  protected def newSubSlice(
      underlying: Underlying,
      from: Int,
      until: Int,
  ): SubSlice

  override def slice(from: Int, until: Int): SubSlice = {
    if (from == 0 && until == this.length) {
      this
    } else {
      checkBounds(this, from, until)
      newSubSlice(underlying, this.from + from, this.from + until)
    }
  }

  override def take(n: Int): SubSlice = {
    slice(0, math.min(math.max(n, 0), length))
  }

  override def drop(n: Int): SubSlice = {
    slice(math.min(math.max(n, 0), length), length)
  }

  override def takeRight(n: Int): SubSlice = drop(length - math.max(n, 0))

  override def dropRight(n: Int): SubSlice = take(length - math.max(n, 0))

  override def tail: SubSlice = {
    if (isEmpty) {
      throw new UnsupportedOperationException("Empty Slice doesn't have .tail")
    }
    drop(1)
  }

  override def init: SubSlice = {
    if (isEmpty) {
      throw new UnsupportedOperationException("Empty Slice doesn't have .init")
    }
    dropRight(1)
  }

  override def tails: Iterator[SubSlice] = {
    for (numDrop <- Iterator.range(0, length + 1)) yield drop(numDrop)
  }

  override def inits: Iterator[SubSlice] = {
    for (numDrop <- Iterator.range(0, length + 1)) yield dropRight(numDrop)
  }

  override def sliding(size: Int, step: Int): Iterator[SubSlice] = {
    require(size >= 1, s"Too small size: $size")
    require(step >= 1, s"Too small step: $step")
    Iterator
      .iterate(0 until size) { range =>
        (range.start + step) until (range.end + step)
      }
      .takeWhile { _.start < length }
      .map { range =>
        this.slice(
          from = range.start,
          until = math.min(range.end, length),
        )
      }
  }

  override def sliding(size: Int): Iterator[SubSlice] = {
    sliding(size, step = 1)
  }

  override def grouped(size: Int): Iterator[SubSlice] = {
    sliding(size, step = size)
  }
}

object SliceOps {
  // noinspection ScalaWeakerAccess
  @throws[IllegalArgumentException]
  def checkBounds[A](
      underlying: SomeIndexedSeqOps[A],
      from: Int,
      until: Int,
  ): Unit = {
    require(from >= 0, s"Negative `from`: $from")
    require(from <= until, s"`from` ($from) is greater than `until` ($until)")
    require(
      until <= underlying.length,
      s"`until` ($until) is greater than underlying length (${underlying.length})",
    )
  }
}

type Slice[A] = SliceOps[A, _ <: SomeIndexedSeqOps[A], _ <: SliceOps[A, _, _]]

object Slice {
  class Impl[A](
      override val underlying: SomeIndexedSeqOps[A],
      override val from: Int,
      override val until: Int,
  ) extends SliceOps[A, SomeIndexedSeqOps[A], Impl[A]] {

    checkBounds(underlying, from, until)

    override protected def newSubSlice(
        underlying: SomeIndexedSeqOps[A],
        from: Int,
        until: Int,
    ): Impl[A] = {
      Impl(underlying, from, until)
    }
  }

  def apply[A](
      underlying: SomeIndexedSeqOps[A]
  )(from: Int = 0, until: Int = underlying.length): Slice[A] = {
    Impl(underlying, from, until)
  }
}
