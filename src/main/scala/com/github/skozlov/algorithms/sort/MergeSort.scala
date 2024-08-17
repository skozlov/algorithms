package com.github.skozlov.algorithms.sort

import com.github.skozlov.commons.collection.{Slice, WritableSlice}

import scala.annotation.tailrec
import scala.math.Ordered.orderingToOrdered

/** Simple sorting algorithm based on merging two sorted subsequences into one
  * sorted sequence.
  *
  * Time consumption: O(n log n).
  *
  * Memory consumption: O(n).
  *
  * @see
  *   [[https://en.wikipedia.org/wiki/Merge_sort]]
  */
object MergeSort {
  // noinspection ScalaWeakerAccess
  @tailrec
  def merge[A: Ordering](
      in1: Slice[A],
      in2: Slice[A],
      out: WritableSlice[A],
  ): Unit = {
    require(
      out.size == in1.size + in2.size,
      s"out size = ${out.size} is not sum of in1 size = ${in1.size} and in2 size = ${in2.size}",
    )
    if (in1.nonEmpty && (in2.isEmpty || in1.head <= in2.head)) {
      out(0) = in1.head
      merge(in1.tail, in2, out.tail)
    } else if (in2.nonEmpty) {
      out(0) = in2.head
      merge(in1, in2.tail, out.tail)
    }
  }

  class SmallChunkSort(val chunkSize: Int, val sort: FunctionalSort) {
    require(chunkSize >= 0, "Negative chunk size: " + chunkSize)
  }

  def sortWithBuffers[A: Ordering](
      input: Slice[A],
      primaryBuffer: WritableSlice[A],
      secondaryBuffer: WritableSlice[A],
      smallChunkSort: SmallChunkSort =
        SmallChunkSort(chunkSize = 5, sort = InsertionSort),
  ): WritableSlice[A] = {
    require(
      primaryBuffer.size == input.size,
      s"Size mismatch: input = ${input.size}, primary buffer = ${primaryBuffer.size}",
    )
    require(
      secondaryBuffer.size == input.size,
      s"Size mismatch: input = ${input.size}, secondary buffer = ${secondaryBuffer.size}",
    )

    @tailrec
    def sort(
        input: Slice[A],
        primaryBuffer: WritableSlice[A],
        secondaryBuffer: WritableSlice[A],
        sortedChunkSize: Int,
    ): WritableSlice[A] = {
      require(
        sortedChunkSize < input.size,
        s"sortedChunkSize ($sortedChunkSize) >= input.size (${input.size})",
      )
      val newSortedChunkSize = sortedChunkSize * 2
      val sortedChunks: Iterator[Slice[A]] =
        input.grouped(size = sortedChunkSize)
      val chunkPairs: Iterator[Seq[Slice[A]]] = sortedChunks.grouped(size = 2)
      val outputs: Iterator[WritableSlice[A]] =
        primaryBuffer.grouped(size = newSortedChunkSize)
      for ((inputs, out) <- chunkPairs zip outputs) {
        inputs match {
          case Seq(left, right) =>
            merge(left, right, out)
          case Seq(in) =>
            merge(in, in take 0, out)
        }
      }
      if (newSortedChunkSize >= input.size) {
        primaryBuffer
      } else {
        sort(
          input = primaryBuffer,
          primaryBuffer = secondaryBuffer,
          secondaryBuffer = primaryBuffer,
          sortedChunkSize = newSortedChunkSize,
        )
      }
    }

    if (input.isEmpty) {
      primaryBuffer
    } else if (input.size == 1) {
      primaryBuffer(0) = input(0)
      primaryBuffer
    } else if (smallChunkSort.chunkSize <= 1) {
      sort(input, primaryBuffer, secondaryBuffer, sortedChunkSize = 1)
    } else {
      val inChunks: Iterator[Slice[A]] =
        input.grouped(size = smallChunkSort.chunkSize)
      val outChunks: Iterator[WritableSlice[A]] =
        primaryBuffer.grouped(size = smallChunkSort.chunkSize)
      for ((in, out) <- inChunks zip outChunks) {
        smallChunkSort.sort.sortFunctionally(in, out)
      }
      if (smallChunkSort.chunkSize >= input.size) {
        primaryBuffer
      } else {
        sort(
          input = primaryBuffer,
          primaryBuffer = secondaryBuffer,
          secondaryBuffer = primaryBuffer,
          sortedChunkSize = smallChunkSort.chunkSize,
        )
      }
    }
  }

  def sortWithBuffer[A: Ordering](
      input: WritableSlice[A],
      buffer: WritableSlice[A],
      smallChunkSort: SmallChunkSort =
        SmallChunkSort(chunkSize = 5, sort = InsertionSort),
  ): WritableSlice[A] = {
    sortWithBuffers(
      input = input,
      primaryBuffer = buffer,
      secondaryBuffer = input,
      smallChunkSort = smallChunkSort,
    )
  }
}
